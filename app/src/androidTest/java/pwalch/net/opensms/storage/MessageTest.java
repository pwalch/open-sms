package pwalch.net.opensms.storage;

import android.content.Context;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 05.08.14.
 */
public class MessageTest extends StorageTest {

    public void testMessageParsing() {
        try {
            final NodeList messageAttributes =
                getDocumentRootFromString(MESSAGE_FIRST_XML).getChildNodes();
            final Message message = Storage.findMessage(messageAttributes);

            assertTrue(message.getDate() == 1000
                        && message.getDirection() == Direction.ME_TO_YOU);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyMessageList(List<Message> messageList) {
        final Message firstMessage = messageList.get(0);
        final Message secondMessage = messageList.get(1);
        assertTrue(firstMessage.getDate() == 1000
                && secondMessage.getDate() == 2000);
    }

    public void testMessageListParsing() {
        try {
            final Node root = getDocumentRootFromString(MESSAGE_LIST_1_XML);
            final NodeList nodeList = root.getChildNodes();

            verifyMessageList(Storage.findMessageList(nodeList));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testReadMessageListFromStorage() {
        try {
            final Context context = getInstrumentation().getContext();
            final Storage storage = new Storage(getActivity().getApplicationContext());

            // Write contacts in "contact.xml". This contact points to "messages_1.xml".
            Storage.writeToFile(new File(storage.getContactFilename()), CONTACT_LIST_XML);

            // Write "messages_XXX.xml" files
            Storage.writeToFile(new File(storage.getAppFolder() + "/" + MESSAGE_LIST_1_FILENAME),
                                MESSAGE_LIST_1_XML);
            Storage.writeToFile(new File(storage.getAppFolder() + "/" + MESSAGE_LIST_2_FILENAME),
                                MESSAGE_LIST_2_XML);

            final List<Contact> contactList = storage.retrieveContactList();
            Contact pierre = contactList.get(0);
            final List<Message> messageList = storage.retrieveMessageList(pierre);

            verifyMessageList(messageList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
