package com.github.maxstupo.flatengine.map.reader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.github.maxstupo.flatengine.map.object.MapObject;
import com.github.maxstupo.flatengine.map.tile.Tileset;
import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.UtilXML;
import com.github.maxstupo.flatengine.util.math.AbstractBasicShape;
import com.github.maxstupo.flatengine.util.math.Circle;
import com.github.maxstupo.flatengine.util.math.Rectangle;
import com.github.maxstupo.flatengine.util.math.UtilMath;

/**
 * This class can load a .tmx map file into a {@link TiledMap} object.
 * 
 * @author Maxstupo
 */
public class TmxMapReader {

    private static TmxMapReader instance;

    private TmxMapReader() {
    }

    /**
     * Loads the given map and returns the map object.
     * 
     * @param id
     *            the id of the map.
     * @param file
     *            the path to the map file.
     * @return a new map object loaded with the given map file.
     * @throws RuntimeException
     *             if a error occurs.
     * @throws SAXException
     *             if a error occurs.
     * @throws IOException
     *             if a error occurs.
     * @throws ParserConfigurationException
     *             if a error occurs.
     */
    public TiledMap load(String id, File file) throws RuntimeException, SAXException, IOException, ParserConfigurationException {
        Document doc = UtilXML.loadDocument(file);

        if (!isTmxMap(doc))
            throw new RuntimeException("XML file isn't formatted as a TMX map!");

        TiledMap map = createMap(id, doc);

        readTilesets(map, doc, file);
        readMapTileLayers(map, doc, file);
        readMapObjects(map, doc, file);
        return map;

    }

    private static List<ObjectLayer> readObjects(TiledMap map, Object doc, File mapFile, String xpath, boolean nameCheck) {
        Node node;
        List<ObjectLayer> ll = new ArrayList<>();
        NodeList nList = UtilXML.xpathGetNodeList(doc, (xpath != null) ? (xpath + "/objectgroup") : "objectgroup");
        for (int i = 0; (node = nList.item(i)) != null; i++) {
            String name = UtilXML.xpathGetString(node, "@name", null);
            float alpha = (float) UtilXML.xpathGetNumber(node, "@opacity", 1f);
            MapProperties properties = readProperties(node, null);

            if (name == null && nameCheck)
                throw new RuntimeException("An object group doesn't have a name set for map: '" + mapFile.getAbsolutePath() + "'");

            ObjectLayer layer = new ObjectLayer(map, name, alpha, false, properties);

            Node n;
            NodeList list = UtilXML.xpathGetNodeList(node, "object");
            for (int j = 0; (n = list.item(j)) != null; j++) {
                int id = (int) UtilXML.xpathGetNumber(n, "@id", -1);
                String objectName = UtilXML.xpathGetString(n, "@name", "");
                String type = UtilXML.xpathGetString(n, "@type", "");
                float x = (float) (UtilXML.xpathGetNumber(n, "@x", 0) / map.getTileWidth());
                float y = (float) (UtilXML.xpathGetNumber(n, "@y", 0) / map.getTileWidth());
                float width = (float) (UtilXML.xpathGetNumber(n, "@width", 0) / map.getTileWidth());
                float height = (float) (UtilXML.xpathGetNumber(n, "@height", 0) / map.getTileWidth());

                AbstractBasicShape shape = null;
                if (UtilXML.xpathGetNode(n, "ellipse") != null) {
                    if (width == height) {
                        float radius = width / 2f;
                        shape = new Circle(x - radius, y - radius, radius);
                    } else {
                        throw new RuntimeException("Ellipse shapes are not supported for map objects: " + mapFile.getAbsolutePath() + ", id: " + id + ", name: " + objectName);
                    }
                } else {
                    shape = new Rectangle(x, y, width, height);
                }
                MapObject object = new MapObject(layer, id, objectName, type, shape);
                layer.addObject(object);

            }
            ll.add(layer);
        }
        return ll;
    }

    private static void readMapObjects(TiledMap map, Document doc, File mapFile) {
        for (ObjectLayer layer : readObjects(map, doc, mapFile, "map", true))
            map.addLayer(layer);

    }

    private static void readMapTileLayers(TiledMap map, Document doc, File mapFile) throws RuntimeException {
        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, "map/layer");
        for (int i = 0; (node = nList.item(i)) != null; i++) {
            String name = UtilXML.xpathGetString(node, "@name", null);
            float alpha = (float) UtilXML.xpathGetNumber(node, "@opacity", 1f);
            boolean isVisible = UtilXML.xpathGetBoolean(node, "@visible");
            MapProperties properties = readProperties(node, null);

            if (name == null)
                throw new RuntimeException("Layer name not set for: " + mapFile.getAbsolutePath());

            TileLayer layer = new TileLayer(map, name, alpha, isVisible, properties);
            loadTileData(layer, node);
            map.addLayer(layer);
        }

