package com.github.maxstupo.flatengine.map.reader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.map.layer.ObjectLayer;
import com.github.maxstupo.flatengine.map.layer.TileLayer;
import com.github.maxstupo.flatengine.map.objects.MapObject;
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
     * @param file
     *            the path to the map file.
     * @return a new map object loaded with the given map file.
     */
    public TiledMap load(String id, File file) {
        try {
            Document doc = UtilXML.loadDocument(file);

            if (!isTmxMap(doc))
                throw new RuntimeException("XML file isn't formatted as a TMX map!");

            TiledMap map = createBlankMap(id, doc);

            readTilesets(map, file, doc);

            readMapLayers(map, doc);
            readMapObjects(map, doc);
            return map;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;

    }

    private void readMapObjects(TiledMap map, Document doc) {
        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, "map/objectgroup");
        for (int i = 0; (node = nList.item(i)) != null; i++) {
            String layerName = UtilXML.xpathGetString(node, "@name", "");
            float alpha = (float) UtilXML.xpathGetNumber(node, "@opacity", 1f);

            ObjectLayer layer = new ObjectLayer(map, layerName, alpha, true, readProperties(node, null));

            Node n;
            NodeList list = UtilXML.xpathGetNodeList(node, "object");
            for (int j = 0; (n = list.item(j)) != null; j++) {

                int id = (int) UtilXML.xpathGetNumber(n, "@id", 0);
                String name = UtilXML.xpathGetString(n, "@name", "");
                String type = UtilXML.xpathGetString(n, "@type", "");
                float x = (float) (UtilXML.xpathGetNumber(n, "@x", 0) / map.getTileWidth());
                float y = (float) (UtilXML.xpathGetNumber(n, "@y", 0) / map.getTileWidth());
                float width = (float) (UtilXML.xpathGetNumber(n, "@width", 0) / map.getTileWidth());
                float height = (float) (UtilXML.xpathGetNumber(n, "@height", 0) / map.getTileWidth());

                MapProperties properties = readProperties(n, null);

                MapObject object = new MapObject(id, name, type, x, y, properties);
                layer.addObject(object);
            }

            map.addLayer(layer);

        }
    }

    private void readMapLayers(TiledMap map, Document doc) {
        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, "map/layer");
        for (int i = 0; (node = nList.item(i)) != null; i++) {
            final String layerName = UtilXML.xpathGetString(node, "@name", null);
            final float layerAlpha = (float) UtilXML.xpathGetNumber(node, "@opacity", 1f);

            TileLayer layer = new TileLayer(map, layerName, layerAlpha, true, readProperties(node, null));

            if (!isDataEncoding(node, "csv")) {
                System.err.println("Ignoring layer '" + layerName + "' as it doesn't use CSV encoding!");
                continue;
            }

            // TODO: Use gzip format.
            final String tileData = UtilXML.xpathGetString(node, "data", "").trim();

            loadTileDataCSV(layer, tileData);
            map.addLayer(layer);
        }

        map.calculateRenderableLayers();
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

    private boolean isDataEncoding(Node node, String type) {
        final String encoding = UtilXML.xpathGetString(node, "data/@encoding", null);
        if (encoding == null)
            return false;
        return encoding.equalsIgnoreCase(type.toLowerCase());
    }

    private void readTilesets(TiledMap map, File mapFile, Document doc) {
        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, "map/tileset");

        for (int i = 0; (node = nList.item(i)) != null; i++) {
            final int firstgid = (int) UtilXML.xpathGetNumber(node, "@firstgid", -1);
            final String source = UtilXML.xpathGetString(node, "@source", null);

            if (source != null && !source.isEmpty()) { // Load a tsx file.
                try {
                    File tilesetFile = new File(mapFile.getParentFile(), source);
                    Document tilesetDoc = UtilXML.loadDocument(tilesetFile);

                    readTileset(map, firstgid, tilesetFile, UtilXML.xpathGetNode(tilesetDoc, "tileset"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else { // Load embedded tileset.

                readTileset(map, firstgid, mapFile, node);
            }

        }
    }

    private void readTileset(TiledMap map, int firstgid, File tilesetFile, Node node) {

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
            File imageFile = new File(tilesetFile.getParentFile(), src).getCanonicalFile();

            BufferedImage image = Util.createImage(imageFile, color);
            Tileset tileset = new Tileset(firstgid, name, tileWidth, tileHeight, tileSpacing, tileMargin, image);

            map.getTilesetStore().addTileset(tileset, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private TiledMap createBlankMap(String id, Document doc) throws RuntimeException {
        int width = (int) UtilXML.xpathGetNumber(doc, "map/@width", -1);
        int height = (int) UtilXML.xpathGetNumber(doc, "map/@height", -1);
        int tileWidth = (int) UtilXML.xpathGetNumber(doc, "map/@tilewidth", -1);
        int tileHeight = (int) UtilXML.xpathGetNumber(doc, "map/@tileheight", -1);
        String color = UtilXML.xpathGetString(doc, "map/@backgroundColor", "#ffffff");
        Color backgroundColor = Util.hexToColor(color);

        if (width == -1 || height == -1)
            throw new RuntimeException("Map dimensions not defined!");

        MapProperties properties = readProperties(doc, "map");
        String name = properties.getString("name", null);

        properties.remove("name");

        TiledMap map = new TiledMap(id, name, width, height, tileWidth, tileHeight, properties);
        map.setBackgroundColor(backgroundColor);

        return map;
    }

    private MapProperties readProperties(Object doc, String xpath) {
        String path = (xpath == null) ? "properties/property" : (xpath + "/properties/property");

        MapProperties properties = new MapProperties();

        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, path);
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
