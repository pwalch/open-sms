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

    private static final String MESSAGE_FIRST_XML =
            "<message>"
            + "<date>1000</date>"
            + "<direction>me_to_you</direction>"
            + "<text>Je suis ton père</text>"
            + "</message>";

    private static final String MESSAGE_SECOND_XML =
            "<message>"
            + "<date>2000</date>"
            + "<from>me</from>"
            + "<text>Nooooon !</text>"
            + "</message>";

    private static final String MESSAGE_LIST_XML =
            "<messageList>"
            + MESSAGE_FIRST_XML
            + MESSAGE_SECOND_XML
            + "</messageList>";


    private void writeExampleMessageListFile() throws IOException, SAXException {
        final NodeList contactNodeList = getDocumentRootFromString(ContactTest.CONTACT_FIELD_PIERRE).getChildNodes();
        final Contact contact = Storage.findContact(contactNodeList);

        final Context context = getActivity().getApplicationContext();
        final File conversationFile = new File(context.getFilesDir(),
                contact.getConversationFilename());

        Storage.writeStringToFile(MESSAGE_LIST_XML, conversationFile);
    }

    public void testMessageParsing() {
        try {
            final NodeList messageAttributes = getDocumentRootFromString(MESSAGE_FIRST_XML).getChildNodes();
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
            final Node root = getDocumentRootFromString(MESSAGE_LIST_XML);
            final NodeList nodeList = root.getChildNodes();

            verifyMessageList(Storage.findMessageList(nodeList));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testReadMessageListFromStorage() {

    }

}
