package pwalch.net.opensms.storage;

import junit.framework.Assert;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pwalch.net.opensms.structures.Contact;

/**
 * Created by pierre on 06.08.14.
 */
public class ContactTest extends StorageTest {

    public void testContactParsing() throws IOException, SAXException {
        final NodeList contactAttributes =
            getDocumentRootFromString(Examples.CONTACT_ENTRY_PIERRE).getChildNodes();
        final Contact contact = XmlParser.findContact(contactAttributes);
        assertTrue(contact.getName().equals("Pierre")
                && contact.getNumber().equals("+33 6 95 95 95"));
    }

    public void testContactListParsing() throws IOException, SAXException {
        final Node root = getDocumentRootFromString(Examples.CONTACT_LIST_XML);
        final NodeList nodeList = root.getChildNodes();

        verifyContactList(XmlParser.findContactList(nodeList));
    }

    private void verifyContactList(List<Contact> contactList) {
        final Contact pierre = contactList.get(0);
        final Contact jon = contactList.get(1);
        assertTrue(pierre.getName().equals("Pierre")
                && jon.getName().equals("Jon"));
    }

    public void testReadContactListFromStorage() throws IOException, ParserConfigurationException, SAXException {
        final Storage storage = new Storage(this.findContext());
        writeContactFile(storage.getContactFilename());

        final List<Contact> contactList = storage.retrieveContactList();
        verifyContactList(contactList);
    }

    public void testCreateContact()
            throws IOException, ParserConfigurationException, SAXException, TransformerException {
        final Storage storage = new Storage(this.findContext());
        writeContactFile(storage.getContactFilename());

        String contactName = "Droid";
        String contactNumber = "+41 79 123 12 13";
        storage.addContact(contactName, contactNumber);

        List<Contact> contactList = storage.retrieveContactList();
        boolean isFound = false;
        for (int i = 0; i < contactList.size(); ++i) {
            Contact currentContact = contactList.get(i);
            if (currentContact.getName().equals(contactName)
                    && currentContact.getNumber().equals(contactNumber)) {
                isFound = true;
                break;
            }
        }

        Assert.assertTrue(isFound);
    }

}
