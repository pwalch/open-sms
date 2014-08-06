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
public class MessageListTest extends StorageTest {

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
        NodeList contactNodeList = getDocumentRootFromString(ContactTest.CONTACT_FIELD_PIERRE).getChildNodes();
        Contact contact = Storage.findContact(contactNodeList);

        Context context = getActivity().getApplicationContext();
        File conversationFile = new File(context.getFilesDir(),
                contact.getConversationFilename());

        Storage.writeStringToFile(MESSAGE_LIST_XML, conversationFile);
    }

    public void testMessageField() {
        try {
            NodeList messageAttributes = getDocumentRootFromString(MESSAGE_FIRST_XML).getChildNodes();
            Message message = Storage.findMessage(messageAttributes);

            assertTrue(message.getDate() == 1000
                        && message.getDirection() == Direction.ME_TO_YOU);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
