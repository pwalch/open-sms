package pwalch.net.opensms;

import android.content.Context;

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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by pierre on 04.08.14.
 */
public class Storage {

    private static final String STORAGE_FILE_FILENAME = "sms.xml";
    private FileOutputStream mOutputStream;
    private FileInputStream mInputStream;

    private File mFile;

    private DocumentBuilderFactory mDocumentBuilderFactory;
    private DocumentBuilder mDocumentBuilder;
    private Document mContactDocument;

    public Storage(Context context) throws IOException, ParserConfigurationException, SAXException {
        mFile = new File(context.getFilesDir(), STORAGE_FILE_FILENAME);

        // Create file if it does not exist
        if (mFile.exists()) {
            mFile.createNewFile();
        }

        mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
        mContactDocument = mDocumentBuilder.parse(mFile);
    }

    private OutputStream createWriteStream(Context context) throws FileNotFoundException {
        return context.openFileOutput(STORAGE_FILE_FILENAME, Context.MODE_PRIVATE);
    }

    private InputStream createReadStream(Context context) throws FileNotFoundException {
        return context.openFileInput(STORAGE_FILE_FILENAME);
    }

    public void addMessage(Context context, Conversation conversation, Message message) {

    }

    private Element getContactRootElement() {
        return mContactDocument.getDocumentElement();
    }

    List<Contact> retrieveContactList(Context context) {
        List<Contact> contactList = new ArrayList<Contact>();

        Element root = getContactRootElement();
        assert root.getTagName().equals("contacts");

        NodeList contactNodeList = root.getChildNodes();
        for (int i = 0; i < contactNodeList.getLength(); ++i) {
            Node contactNode = contactNodeList.item(i);
            assert contactNode.hasChildNodes();

            contactList.add(findContact(contactNode.getChildNodes()));
        }
        return contactList;
    }

    public static Contact findContact(NodeList contactAttributeList) {
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
        Contact contact = new Contact(contactName, contactPhoneNumber, contactConversationFile);

        return contact;
    }
}
