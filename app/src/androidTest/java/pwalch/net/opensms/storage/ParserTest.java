package pwalch.net.opensms.storage;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import pwalch.net.opensms.examples.ContactExamples;
import pwalch.net.opensms.examples.ListExamples;
import pwalch.net.opensms.examples.MessageExamples;
import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 11.09.14.
 */
public class ParserTest extends BaseTest {

    private DocumentBuilderFactory mDocumentBuilderFactory;
    private DocumentBuilder mDocumentBuilder;

    private Document getDocumentFromString(String xmlDocumentText) throws IOException, SAXException {
        return mDocumentBuilder.parse(getStreamFromString(xmlDocumentText));
    }

    private InputStream getStreamFromString(String xmlDocumentText) {
        return new ByteArrayInputStream(
                xmlDocumentText.getBytes(Charset.defaultCharset()));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
    }

    public void testContactParsing() throws IOException, SAXException {
        final Node contactNode =
                getDocumentFromString(ContactExamples.CONTACT_ENTRY_PIERRE).getDocumentElement();
        final Contact contact = XmlParser.findContact(contactNode);
        assertTrue(contact.getName().equals(ContactExamples.CONTACT_PIERRE_NAME)
                && contact.getPhoneNumber().equals(ContactExamples.CONTACT_PIERRE_PHONE_NUMBER));
    }

    public void testContactListParsing() throws IOException, SAXException {
        final Node contactNodeListRootNode =
            getDocumentFromString(ListExamples.CONTACT_LIST_XML).getDocumentElement();

        List<Contact> contactList = XmlParser.findContactList(contactNodeListRootNode);

        assertTrue(contactList.get(0).getName().equals(ContactExamples.CONTACT_PIERRE_NAME)
                && contactList.get(1).getName().equals(ContactExamples.CONTACT_JON_NAME));
    }

    public void testMessageParsing() throws IOException, SAXException {
        final Node messageNode =
                getDocumentFromString(MessageExamples.MESSAGE_FIRST_XML).getDocumentElement();
        final Message message = XmlParser.findMessage(messageNode);

        assertTrue(message.getDate() == 1000
                && message.getDirection() == Direction.ME_TO_YOU);
    }

    public void testMessageListParsing() throws IOException, SAXException {
        final Node messageNode =
                getDocumentFromString(ListExamples.MESSAGE_LIST_0_XML).getDocumentElement();

        List<Message> messageList = XmlParser.findMessageList(messageNode);

        assertTrue(messageList.get(0).getDate() == 1000
                && messageList.get(1).getDirection() == Direction.YOU_TO_ME);
    }
}
