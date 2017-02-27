package nl.knaw.dans.easy;

import java.util.LinkedList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DocumentPruner {
    private Document doc;

    public DocumentPruner(Document doc) {
        this.doc = doc;
    }

    public Document prune() {
        XPathExpression xpath;
        try {
            xpath = XPathFactory.newInstance().newXPath().compile("//*[count(./*) = 0]");
            List<Element> ees = getEmptyElements((NodeList) xpath.evaluate(doc, XPathConstants.NODESET));
            while (ees.size() > 0) {
                removeElements(ees);
                ees = getEmptyElements((NodeList) xpath.evaluate(doc, XPathConstants.NODESET));
            }
            return doc;
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException("Cannot compile XPath expression", e);
        }
    }

    private static List<Element> getEmptyElements(NodeList nodeList) {
        List<Element> result = new LinkedList<Element>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Element el = (Element) nodeList.item(i);
            if (el.getTextContent() == null || el.getTextContent().trim().length() == 0) {
                result.add(el);
            }
        }
        return result;
    }

    private static void removeElements(List<Element> es) {
        for (Element e : es) {
            e.getParentNode().removeChild(e);
        }
    }
}
