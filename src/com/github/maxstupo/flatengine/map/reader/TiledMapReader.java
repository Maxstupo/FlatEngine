package com.github.maxstupo.flatengine.map.reader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.map.layer.TileLayer;
import com.github.maxstupo.flatengine.map.tile.Tileset;
import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.UtilXML;
import com.github.maxstupo.flatengine.util.math.UtilMath;

/**
 * This class can load a .tmx map file into a {@link TiledMap} object.
 * 
 * @author Maxstupo
 */
public class TiledMapReader {

    private static TiledMapReader instance;

    private TiledMapReader() {
    }

    /**
     * Load the given map and return the map object.
     * 
     * @param id
     *            the id of the map.
     * @param path
     *            the path to the map file.
     * @return a new map object loaded with the given map file.
     */
    public TiledMap load(String id, String path) {
        try {
            Document doc = UtilXML.loadDocument(path);

            if (!isTmxMap(doc))
                throw new RuntimeException("XML file isn't formatted as a TMX map!");

            TiledMap map = createBlankMap(id, doc);

            readTilesets(map, doc);

            readMapLayers(map, doc);

            return map;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;

    }

    private void readMapLayers(TiledMap map, Document doc) {
        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, "map/layer");
        for (int i = 0; (node = nList.item(i)) != null; i++) {
            final String layerName = UtilXML.xpathGetString(node, "@name", null);
            final float layerAlpha = (float) UtilXML.xpathGetNumber(node, "@opacity", 1f);

            TileLayer layer = new TileLayer(map, layerName, layerAlpha);

            if (!isDataEncodingCsv(node)) {
                System.err.println("Map layer not encoded with CSV! Ignoring! (LayerName: " + layerName + ")");
                continue;
            }

            // TODO: Use gzip format.
            final String tileCsv = UtilXML.xpathGetString(node, "data", "").trim();
            loadTileDataCSV(layer, tileCsv);

            if (layerName.toLowerCase().startsWith("over")) {
                map.addForegroundLayer(layer);

            } else if (layerName.toLowerCase().startsWith("ground")) {
                map.addBackgroundLayer(layer);

            } else {
                System.out.println(layer);
            }
        }

    }

    private void loadTileDataCSV(TileLayer layer, String tileCsv) {
        StringTokenizer tokenizer = new StringTokenizer(tileCsv, ",");

        int x = 0;
        int y = 0;
        while (tokenizer.hasMoreTokens()) {
            final String tileID = tokenizer.nextToken();
            final int id = UtilMath.toInt(tileID.trim());

            layer.setTileAt(x, y, id);

            x++;
            if (x > layer.getMap().getWidth() - 1) {
                x = 0;
                y++;
            }
        }
    }

    private boolean isDataEncodingCsv(Node node) {
        final String encoding = UtilXML.xpathGetString(node, "data/@encoding", null);
        if (encoding == null)
            return false;
        return encoding.equalsIgnoreCase("csv");
    }

    private void readTilesets(TiledMap map, Document doc) {
        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, "map/tileset");

        for (int i = 0; (node = nList.item(i)) != null; i++) {
            final int firstgid = (int) UtilXML.xpathGetNumber(node, "@firstgid", -1);
            final String source = UtilXML.xpathGetString(node, "@source", null);

            if (source != null && !source.isEmpty()) { // Load a tsx file.
                try {

                    Document tilesetDoc = UtilXML.loadDocument(source);

                    readTileset(map, firstgid, UtilXML.xpathGetNode(tilesetDoc, "tileset"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else { // Load embedded tileset.
                readTileset(map, firstgid, node);
            }

        }
    }

    private void readTileset(TiledMap map, int firstgid, Node node) {

        final int tileWidth = (int) UtilXML.xpathGetNumber(node, "@tilewidth", 0);
        final int tileHeight = (int) UtilXML.xpathGetNumber(node, "@tileheight", 0);
        final int tileSpacing = (int) UtilXML.xpathGetNumber(node, "@spacing", 0);
        final int tileMargin = (int) UtilXML.xpathGetNumber(node, "@margin", 0);
        final String name = UtilXML.xpathGetString(node, "@name", null);

        Node imageNode = UtilXML.xpathGetNode(node, "image");
        final String src = UtilXML.xpathGetString(imageNode, "@source", null);
        final String trans = UtilXML.xpathGetString(imageNode, "@trans", null);
        final Color color = Util.hexToColor(trans);

        try {
            BufferedImage image = Util.createImage(src, color);
            Tileset tileset = new Tileset(firstgid, name, tileWidth, tileHeight, tileSpacing, tileMargin, image);

            map.getTilesetStore().addTileset(tileset, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private TiledMap createBlankMap(String id, Document doc) throws RuntimeException {
        int width = (int) UtilXML.xpathGetNumber(doc, "map/@width", -1);
        int height = (int) UtilXML.xpathGetNumber(doc, "map/@height", -1);
        String color = UtilXML.xpathGetString(doc, "map/@backgroundColor", "#000000");
        Color backgroundColor = Util.hexToColor(color);

        if (width == -1 || height == -1)
            throw new RuntimeException("Map dimensions not defined!");

        MapProperties properties = readProperties(doc, "map");
        String name = properties.getString("name", null);

        properties.remove("name");

        TiledMap map = new TiledMap(id, name, width, height, properties);
        map.setBackgroundColor(backgroundColor);

        return map;
    }

    private MapProperties readProperties(Document doc, String xpath) {
        MapProperties properties = new MapProperties();

        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, xpath + "/properties/property");
        for (int i = 0; (node = nList.item(i)) != null; i++) {
            String name = UtilXML.xpathGetString(node, "@name", null);
            String value = UtilXML.xpathGetString(node, "@value", null);
            String type = UtilXML.xpathGetString(node, "@type", "string");

            properties.parseAdd(name, type, value);
        }

        return properties;
    }

    private boolean isTmxMap(Document doc) {
        return doc.getDocumentElement().getNodeName().equalsIgnoreCase("map");
    }

    /**
     * Returns the single instance of the tiled map reader.
     * 
     * @return the single instance of the tiled map reader.
     */
    public static TiledMapReader get() {
        if (instance == null)
            instance = new TiledMapReader();
        return instance;
    }
}
