package pwalch.net.opensms.storage;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import pwalch.net.opensms.storage.parsing.XmlParser;
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

    private static final String UID_XML_NAME = "currentUid";
    private int mCurrentId;

    public Storage(Context context) throws ParserConfigurationException {
        mContext = context;
        mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
    }

    protected void initializeContactListFile()
            throws IOException, SAXException, TransformerException {
        File contactFile = new File(getContactFilename());
        if (!contactFile.exists()) {
            Log.i("tag", "Trying to create file : " + contactFile.getPath());
            InternalStorage.writeToFile(
                contactFile,
                "<?xml version=\"1.0\" ?>\n<contactList></contactList>");
            setCurrentUid(0);
        } else {
            mCurrentId = getCurrentUid();
        }
    }

    protected int getCurrentUid() throws IOException, SAXException {
        Document contactDocument = getContactListDocument();
        return Integer.parseInt(
                contactDocument.getDocumentElement().getAttribute(
                        UID_XML_NAME));
    }

    protected void setCurrentUid(int uid)
            throws IOException, SAXException, TransformerException {
        mCurrentId = uid;
        Document contactDocument = getContactListDocument();
        contactDocument.getDocumentElement().setAttribute(UID_XML_NAME,
                                                          Integer.toString(mCurrentId));
        writeXmlDocument(getContactListFile(), contactDocument);
    }

    public List<Contact> retrieveContactList()
            throws IOException, SAXException, TransformerException {
        initializeContactListFile();

        Document document = mDocumentBuilder.parse(
                                new FileInputStream(
                                    new File(getContactFilename())));

        Node root = document.getDocumentElement();
        assert root.getNodeName().equals("contacts");

        return XmlParser.findContactList(root.getChildNodes());
    }

    public void addContact(String contactName, String contactNumber)
            throws IOException, SAXException, TransformerException {
        initializeContactListFile();

        Log.i("tag", "Adding contact to storage");
        if (containsContact(retrieveContactList(), contactName)) {
            throw new IllegalArgumentException("Contact already exists");
        }

        String uniqueFilename = "conversation_" + mCurrentId + ".xml";
        setCurrentUid(mCurrentId + 1);
        new File(getAppFolderName() + "/" + uniqueFilename).createNewFile();

        Contact contact = new Contact(contactName, contactNumber, uniqueFilename);
        writeContact(contact);

        initializeContactMessageList(contact);
    }

    protected void initializeContactMessageList(Contact contact) throws IOException {
        File messageListFile = getMessageListFile(contact);

        Log.i("tag", "Trying to create file : " + messageListFile.getPath());
        InternalStorage.writeToFile(
            messageListFile,
            "<?xml version=\"1.0\" ?>\n<messageList></messageList>");
    }

    protected void writeContact(Contact contact)
            throws IOException, SAXException, TransformerException {
        Document contactListDocument = getContactListDocument();
        Node root = contactListDocument.getDocumentElement();

        Element nameElement = contactListDocument.createElement("name");
        nameElement.setTextContent(contact.getName());

        Element numberElement = contactListDocument.createElement("phoneNumber");
        numberElement.setTextContent(contact.getNumber());

        Element conversationElement = contactListDocument.createElement("conversationFilename");
        conversationElement.setTextContent(contact.getMessageListFilename());

        Element contactElement = contactListDocument.createElement("contact");
        contactElement.appendChild(nameElement);
        contactElement.appendChild(numberElement);
        contactElement.appendChild(conversationElement);

        root.appendChild(contactElement);

        writeXmlDocument(getContactListFile(), contactListDocument);
    }

    public List<Message> retrieveMessageList(Contact contact)
            throws IOException, SAXException {
        // Read and parse message list file
        Log.i("tag", "Retrieving message list at : " + contact.getMessageListFilename());
        Document messageListDocument = getMessageListDocument(contact);
        Node root = messageListDocument.getDocumentElement();
        assert root.getNodeName().equals("message");

        return XmlParser.findMessageList(root.getChildNodes());
    }

    public void writeMessage(Contact contact, Message message)
            throws IOException, SAXException, TransformerException {
        Document messageListDocument = getMessageListDocument(contact);
        Node root = messageListDocument.getDocumentElement();

        Element dateElement = messageListDocument.createElement("date");
        dateElement.setTextContent(Integer.toString(message.getDate()));

        Element directionElement = messageListDocument.createElement("direction");
        String directionString = "";
        switch (message.getDirection()) {
            case ME_TO_YOU: {
                directionString = "me_to_you";
                break;
            }

            case YOU_TO_ME: {
                directionString = "you_to_me";
            }

            default: {
                assert false;
            }
        }
        directionElement.setTextContent(directionString);

        Element textElement = messageListDocument.createElement("text");
        textElement.setTextContent(message.getText());

        Element messageElement = messageListDocument.createElement("message");
        messageElement.appendChild(dateElement);
        messageElement.appendChild(directionElement);
        messageElement.appendChild(textElement);
        root.appendChild(messageElement);

        writeXmlDocument(getMessageListFile(contact), messageListDocument);
    }

    public static boolean containsContact(List<Contact> contactList, String contactName) {
        for (int i = 0; i < contactList.size(); ++i) {
            Contact currentContact = contactList.get(i);
            if (currentContact.getName().equals(contactName)) {
                return true;
            }
        }
        return false;
    }

    protected String getAppFolderName() {
        return mContext.getFilesDir().getAbsolutePath() + "/" + Storage.APP_FOLDER;
    }

    protected String getContactFilename() {
        return getAppFolderName() + "/" + CONTACT_FILENAME;
    }

    private File getContactListFile() {
        return new File(getContactFilename());
    }

    private File getMessageListFile(Contact contact) {
        return new File(getAppFolderName() + "/" + contact.getMessageListFilename());
    }

    protected Document getMessageListDocument(Contact contact) throws IOException, SAXException {
        return mDocumentBuilder.parse(getMessageListFile(contact));
    }

    protected Document getContactListDocument() throws IOException, SAXException {
        return mDocumentBuilder.parse(getContactListFile());
    }

    private void writeXmlDocument(File messageListFile, Document xmlDocument)
            throws TransformerException, IOException {
        String outputString = XmlParser.getStringFromXmlDocument(xmlDocument);

        InternalStorage.writeToFile(messageListFile, outputString);
    }
}
