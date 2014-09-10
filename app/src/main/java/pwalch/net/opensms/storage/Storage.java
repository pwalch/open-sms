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
        Document document = mInternalStorage.readContactListFile();
        Node root = document.getDocumentElement();
        assert root.getNodeName().equals("contacts");

        return XmlParser.findContactList(root.getChildNodes());
    }

    public void addContact(String contactName, String contactNumber)
            throws IOException, SAXException, TransformerException {
        Log.i("tag", "Adding contact to storage");

        if (containsContact(retrieveContactList(), contactName)) {
            throw new IllegalArgumentException("Contact already exists");
        }

        Contact contact = new Contact(
            contactName,
            contactNumber,
            mInternalStorage.generateMessageListFile().getName());
        writeContact(contact);
    }

    public List<Message> retrieveMessageList(Contact contact)
            throws IOException, SAXException {
        Log.i("tag", "Retrieving message list at : " + contact.getMessageListFilename());

        Document messageListDocument = mInternalStorage.readMessageListFile(contact);
        Node root = messageListDocument.getDocumentElement();
        assert root.getNodeName().equals("message");

        return XmlParser.findMessageList(root.getChildNodes());
    }

    public void addMessage(Contact contact, Message message)
            throws IOException, SAXException, TransformerException {
        Document messageListDocument = mInternalStorage.readMessageListFile(contact);
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

        mInternalStorage.writeMessageListFile(contact, messageListDocument);
    }

    private void writeContact(Contact contact)
            throws IOException, SAXException, TransformerException {
        Document contactListDocument = mInternalStorage.readContactListFile();
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

        mInternalStorage.writeContactListFile(contactListDocument);
    }

    private static boolean containsContact(List<Contact> contactList, String contactName) {
        for (int i = 0; i < contactList.size(); ++i) {
            Contact currentContact = contactList.get(i);
            if (currentContact.getName().equals(contactName)) {
                return true;
            }
        }
        return false;
    }
}
