package pwalch.net.opensms.storage;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pwalch.net.opensms.examples.ContactExamples;
import pwalch.net.opensms.structures.Contact;

/**
 * Created by pierre on 11.09.14.
 */
public class StorageTest extends BaseTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        new InternalStorage(findContext()).resetAppFolder();
    }

    public void testReadWriteContact()
            throws ParserConfigurationException, IOException, SAXException, TransformerException {
        final Storage storage = new Storage(this.findContext());

        storage.addContact(ContactExamples.CONTACT_PIERRE_NAME, ContactExamples.CONTACT_PIERRE_PHONE_NUMBER);
        storage.addContact(ContactExamples.CONTACT_JON_NAME, ContactExamples.CONTACT_JON_PHONE_NUMBER);

        List<Contact> contactList = storage.retrieveContactList();

        assertTrue(contactList.get(0).getName().equals(ContactExamples.CONTACT_PIERRE_NAME)
                && contactList.get(1).getName().equals(ContactExamples.CONTACT_JON_NAME));
    }
}
