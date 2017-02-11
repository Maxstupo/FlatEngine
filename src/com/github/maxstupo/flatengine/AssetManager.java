package com.github.maxstupo.flatengine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.UtilXML;

/**
 * This class handles all assets within the game engine.
 * 
 * @deprecated Needs rewrite.
 * @author Maxstupo
 */
@Deprecated
public class AssetManager {

    private final FlatEngine engine;

    private final Map<String, Font> fonts = new HashMap<>();
    private final Map<String, Sprite> sprites = new HashMap<>();

    /**
     * Create a new {@link AssetManager} object.
     * 
     * @param engine
     *            the engine that owns this asset manager.
     */
    public AssetManager(FlatEngine engine) {
        this.engine = engine;
    }

    /**
     * Loads sprites and fonts from an XML asset file.
     * 
     * @param path
     *            the path to the XML file.
     */
    public void loadAssets(String path) {
        engine.getLog().debug(getClass().getSimpleName(), "Loading assets from file: '{0}'", path);
        try {
            Document doc = UtilXML.loadDocument(path);

            Node node;
            NodeList nList;

            /* Load fonts */
            nList = UtilXML.xpathGetNodeList(doc, "assets/font/@path");
            for (int i = 0; ((node = nList.item(i)) != null); i++) {
                String fontPath = node.getNodeValue();
                if (fontPath != null && !fontPath.isEmpty())
                    registerFont(fontPath);
            }

            /* Load sprites */
            nList = UtilXML.xpathGetNodeList(doc, "assets/sprite");
            for (int i = 0; ((node = nList.item(i)) != null); i++) {
                String key = UtilXML.xpathGetString(node, "@key", null);
                String spritePath = UtilXML.xpathGetString(node, "@path", null);

                BufferedImage sprite = Util.createImage(new File(spritePath), null);
                registerSprite(key, sprite);
            }

        } catch (ParserConfigurationException e) {
            engine.getLog().warn(getClass().getSimpleName(), "XML config error, while loading asset file -", e);
        } catch (SAXException e) {
            engine.getLog().warn(getClass().getSimpleName(), "Incorrectly formatted XML asset file -", e);
        } catch (IOException e) {
            engine.getLog().warn(getClass().getSimpleName(), "Failed to load asset file -", e);
        }
    }

    /**
     * Register the given image with the given key.
     * 
     * @param key
     *            the key.
     * @param sprite
     *            the image.
     */
    public void registerSprite(String key, BufferedImage sprite) {
        if (key == null || sprite == null)
            return;
        engine.getLog().fine(getClass().getSimpleName(), "Registering sprite: '{0}'", key);
        sprites.put(key, new Sprite(sprite, key));
    }

    /**
     * Register a true type font, the id of the font is the name of the given font file.
     * 
     * @param path
     *            the path to the font file.
     */
    public void registerFont(String path) {
        if (path == null)
            return;

        try (InputStream url = AssetManager.class.getClassLoader().getResourceAsStream(path)) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, url);

            font = font.deriveFont(Font.PLAIN, 20);

            // GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // ge.registerFont(font);

            String key = new File(path).getName();
            fonts.put(key, font);
            engine.getLog().fine(getClass().getSimpleName(), "Regisering font: '{0}'", key);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears all sprites from this asset manager.
     */
    public void clear() {
        sprites.clear();
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
     * Returns a new font with the given size referenced by the give key.
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

}
