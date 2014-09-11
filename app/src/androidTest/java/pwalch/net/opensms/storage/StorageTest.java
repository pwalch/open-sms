package pwalch.net.opensms.storage;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pwalch.net.opensms.examples.ContactExamples;
import pwalch.net.opensms.examples.MessageExamples;
import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 11.09.14.
 */
public class StorageTest extends BaseTest {

    private Storage mStorage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        new InternalStorage(findContext()).resetAppFolder();

        mStorage = new Storage(this.findContext());
    }

    private void writeContacts()
            throws TransformerException, SAXException, IOException {
        mStorage.addContact(ContactExamples.CONTACT_PIERRE_NAME,
                            ContactExamples.CONTACT_PIERRE_PHONE_NUMBER);
        mStorage.addContact(ContactExamples.CONTACT_JON_NAME,
                            ContactExamples.CONTACT_JON_PHONE_NUMBER);
    }

    public void testReadWriteContact()
            throws ParserConfigurationException, IOException, SAXException, TransformerException {
        writeContacts();

        List<Contact> contactList = mStorage.retrieveContactList();
        assertTrue(contactList.get(0).getName().equals(ContactExamples.CONTACT_PIERRE_NAME)
                && contactList.get(1).getName().equals(ContactExamples.CONTACT_JON_NAME));
    }

    public void testReadWriteMessage()
            throws IOException, TransformerException, SAXException {
        writeContacts();

        List<Contact> contactList = mStorage.retrieveContactList();
        Contact jon = contactList.get(1);

        Message addedMessage = new Message(MessageExamples.MESSAGE_FIRST_DATE,
                                           MessageExamples.MESSAGE_FIRST_DIRECTION,
                                           MessageExamples.MESSAGE_FIRST_TEXT);
        mStorage.addMessage(jon, addedMessage);

        List<Message> messageList = mStorage.retrieveMessageList(jon);
        Message lastMessage = messageList.get(messageList.size() - 1);

        assertTrue(addedMessage.getDate() == lastMessage.getDate()
                    && addedMessage.getDirection() == lastMessage.getDirection());
    }
}
