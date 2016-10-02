package main.Metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pixy.meta.Metadata;
import pixy.meta.MetadataType;
import pixy.meta.jpeg.JPEGMeta;
import pixy.meta.xmp.XMP;
import pixy.string.XMLUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Shirobako on 9/3/2016.
 */
public class XMPManager {

    private static final Logger log = LoggerFactory.getLogger(XMPManager.class);

    private static final String FRESH_XML = "<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>\n" +
            "\t<rdf:Description xmlns:dc='http://purl.org/dc/elements/1.1/' xmlns:xmp='http://ns.adobe.com/xap/1.0/'>\n" +
            "\t\t<dc:subject>\n" +
            "\t\t\t<rdf:Bag>\n" +
            "\t\t\t</rdf:Bag>\n" +
            "\t\t</dc:subject>\n" +
            "\t</rdf:Description>\n" +
            "</rdf:RDF>";

    public void addXmlTags(File inputFile, File outputFile, List<String> tags) throws MetadataExecption {
        try {
            Map<MetadataType, Metadata> metadataMap = Metadata.readMetadata(inputFile);
            XMP xmp = (XMP) metadataMap.get(MetadataType.XMP);
            Document xmpDoc = xmp.getXmpDocument();

            addTags(tags, xmpDoc);
            writeXMP(inputFile, outputFile, xmpDoc, xmp);
        } catch (IOException e) {
            log.error("Exception getting metadata map");
            throw new MetadataExecption(e);
        }
    }

    public void writeNewXmlTags(File inputFile, File outputFile, List<String> tags) throws MetadataExecption {
        Document doc = getNewXML();
        addTags(tags, doc);
        writeXMP(inputFile, outputFile, doc, null);
    }

    public boolean hasTags(File inputFile) {
        try {
            Map<MetadataType, Metadata> metadataMap = Metadata.readMetadata(inputFile);
            XMP xmp = (XMP) metadataMap.get(MetadataType.XMP);
            Document xmpDoc = xmp.getXmpDocument();
            Node bag = xmpDoc.getElementsByTagName("rdf:Bag").item(0);
            if (bag == null) {
                return false;
            } else {
                return true;
            }
        } catch (IOException | NullPointerException e) {
//            log.error("Error doc");
            return false;
        }
    }

    private void writeXMP(File inputFile, File outputFile, Document xmpDoc, XMP xmp) throws MetadataExecption {

        try {
            FileInputStream fin = new FileInputStream(inputFile);
            FileOutputStream fout = new FileOutputStream(outputFile);

            if (xmp != null && xmp.hasExtendedXmp()) {
                System.out.println("Write extendedDocument");
                Document extendedXmpDoc = xmp.getExtendedXmpDocument();
                JPEGMeta.insertXMP(fin, fout, XMLUtils.serializeToString(xmpDoc.getDocumentElement(), "UTF-8"), XMLUtils.serializeToString(extendedXmpDoc));
            } else {
                Metadata.insertXMP(fin, fout, XMLUtils.serializeToString(xmpDoc.getDocumentElement(), "UTF-8"));
            }

            fin.close();
            fout.close();


        } catch (IOException e) {
            log.error("Exception while writing XMP to file: " + outputFile.getAbsolutePath());
            throw new MetadataExecption(e);
        }
    }


    private Document getNewXML() throws MetadataExecption {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmpDoc = builder.parse(new InputSource(new StringReader(FRESH_XML)));
            xmpDoc.getDocumentElement().normalize();
            return xmpDoc;

        } catch (IOException | SAXException | ParserConfigurationException e) {
            log.error("IOException generating fresh XML");
            throw new MetadataExecption(e);
        }
    }

    private void addTags(List<String> tagsToAdd, Document doc) throws MetadataExecption {
        Node bag = doc.getElementsByTagName("rdf:Bag").item(0);

        if (bag == null) {
            throw new MetadataExecption("Bag was null");
        }

        log.info(tagsToAdd.toString());

        for (String newTag : tagsToAdd) {
            addNode(doc, bag, "rdf:li", newTag);
        }
    }

    private void addNode(Document doc, Node node, String nodeName, String nodeValue) {
        Element element = doc.createElement(nodeName);
        element.appendChild(doc.createTextNode(nodeValue));
        node.appendChild(element);
    }

    private void updateAtribute(Document doc, String attributeName, String attributeValue) {
        NamedNodeMap attr = doc.getAttributes();
        Node nodeAttr = attr.getNamedItem(attributeName);
        nodeAttr.setTextContent(attributeValue);
    }

    public void printMetadata(File file) throws MetadataExecption {
        try {
            Map<MetadataType, Metadata> metadataMap = Metadata.readMetadata(file);

            log.info("Start of metadata information:");
            log.info("Total number of metadata entries: {}", metadataMap.size());
            int i = 0;
            for (Map.Entry<MetadataType, Metadata> entry : metadataMap.entrySet()) {
                log.info("Metadata entry {} - {}", i, entry.getKey());
                entry.getValue().showMetadata();
                i++;
                log.info("-----------------------------------------");
            }
            log.info("End of metadata information.");
        } catch (IOException e) {
            log.error("Issue getting MetadataMap in order to output metadata", e);
            throw new MetadataExecption(e);
        }
    }
}
