package pwalch.net.opensms.storage;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 04.08.14.
 */
public class Storage {

    private Context mContext;
    private DocumentBuilderFactory mDocumentBuilderFactory;
    private DocumentBuilder mDocumentBuilder;

    private static final String APP_FOLDER = "open_sms";
    private static final String CONTACT_FILENAME = "contact.xml";

    public Storage(Context context) throws ParserConfigurationException, IOException {
        mContext = context;
        mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
    }

    protected String getAppFolderName() {
        return mContext.getFilesDir().getAbsolutePath() + "/" + Storage.APP_FOLDER;
    }

    protected String getContactFilename() {
        return getAppFolderName() + "/" + CONTACT_FILENAME;
    }

    public List<Contact> retrieveContactList() throws IOException, SAXException {
        Document document = mDocumentBuilder.parse(
                                new FileInputStream(
                                    new File(getContactFilename())));

        Node root = document.getDocumentElement();
        assert root.getNodeName().equals("contacts");

        return findContactList(root.getChildNodes());
    }

    protected static List<Contact> findContactList(NodeList contactNodeList) {
        List<Contact> contactList = new ArrayList<Contact>();
        for (int i = 0; i < contactNodeList.getLength(); ++i) {
            Node contactNode = contactNodeList.item(i);
            assert contactNode.hasChildNodes();

            contactList.add(findContact(contactNode.getChildNodes()));
        }
        return contactList;
    }

    protected static Contact findContact(NodeList contactAttributeList) {
        String contactName = "";
        String contactPhoneNumber = "";
        String contactConversationFile = "";
        for (int j = 0; j < contactAttributeList.getLength(); ++j) {
            Node attributeNode = contactAttributeList.item(j);
            String attributeName = attributeNode.getNodeName();
            String attributeValue = attributeNode.getTextContent();

            if (attributeName.equals("name")) {
                contactName = attributeValue;
            } else if (attributeName.equals("phoneNumber")) {
                contactPhoneNumber = attributeValue;
            } else if (attributeName.equals("conversationFilename")) {
                contactConversationFile = attributeValue;
            } else {
                assert false;
            }
        }

        return new Contact(contactName, contactPhoneNumber, contactConversationFile);
    }

    protected static Message findMessage(NodeList messageAttributeList) {
        int date = 0;
        Direction direction = Direction.ME_TO_YOU;
        String text = "";
        for (int j = 0; j < messageAttributeList.getLength(); ++j) {
            Node attributeNode = messageAttributeList.item(j);
            String attributeName = attributeNode.getNodeName();
            String attributeValue = attributeNode.getTextContent();

            if (attributeName.equals("date")) {
                date = Integer.parseInt(attributeValue);
            } else if (attributeName.equals("direction")) {
                if (attributeValue.equals("me_to_you")) {
                    direction = Direction.ME_TO_YOU;
                } else if (attributeValue.equals("you_to_me")) {
                    direction = Direction.YOU_TO_ME;
                } else {
                    assert false;
                }
            } else if (attributeName.equals("text")) {
                text = attributeValue;
            } else {
                assert false;
            }
        }

        return new Message(date, direction, text);
    }

    protected static List<Message> findMessageList(NodeList messageNodeList) {
        List<Message> messageList = new ArrayList<Message>();
        for (int i = 0; i < messageNodeList.getLength(); ++i) {
            Node messageNode = messageNodeList.item(i);
            assert messageNode.hasChildNodes();

            messageList.add(findMessage(messageNode.getChildNodes()));
        }
        return messageList;
    }

    public List<Message> retrieveMessageList(Contact contact)
            throws IOException, SAXException {
        // Read and parse message list file
        File messageListFile = new File(getAppFolderName() + "/" + contact.getMessageListFilename());
        Document messageListDocument = mDocumentBuilder.parse(messageListFile);
        Node root = messageListDocument.getDocumentElement();
        assert root.getNodeName().equals("message");

        return findMessageList(root.getChildNodes());
    }
}
