package pwalch.net.opensms.storage;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pwalch.net.opensms.sms.MessageManager;
import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 04.08.14.
 */
public class Storage {

    private Context mContext;
    private InternalStorage mInternalStorage;

    public Storage(Context context)
            throws ParserConfigurationException, TransformerException, IOException, SAXException {
        mContext = context;
        mInternalStorage = new InternalStorage(context);
    }

    public List<Contact> retrieveContactList()
            throws IOException, SAXException, TransformerException {
        Log.i("tag", "Retrieving contact list from storage");
        Document document = mInternalStorage.readContactListFile();
        Node root = document.getDocumentElement();

        return XmlParser.findContactList(root);
    }

    public void addContact(String contactName, String contactPhoneNumber)
            throws IOException, SAXException, TransformerException {
        Log.i("tag", "Adding contact to storage");

        if (MessageManager.findContactWithPhoneNumber(retrieveContactList(), contactPhoneNumber)
                != null) {
            throw new IllegalArgumentException("Contact already exists");
        }

        Contact contact = new Contact(
            contactName,
            contactPhoneNumber,
            mInternalStorage.generateMessageListFile().getName());
        writeContact(contact);
    }

    public List<Message> retrieveMessageList(Contact contact)
            throws IOException, SAXException {
        Log.i("tag", "Retrieving message list at : " + contact.getMessageListFilename());

        Document messageListDocument = mInternalStorage.readMessageListFile(contact);
        Node root = messageListDocument.getDocumentElement();

        return XmlParser.findMessageList(root);
    }

    public void addMessage(Contact contact, Message message)
            throws IOException, SAXException, TransformerException {
        Log.i("tag", "Adding message to message list of a contact");
        Document messageListDocument = mInternalStorage.readMessageListFile(contact);
        Node root = messageListDocument.getDocumentElement();

        Element messageElement = XmlParser.generateMessageElement(messageListDocument, message);
        root.appendChild(messageElement);

        mInternalStorage.writeMessageListFile(contact, messageListDocument);
    }

    private void writeContact(Contact contact)
            throws IOException, SAXException, TransformerException {
        Document contactListDocument = mInternalStorage.readContactListFile();
        Node root = contactListDocument.getDocumentElement();

        Element contactElement = XmlParser.generateContactElement(contactListDocument, contact);
        root.appendChild(contactElement);

        mInternalStorage.writeContactListFile(contactListDocument);
    }
}
