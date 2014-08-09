package pwalch.net.opensms;

import android.content.Context;
import android.test.ActivityTestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
            writeToFile(context, storage.getAppFolder(), storage.getContactFilename(),
                        CONTACT_LIST_XML);

            // Write "messages_XXX.xml" files
            writeToFile(context, storage.getAppFolder(), MESSAGE_LIST_1_FILENAME,
                        MESSAGE_LIST_1_XML);
            writeToFile(context, storage.getAppFolder(), MESSAGE_LIST_2_FILENAME,
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
