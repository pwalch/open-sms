package pwalch.net.opensms;

import android.app.Application;
import android.test.ActivityTestCase;
import android.test.ApplicationTestCase;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by pierre on 05.08.14.
 */
public class StorageTest extends ActivityTestCase {

    public void testPopulateStorage() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            String contactTag = "<contact>"
                                + "<name>Pierre</name>"
                                + "<phoneNumber>+33 6 95 95 95</phoneNumber>"
                                + "<conversationFilename>3521-number.xml</conversationFilename>"
                                + "</contact>";
            InputStream stream = new ByteArrayInputStream(
                                    contactTag.getBytes(Charset.defaultCharset()));
            Document document = builder.parse(stream);
            Node root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            Contact contact = Storage.findContact(nodeList);

            assertTrue(contact.getName().equals("Pierre")
                        && contact.getNumber().equals("+33 6 95 95 95"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
