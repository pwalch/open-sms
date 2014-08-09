package pwalch.net.opensms.storage;

import android.content.Context;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import pwalch.net.opensms.structures.Contact;

/**
 * Created by pierre on 06.08.14.
 */
public class ContactTest extends StorageTest {

    public void testContactParsing() {
        try {
            final NodeList contactAttributes =
                getDocumentRootFromString(CONTACT_FIELD_PIERRE).getChildNodes();
            final Contact contact = Storage.findContact(contactAttributes);
            assertTrue(contact.getName().equals("Pierre")
                    && contact.getNumber().equals("+33 6 95 95 95"));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testContactListParsing() {
        try {
            final Node root = getDocumentRootFromString(CONTACT_LIST_XML);
            final NodeList nodeList = root.getChildNodes();

            verifyContactList(Storage.findContactList(nodeList));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyContactList(List<Contact> contactList) {
        final Contact pierre = contactList.get(0);
        final Contact jon = contactList.get(1);
        assertTrue(pierre.getName().equals("Pierre")
                && jon.getName().equals("Jon"));
    }

    public void testReadContactListFromStorage() {
        try {
            final Context context = getInstrumentation().getContext();

            final Storage storage = new Storage(getActivity().getApplicationContext());
            Storage.writeToFile(context, storage.getAppFolder(), storage.getContactFilename(),
                        CONTACT_LIST_XML);

            final List<Contact> contactList = storage.retrieveContactList();
            verifyContactList(contactList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
