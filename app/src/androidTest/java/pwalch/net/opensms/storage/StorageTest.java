package pwalch.net.opensms.storage;

import android.content.Context;
import android.test.ActivityTestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by pierre on 06.08.14.
 */
public class StorageTest extends ActivityTestCase {
    protected static DocumentBuilderFactory fDocumentBuilderFactory;
    protected static DocumentBuilder fDocumentBuilder;

    protected void setUp() throws Exception {
        super.setUp();

        fDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        fDocumentBuilder = fDocumentBuilderFactory.newDocumentBuilder();
    }

    protected Node getDocumentRootFromString(String xmlDocumentText)
            throws IOException, SAXException {
        final InputStream stream = new ByteArrayInputStream(
                xmlDocumentText.getBytes(Charset.defaultCharset()));
        final Document document = fDocumentBuilder.parse(stream);
        return document.getDocumentElement();
    }

    protected void writeExampleFiles(Storage storage) throws IOException {
        writeContactFile(storage.getContactFilename());
        writeMessageFiles(storage.getAppFolderName());
    }

    protected void writeContactFile(String contactFilename) throws IOException {
        // Write contacts in "contact.xml". This contact points to "messages_1.xml".
        InternalStorage.writeToFile(new File(contactFilename), Examples.CONTACT_LIST_XML);
    }

    protected void writeMessageFiles(String appFolderName) throws IOException {
        // Write "messages_XXX.xml" files
        InternalStorage.writeToFile(new File(appFolderName + "/" + Examples.MESSAGE_LIST_1_FILENAME),
                Examples.MESSAGE_LIST_1_XML);
        InternalStorage.writeToFile(new File(appFolderName + "/" + Examples.MESSAGE_LIST_2_FILENAME),
                Examples.MESSAGE_LIST_2_XML);
    }

    protected Node getDocumentRootFromFile(File xmlFile)
            throws IOException, SAXException {
        final InputStream stream = new FileInputStream(xmlFile);
        final Document document = fDocumentBuilder.parse(stream);
        return document.getDocumentElement();
    }

    protected Context findContext() {
        return this.getInstrumentation().getTargetContext().getApplicationContext();
    }
}
