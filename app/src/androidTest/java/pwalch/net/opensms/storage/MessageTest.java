package pwalch.net.opensms.storage;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 05.08.14.
 */
public class MessageTest extends StorageTest {

    public void testMessageParsing() throws IOException, SAXException {
        final NodeList messageAttributes =
            getDocumentRootFromString(Examples.MESSAGE_FIRST_XML).getChildNodes();
        final Message message = Storage.findMessage(messageAttributes);

        assertTrue(message.getDate() == 1000
                    && message.getDirection() == Direction.ME_TO_YOU);
    }

    private void verifyMessageList(List<Message> messageList) {
        final Message firstMessage = messageList.get(0);
        final Message secondMessage = messageList.get(1);
        assertTrue(firstMessage.getDate() == 1000
                && secondMessage.getDate() == 2000);
    }

    public void testMessageListParsing() throws IOException, SAXException {
        final Node root = getDocumentRootFromString(Examples.MESSAGE_LIST_1_XML);
        final NodeList nodeList = root.getChildNodes();

        verifyMessageList(Storage.findMessageList(nodeList));
    }

    public void testReadMessageListFromStorage() throws IOException, ParserConfigurationException, SAXException {
        final Storage storage = new Storage(this.findContext());

        writeContactFile(storage.getContactFilename());
        writeMessageFiles(storage.getAppFolderName());

        final List<Contact> contactList = storage.retrieveContactList();
        Contact pierre = contactList.get(0);
        final List<Message> messageList = storage.retrieveMessageList(pierre);

        verifyMessageList(messageList);
    }

    public void testWriteMessage() throws IOException, SAXException {
        final Node root = getDocumentRootFromString(Examples.MESSAGE_LIST_1_XML);
    }

}
