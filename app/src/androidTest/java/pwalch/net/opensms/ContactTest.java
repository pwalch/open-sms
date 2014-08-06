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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by pierre on 06.08.14.
 */
public class ContactTest extends StorageTest {

    public static final String CONTACT_FIELD_PIERRE = "<contact>"
            + "<name>Pierre</name>"
            + "<phoneNumber>+33 6 95 95 95</phoneNumber>"
            + "<conversationFilename>3521-number.xml</conversationFilename>"
            + "</contact>";

    public static final String CONTACT_FIELD_JON = "<contact>"
            + "<name>Jon</name>"
            + "<phoneNumber>+41 6 85 85 85</phoneNumber>"
            + "<conversationFilename>3525-number.xml</conversationFilename>"
            + "</contact>";

    public static final String CONTACT_LIST_XML =
            "<contactList>"
                    + CONTACT_FIELD_PIERRE
                    + CONTACT_FIELD_JON
                    + "</contactList>";

    public void testContactField() {
        try {
            NodeList contactAttributes = getDocumentRootFromString(CONTACT_FIELD_PIERRE).getChildNodes();
            Contact contact = Storage.findContact(contactAttributes);
            assertTrue(contact.getName().equals("Pierre")
                    && contact.getNumber().equals("+33 6 95 95 95"));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testContactList() {
        try {
            Node root = getDocumentRootFromString(CONTACT_LIST_XML);
            NodeList nodeList = root.getChildNodes();

            verifyContactList(Storage.findContactList(nodeList));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyContactList(List<Contact> contactList) {
        Contact pierre = contactList.get(0);
        Contact jon = contactList.get(1);
        assertTrue(pierre.getName().equals("Pierre")
                && jon.getName().equals("Jon"));
    }

    private void writeExampleContactFile() throws IOException {
        Context context = getActivity().getApplicationContext();
        File contactFile = new File(context.getFilesDir(), Storage.CONTACT_FILENAME);

        if (!contactFile.exists()) {
            contactFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(contactFile));
        writer.write(Storage.CONTACT_FILENAME);
        writer.close();
    }

    public void testReadContactListFromStorage() {
        try {
            writeExampleContactFile();

            Storage storage = new Storage(getActivity().getApplicationContext());
            List<Contact> contactList = storage.retrieveContactList();
            verifyContactList(contactList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
