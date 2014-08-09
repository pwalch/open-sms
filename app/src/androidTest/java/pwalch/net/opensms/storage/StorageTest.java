package pwalch.net.opensms.storage;

import android.content.Context;
import android.test.ActivityTestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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

    protected static final String MESSAGE_FIRST_XML =
            "<message>"
                    + "<date>1000</date>"
                    + "<direction>me_to_you</direction>"
                    + "<text>Vous voulez un whisky ?</text>"
                    + "</message>";

    protected static final String MESSAGE_SECOND_XML =
            "<message>"
                    + "<date>2000</date>"
                    + "<from>you_to_me</from>"
                    + "<text>Oui juste un doigt.</text>"
                    + "</message>";

    protected static final String MESSAGE_THIRD_XML =
            "<message>"
                    + "<date>3000</date>"
                    + "<from>me_to_you</from>"
                    + "<text>Vous voulez pas un whisky d'abord ?</text>"
                    + "</message>";

    protected static final String MESSAGE_FOURTH_XML =
            "<message>"
                    + "<date>1000</date>"
                    + "<from>me_to_you</from>"
                    + "<text>Luke, je suis ton père.</text>"
                    + "</message>";

    protected static final String MESSAGE_FIFTH_XML =
            "<message>"
                    + "<date>2000</date>"
                    + "<from>me_to_you</from>"
                    + "<text>NOOOOOOOOOOON !</text>"
                    + "</message>";

    protected static final String MESSAGE_LIST_1_FILENAME = "messages_1.xml";

    protected static final String MESSAGE_LIST_1_XML =
            "<messageList>"
                    + MESSAGE_FIRST_XML
                    + MESSAGE_SECOND_XML
                    + MESSAGE_THIRD_XML
                    + "</messageList>";

    protected static final String MESSAGE_LIST_2_FILENAME = "messages_2.xml";

    protected static final String MESSAGE_LIST_2_XML =
            "<messageList>"
                    + MESSAGE_FOURTH_XML
                    + MESSAGE_FIFTH_XML
                    + "</messageList>";

    protected static final String CONTACT_FIELD_PIERRE = "<contact>"
            + "<name>Pierre</name>"
            + "<phoneNumber>+33 6 95 95 95</phoneNumber>"
            + "<conversationFilename>messages_1.xml</conversationFilename>"
            + "</contact>";

    protected static final String CONTACT_FIELD_JON = "<contact>"
            + "<name>Jon</name>"
            + "<phoneNumber>+41 6 85 85 85</phoneNumber>"
            + "<conversationFilename>messages_2.xml</conversationFilename>"
            + "</contact>";

    protected static final String CONTACT_LIST_XML =
            "<contactList>"
                    + CONTACT_FIELD_PIERRE
                    + CONTACT_FIELD_JON
                    + "</contactList>";

    protected void setUp() throws Exception {
        super.setUp();

        fDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        fDocumentBuilder = fDocumentBuilderFactory.newDocumentBuilder();
    }

    protected Node getDocumentRootFromString(String xmlDocumentText)
            throws IOException, SAXException {
        final InputStream stream = new ByteArrayInputStream(
                xmlDocumentText.getBytes(Charset.defaultCharset()));
        final Document document = fDocumentBuilder.parse(stream);
        return document.getDocumentElement();
    }

    protected Node getDocumentRootFromFile(File xmlFile)
            throws IOException, SAXException {
        final InputStream stream = new FileInputStream(xmlFile);
        final Document document = fDocumentBuilder.parse(stream);
        return document.getDocumentElement();
    }

}
