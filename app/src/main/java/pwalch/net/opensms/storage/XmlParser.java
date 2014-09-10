package pwalch.net.opensms.storage;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import pwalch.net.opensms.structures.Contact;
import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.Message;

/**
 * Created by pierre on 10.09.14.
 */
public class XmlParser {
    public static List<Contact> findContactList(NodeList contactNodeList) {
        List<Contact> contactList = new ArrayList<Contact>();
        for (int i = 0; i < contactNodeList.getLength(); ++i) {
            Node contactNode = contactNodeList.item(i);
            assert contactNode.hasChildNodes();

            contactList.add(findContact(contactNode.getChildNodes()));
        }
        return contactList;
    }

    public static Contact findContact(NodeList contactAttributeList) {
        String contactName = "";
        String contactPhoneNumber = "";
        String contactConversationFile = "";
        for (int j = 0; j < contactAttributeList.getLength(); ++j) {
            Node attributeNode = contactAttributeList.item(j);
            String attributeName = attributeNode.getNodeName();
            String attributeValue = attributeNode.getTextContent();

            if (attributeName.equals("name")) {
                contactName = attributeValue;
            } else if (attributeName.equals("phoneNumber")) {
                contactPhoneNumber = attributeValue;
            } else if (attributeName.equals("conversationFilename")) {
                contactConversationFile = attributeValue;
            } else {
                assert false;
            }
        }

        return new Contact(contactName, contactPhoneNumber, contactConversationFile);
    }

    public static Message findMessage(NodeList messageAttributeList) {
        int date = 0;
        Direction direction = Direction.ME_TO_YOU;
        String text = "";
        for (int j = 0; j < messageAttributeList.getLength(); ++j) {
            Node attributeNode = messageAttributeList.item(j);
            String attributeName = attributeNode.getNodeName();
            String attributeValue = attributeNode.getTextContent();

            if (attributeName.equals("date")) {
                date = Integer.parseInt(attributeValue);
            } else if (attributeName.equals("direction")) {
                if (attributeValue.equals("me_to_you")) {
                    direction = Direction.ME_TO_YOU;
                } else if (attributeValue.equals("you_to_me")) {
                    direction = Direction.YOU_TO_ME;
                } else {
                    assert false;
                }
            } else if (attributeName.equals("text")) {
                text = attributeValue;
            } else {
                assert false;
            }
        }

        return new Message(date, direction, text);
    }

    public static List<Message> findMessageList(NodeList messageNodeList) {
        List<Message> messageList = new ArrayList<Message>();
        for (int i = 0; i < messageNodeList.getLength(); ++i) {
            Node messageNode = messageNodeList.item(i);
            assert messageNode.hasChildNodes();
            messageList.add(findMessage(messageNode.getChildNodes()));
        }
        return messageList;
    }

    public static String getStringFromXmlDocument(Document xmlDocument) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(xmlDocument);
        StreamResult result =  new StreamResult(new StringWriter());
        transformer.transform(source, result);
        String outputString = result.getWriter().toString();

        return outputString;
    }
}
