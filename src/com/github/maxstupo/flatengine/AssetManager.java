package com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.UtilXML;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * This class handles all assets within the game engine.
 * 
 * @author Maxstupo
 */
public class AssetManager {

    private final JFlatLog log;
    private final Map<String, Sprite> sprites = new HashMap<>();
    private final Map<String, Font> fonts = new HashMap<>();

    /**
     * Create a new {@link AssetManager} object.
     * 
     * @param log
     *            the logger used to log information.
     */
    public AssetManager(JFlatLog log) {
        this.log = log;
    }

    /**
     * Loads assets from a XML file.
     * 
     * @param file
     *            the XML file.
     * @throws IllegalArgumentException
     *             if the given file is null or doesn't exist.
     * @throws Exception
     *             if an error occurred.
     */
    public void loadFromXml(String file) throws IllegalArgumentException, Exception {
        if (file == null)
            throw new IllegalArgumentException("File is null or doesn't exist: " + file);

        log.debug(getClass().getSimpleName(), "Loading assets from xml file: '{0}'", file);

        Document doc = UtilXML.loadDocument(file);

        Node node;
        NodeList nList;

        /* Load sprites */
        nList = UtilXML.xpathGetNodeList(doc, "assets/sprite");
        for (int i = 0; ((node = nList.item(i)) != null); i++) {
            String key = UtilXML.xpathGetString(node, "@key", null);
            String path = UtilXML.xpathGetString(node, "@path", null);

            if (key == null || path == null)
                continue;

            Path p = Util.path(file, path);
            registerSprite(key, p.toString());
        }

        /* Load fonts */
        nList = UtilXML.xpathGetNodeList(doc, "assets/font");
        for (int i = 0; ((node = nList.item(i)) != null); i++) {
            String key = UtilXML.xpathGetString(node, "@key", null);
            String path = UtilXML.xpathGetString(node, "@path", null);

            if (key == null || path == null)
                continue;

            Path p = Util.path(file, path);
            registerFont(key, p.toString());
        }
    }

    /**
     * Clears all sprites and fonts from this asset manager.
     */
    public void clear() {
        sprites.clear();
        fonts.clear();
    }

    /**
     * Register the given image with the given key.
     * 
     * @param key
     *            the key.
     * @param file
     *            the file of the sprite image.
     * 
     * @return true if the manager added the sprite, false if the manager contains the given key.
     * @throws IllegalArgumentException
     *             if the given key or file are null or the file doesn't exist.
     */
    public boolean registerSprite(String key, String file) throws IllegalArgumentException {
        if (key == null || file == null)
            throw new IllegalArgumentException("The key or file is null!");

        if (sprites.containsKey(key))
            return false;

        try {
            BufferedImage sprite = Util.loadImage(file, Color.white);
            sprites.put(key, new Sprite(sprite, key));
            log.fine(getClass().getSimpleName(), "Registered sprite: '{0}'", key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Register a true type font.
     * 
     * @param key
     *            the id of the font.
     * @param file
     *            the font file location.
     * @return true if the font was registered, false if the given key is already used within the manager.
     * @throws IllegalArgumentException
     *             if the given file was null or the file doesn't exist.
     * @throws Exception
     *             if the font file format is incorrect, or an I/O error occurred.
     */
    public boolean registerFont(String key, String file) throws IllegalArgumentException, Exception {
        if (file == null)
            throw new IllegalArgumentException("The file is null!");

        if (fonts.containsKey(key))
            return false;
        // System.out.println(file);
        try (InputStream is = AssetManager.class.getClassLoader().getResourceAsStream(file)) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 18);

            fonts.put(key, font);
            log.fine(getClass().getSimpleName(), "Registered font: '{0}'", key);
            return true;
        }
    }

    /**
     * Returns a font referenced by a given key.
     * 
     * @param key
     *            the key of the font to return.
     * @return the font.
     */
    public Font getFont(String key) {
        return fonts.get(key);
    }

    /**
     * Returns a new font with the given size referenced by the given key.
     * 
     * @param key
     *            the key of the font to return.
     * @param size
     *            the new font size.
     * @return a new derived font object.
     */
    public Font getFont(String key, float size) {
        Font font = getFont(key);
        if (font == null)
            return null;
        return font.deriveFont(size);
    }

    /**
     * Returns a sprite referenced by a given key.
     * 
     * @param key
     *            the key of the sprite to return.
     * @return the sprite.
     */
    public Sprite getSprite(String key) {
        return sprites.get(key);
    }
}