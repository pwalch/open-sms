package pwalch.net.opensms;

import android.test.ActivityTestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by pierre on 06.08.14.
 */
public class StorageTest extends ActivityTestCase {
    protected static DocumentBuilderFactory fDocumentBuilderFactory;
    protected static DocumentBuilder fDocumentBuilder;

    protected void setUp() throws Exception {
        super.setUp();

        fDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        fDocumentBuilder = fDocumentBuilderFactory.newDocumentBuilder();
    }

    protected Node getDocumentRootFromString(String xmlDocumentText) throws IOException, SAXException {
        final InputStream stream = new ByteArrayInputStream(
                xmlDocumentText.getBytes(Charset.defaultCharset()));
        final Document document = fDocumentBuilder.parse(stream);
        return document.getDocumentElement();
    }

    protected Node getDocumentRootFromFile(File xmlFile) throws IOException, SAXException {
        final InputStream stream = new FileInputStream(xmlFile);
        final Document document = fDocumentBuilder.parse(stream);
        return document.getDocumentElement();
    }

}
