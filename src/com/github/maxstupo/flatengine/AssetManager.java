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
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 *
 * @author Maxstupo
 */
public class AssetManager {

    private final Map<String, Font> fonts = new HashMap<>();
    private final Map<String, Sprite> sprites = new HashMap<>();

    public AssetManager() {
    }

    public void loadAssets(String path) {
        loadAssets(null, path);
    }

    public void loadAssets(JFlatLog log, String path) {
        if (log != null)
            log.debug(getClass().getSimpleName(), "Loading assets from file: '{0}'", path);
        try {
            Document doc = UtilXML.loadDocument(path);

            Node node;
            NodeList nList;

            /* Load fonts */
            nList = UtilXML.xpathGetNodeList(doc, "assets/font/@path");
            for (int i = 0; ((node = nList.item(i)) != null); i++) {
                String fontPath = node.getNodeValue();
                if (fontPath != null && !fontPath.isEmpty())
                    registerFont(log, fontPath);
            }

            /* Load sprites */
            nList = UtilXML.xpathGetNodeList(doc, "assets/sprite");
            for (int i = 0; ((node = nList.item(i)) != null); i++) {
                String key = UtilXML.xpathGetString(node, "@key");
                String spritePath = UtilXML.xpathGetString(node, "@path");

                BufferedImage sprite = Util.createImage(spritePath);
                registerSprite(log, key, sprite);
            }

        } catch (ParserConfigurationException e) {
            if (log != null)
                log.warn(getClass().getSimpleName(), "XML config error, while loading asset file -", e);
        } catch (SAXException e) {
            if (log != null)
                log.warn(getClass().getSimpleName(), "Incorrectly formatted XML asset file -", e);
        } catch (IOException e) {
            if (log != null)
                log.warn(getClass().getSimpleName(), "Failed to load asset file -", e);
        }
    }

    public void registerSprite(String key, BufferedImage sprite) {
        registerSprite(null, key, sprite);
    }

    public void registerSprite(JFlatLog log, String key, BufferedImage sprite) {
        if (key == null || sprite == null)
            return;
        if (log != null)
            log.fine(getClass().getSimpleName(), "Registering sprite: '{0}'", key);
        sprites.put(key, new Sprite(sprite, key));
    }

    public void registerFont(String path) {
        registerFont(null, path);
    }

    public void registerFont(JFlatLog log, String path) {
        if (path == null)
            return;

        try (InputStream url = AssetManager.class.getClassLoader().getResourceAsStream(path)) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, url);

            font = font.deriveFont(Font.PLAIN, 20);

            // GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // ge.registerFont(font);

            String key = new File(path).getName();
            fonts.put(key, font);
            if (log != null)
                log.fine(getClass().getSimpleName(), "Regisering font: '{0}'", key);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        sprites.clear();
    }

    public Sprite getSprite(String key) {
        return sprites.get(key);
    }

    public Font getFont(String key) {
        return fonts.get(key);
    }

    public Font getFont(String key, float size) {
        Font font = getFont(key);
        if (font == null)
            return null;
        return font.deriveFont(size);
    }

}
