package pwalch.net.opensms.storage;

import android.content.Context;
import android.util.Log;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pwalch.net.opensms.structures.Contact;

/**
 * Created by pierre on 09/08/14.
 */
public class InternalStorage {

    private static final String APP_FOLDER = "open_sms_myfiles";
    private static final String CONTACT_FILENAME = "contact.xml";

    private Context mContext;

    private static final String UID_XML_NAME = "currentUid";
    private int mCurrentId = 0;

    private DocumentBuilderFactory mFactory;
    private DocumentBuilder mBuilder;

    public InternalStorage(Context context)
            throws ParserConfigurationException, TransformerException, IOException, SAXException {
        mContext = context;

        mFactory = DocumentBuilderFactory.newInstance();
        mBuilder = mFactory.newDocumentBuilder();

        initializeAppFolder();
    }

    public Document readContactListFile()
            throws IOException, SAXException {
        return parse(getContactListFile());
    }

    public Document readMessageListFile(Contact contact)
            throws IOException, SAXException {
        return parse(getMessageListFile(contact));
    }

    public void writeContactListFile(Document xmlContactList)
            throws TransformerException, IOException {
        Log.i("tag", "Writing contact list file");
        writeXmlDocument(getContactListFile(), xmlContactList);
    }

    public void writeMessageListFile(Contact contact, Document xmlMessageList)
            throws TransformerException, IOException {
        Log.i("tag", "Writing a message list file");
        writeXmlDocument(getMessageListFile(contact), xmlMessageList);
    }

    public File generateMessageListFile()
            throws SAXException, TransformerException, IOException {
        Log.i("tag", "Generating a message list file");

        String uniqueFilename = "conversation_" + mCurrentId + ".xml";
        setCurrentUid(mCurrentId + 1);

        File messageListFile = new File(getAppFolderName() + "/" + uniqueFilename);
        messageListFile.createNewFile();
        writeToFile(messageListFile, "<?xml version=\"1.0\" ?>\n<messageList></messageList>");

        return messageListFile;
    }

    private void initializeAppFolder()
            throws IOException, TransformerException, SAXException {
        Log.i("tag", "Initializing app folder");
        if (!getContactListFile().exists()) {
            resetAppFolder();
        } else {
            mCurrentId = retrieveCurrentUid();
        }
    }

    protected void resetAppFolder()
            throws IOException, TransformerException, SAXException {
        File contactListFile = getContactListFile();

        File parentFile = contactListFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }

        File childArray[] = parentFile.listFiles();
        for (File childFile : childArray) {
            childFile.delete();
        }

        createContactListFile(contactListFile);
    }

    private void createContactListFile(File contactListFile)
            throws TransformerException, SAXException, IOException {
        Log.i("tag", "Creating contact list file");
        Assert.assertTrue(!contactListFile.exists());

        contactListFile.createNewFile();
        writeToFile(contactListFile,
                "<?xml version=\"1.0\" ?>\n<contactList></contactList>");
        setCurrentUid(0);
    }

    private int retrieveCurrentUid()
            throws IOException, SAXException {
        Document contactDocument = readContactListFile();
        return Integer.parseInt(
            contactDocument.getDocumentElement().getAttribute(
                UID_XML_NAME));
    }

    private void setCurrentUid(int uid)
            throws IOException, SAXException, TransformerException {
        mCurrentId = uid;
        Document contactDocument = readContactListFile();
        contactDocument.getDocumentElement().setAttribute(UID_XML_NAME,
                Integer.toString(mCurrentId));
        writeContactListFile(contactDocument);
    }

    private String getAppFolderName() {
        return mContext.getFilesDir().getAbsolutePath() + "/" + APP_FOLDER;
    }

    private String getContactFilename() {
        return getAppFolderName() + "/" + CONTACT_FILENAME;
    }

    private File getContactListFile() {
        return new File(getContactFilename());
    }

    private File getMessageListFile(Contact contact) {
        return new File(getAppFolderName() + "/" + contact.getMessageListFilename());
    }

    private Document parse(File file)
            throws IOException, SAXException {
        return mBuilder.parse(file);
    }

    private void writeXmlDocument(File messageListFile, Document xmlDocument)
            throws TransformerException, IOException {
        String outputString = XmlParser.getStringFromXmlDocument(xmlDocument);

        writeToFile(messageListFile, outputString);
    }

    private void writeToFile(File file, String textToWrite)
            throws IOException {
        Assert.assertTrue(file != null && textToWrite != null
                && file.exists() && file.isFile());
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(textToWrite);
        writer.close();
    }
}
