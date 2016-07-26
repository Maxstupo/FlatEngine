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
 *
 * @author Maxstupo
 */
public class UtilXML {

    private static final XPath path = XPathFactory.newInstance().newXPath();

    private UtilXML() {
    }

    public static XPathExpression createXPathExpression(String xpath) {
        try {
            return path.compile(xpath);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document loadDocument(File file) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        return dBuilder.parse(file);
    }

    public static Document loadDocument(String path) throws SAXException, IOException, ParserConfigurationException {

        try (InputStream file = UtilXML.class.getClassLoader().getResourceAsStream(path)) {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(file);
        }

    }

    public static String xpathGetString(Object doc, String xpath) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            return (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NodeList xpathGetNodeList(Object doc, String xpath) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Node xpathGetNode(Object doc, String xpath) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            return (Node) expr.evaluate(doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    public static boolean xpathGetBoolean(Object doc, String xpath, boolean defaultValue) {
        XPathExpression expr = createXPathExpression(xpath);
        try {
            boolean value = (boolean) expr.evaluate(doc, XPathConstants.BOOLEAN);
            return value;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
