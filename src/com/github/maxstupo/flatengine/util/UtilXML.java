package com.github.maxstupo.flatengine.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class contains a list of methods to help extract data out of a XML file.
 * 
 * @author Maxstupo
 */
public final class UtilXML {

    private static final XPath path = XPathFactory.newInstance().newXPath();

    private UtilXML() {
    }

    private static XPathExpression createXPathExpression(String xpath) {
        try {
            return path.compile(xpath);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a document object representing a XML file.
     * 
     * @param file
     *            the path to the file.
     * @return a document object representing a XML file.
     * @throws SAXException
     *             if any parse errors occur.
     * @throws IOException
     *             if any IO errors occur.
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the configuration requested.
     */
    public static Document loadDocument(File file) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        return dBuilder.parse(file);
    }

    /**
     * Returns a document object representing a XML file.
     * 
     * @param path
     *            the path to the file resource.
     * @return a document object representing a XML file.
     * @throws SAXException
     *             if any parse errors occur.
     * @throws IOException
     *             if any IO errors occur.
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the configuration requested.
     */
    public static Document loadDocument(String path) throws SAXException, IOException, ParserConfigurationException {

        try (InputStream file = UtilXML.class.getClassLoader().getResourceAsStream(path)) {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(file);
        }
    }

    /**
     * Returns a string value from the given xpath or the defaultValue if the xpath is invalid.
     * 
     * @param doc
     *            the object to evaluate.
     * @param xpath
     *            the xpath.
     * @param defaultValue
     *            the defaultValue returned if xpath is invalid.
     * @return a node from the given xpath or null if the xpath is invalid.
     */
    public static String xpathGetString(Object doc, String xpath, String defaultValue) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            return (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * Returns a node list from the given xpath or null if the xpath is invalid.
     * 
     * @param doc
     *            the object to evaluate.
     * @param xpath
     *            the xpath.
     * @return a node list from the given xpath or null if the xpath is invalid.
     */
    public static NodeList xpathGetNodeList(Object doc, String xpath) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a node from the given xpath or null if the xpath is invalid.
     * 
     * @param doc
     *            the object to evaluate.
     * @param xpath
     *            the xpath.
     * @return a node from the given xpath or null if the xpath is invalid.
     */
    public static Node xpathGetNode(Object doc, String xpath) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            return (Node) expr.evaluate(doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a double value from the given xpath or the defaultValue if the xpath is invalid.
     * 
     * @param doc
     *            the object to evaluate.
     * @param xpath
     *            the xpath.
     * @param defaultValue
     *            the defaultValue returned if xpath is invalid.
     * @return a double value from the given xpath or the defaultValue if the xpath is invalid.
     */
    public static double xpathGetNumber(Object doc, String xpath, double defaultValue) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            double value = (double) expr.evaluate(doc, XPathConstants.NUMBER);
            if (Double.isNaN(value)) {
                return defaultValue;
            }
            return value;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * Returns a boolean value from the given xpath or the defaultValue if the xpath is invalid.
     * 
     * @param doc
     *            the object to evaluate.
     * @param xpath
     *            the xpath.
     * @param defaultValue
     *            the defaultValue returned if xpath is invalid.
     * @return a boolean value from the given xpath or the defaultValue if the xpath is invalid.
     */
    public static boolean xpathGetBoolean(Object doc, String xpath, boolean defaultValue) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            boolean value = (boolean) expr.evaluate(doc, XPathConstants.BOOLEAN);
            return value;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