        map.calculateRenderableLayers();
    }

    private static void loadTileData(TileLayer layer, Node node) throws RuntimeException {

        String data = UtilXML.xpathGetString(node, "data", null);
        if (data == null)
            throw new RuntimeException("Tile data isn't set for: '" + layer.getId() + "'");

        String encoding = UtilXML.xpathGetString(node, "data/@encoding", null);
        String compression = UtilXML.xpathGetString(node, "data/@compression", "");

        if (encoding == null)
            throw new RuntimeException("Layer encoding not set for: '" + layer.getId() + "'");

        if (encoding.equalsIgnoreCase("csv")) { // Uncompressed CSV

            StringTokenizer tokenizer = new StringTokenizer(data.trim(), ",");

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
        } else if (encoding.equalsIgnoreCase("base64") && compression.equalsIgnoreCase("gzip")) { // Base64 GZIP compressed.
            throw new RuntimeException("Map Reader doesn't support Base64 GZIP layer data yet! Layer: '" + layer.getId() + "'");
        }
    }

    private static void readTilesets(TiledMap map, Document doc, File mapFile) throws RuntimeException, SAXException, IOException, ParserConfigurationException {
        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, "map/tileset");

        for (int i = 0; (node = nList.item(i)) != null; i++) {
            int firstGid = (int) UtilXML.xpathGetNumber(node, "@firstgid", -1);
            if (firstGid == -1)
                continue;

            String source = UtilXML.xpathGetString(node, "@source", null);

            if (source != null) { // Load .tsx file.
                File tilesetFile = new File(mapFile.getParentFile(), source);
                Document tilesetDoc = UtilXML.loadDocument(tilesetFile);

                readTileset(map, firstGid, tilesetFile, mapFile, UtilXML.xpathGetNode(tilesetDoc, "tileset"));

            } else { // Load embedded tileset.
                readTileset(map, firstGid, mapFile, mapFile, node);

            }
        }

    }

    private static void readTileset(TiledMap map, int firstGid, File tilesetFile, File mapFile, Node node) throws RuntimeException, IOException {
        String name = UtilXML.xpathGetString(node, "@name", null);
        int tileWidth = (int) UtilXML.xpathGetNumber(node, "@tilewidth", -1);
        int tileHeight = (int) UtilXML.xpathGetNumber(node, "@tileheight", -1);
        int tileSpacing = (int) UtilXML.xpathGetNumber(node, "@spacing", 0);
        int tileMargin = (int) UtilXML.xpathGetNumber(node, "@margin", 0);

        if (name == null)
            throw new RuntimeException("Tileset name not set for: " + tilesetFile.getAbsolutePath());
        if (tileWidth == -1)
            throw new RuntimeException("Tileset tilewidth not set for: " + tilesetFile.getAbsolutePath());
        if (tileHeight == -1)
            throw new RuntimeException("Tileset tileheight not set for: " + tilesetFile.getAbsolutePath());

        Node imageNode = UtilXML.xpathGetNode(node, "image");
        String src = UtilXML.xpathGetString(imageNode, "@source", null);
        Color transparentColor = Util.hexToColor(UtilXML.xpathGetString(imageNode, "@trans", "#ffffff"));

        if (src == null)
            throw new RuntimeException("Tileset source not set for: " + tilesetFile.getAbsolutePath());

        File imageFile = new File(tilesetFile.getParentFile(), src).getCanonicalFile();
        BufferedImage tilesetImage = Util.loadImage(imageFile, transparentColor);

        Map<Integer, MapProperties> tileProperties = new HashMap<>();
        Map<Integer, List<MapObject>> tileCollisions = new HashMap<>();

        Node tileNode;
        NodeList nList = UtilXML.xpathGetNodeList(node, "tile");

        for (int i = 0; (tileNode = nList.item(i)) != null; i++) {
            int tileId = (int) UtilXML.xpathGetNumber(tileNode, "@id", -1);

            if (tileId == -1)
                continue;

            List<MapObject> collisions = new ArrayList<>();

            for (ObjectLayer layer : readObjects(map, tileNode, mapFile, null, false))
                collisions.addAll(layer.getObjects());

            tileCollisions.put(tileId, collisions);

            MapProperties properties = readProperties(tileNode, null);
            tileProperties.put(tileId, properties);

        }

        Tileset tileset = new Tileset(firstGid, name, tileWidth, tileHeight, tileSpacing, tileMargin, tilesetImage, tileProperties, tileCollisions);
        map.getTilesetStore().addTileset(tileset, true);
    }

    private static TiledMap createMap(String id, Document doc) throws RuntimeException {
        int width = (int) UtilXML.xpathGetNumber(doc, "map/@width", -1);
        int height = (int) UtilXML.xpathGetNumber(doc, "map/@height", -1);
        int tileWidth = (int) UtilXML.xpathGetNumber(doc, "map/@tilewidth", -1);
        int tileHeight = (int) UtilXML.xpathGetNumber(doc, "map/@tileheight", -1);
        Color backgroundColor = Util.hexToColor(UtilXML.xpathGetString(doc, "map/@backgroundColor", "#ffffff"));

        if (width == -1 || height == -1 || tileWidth == -1 || tileHeight == -1)
            throw new RuntimeException("One or more map attributes not set.");

        MapProperties properties = readProperties(doc, "map");
        String name = properties.get("name", String.class, null);

        properties.remove("name");

        return new TiledMap(id, name, width, height, tileWidth, tileHeight, backgroundColor, properties);
    }

    private static MapProperties readProperties(Object doc, String xpath) {
        String path = (xpath == null) ? "properties/property" : (xpath + "/properties/property");

        MapProperties properties = new MapProperties();

        Node node;
        NodeList nList = UtilXML.xpathGetNodeList(doc, path);
        for (int i = 0; (node = nList.item(i)) != null; i++) {
            String name = UtilXML.xpathGetString(node, "@name", null);
            String value = UtilXML.xpathGetString(node, "@value", null);
            String type = UtilXML.xpathGetString(node, "@type", "string");

            properties.parse(name, type, value);
        }

        return properties;
    }

    private static boolean isTmxMap(Document doc) {
        return doc.getDocumentElement().getNodeName().equalsIgnoreCase("map");
    }

    /**
     * Returns the single instance of the tiled map reader.
     * 
     * @return the single instance of the tiled map reader.
     */
    public static TmxMapReader get() {
        if (instance == null)
            instance = new TmxMapReader();
        return instance;
    }
}
