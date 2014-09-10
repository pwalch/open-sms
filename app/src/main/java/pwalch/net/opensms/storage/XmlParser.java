package pwalch.net.opensms.storage;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    private static String CONTACT_TAG = "contact";
    private static String CONTACT_NAME_ATTRIBUTE = "name";
    private static String CONTACT_PHONE_NUMBER_ATTRIBUTE = "phoneNumber";
    private static String CONTACT_MESSAGE_LIST_FILENAME = "conversationFilename";

    private static String MESSAGE_TAG = "message";
    private static String MESSAGE_DATE_ATTRIBUTE = "date";
    private static String MESSAGE_DIRECTION_ATTRIBUTE = "direction";
    private static String MESSAGE_ME_TO_YOU = "me_to_you";
    private static String MESSAGE_YOU_TO_ME = "you_to_me";
    private static String MESSAGE_TEXT = "text";

    public static List<Contact> findContactList(Node contactNodeListRoot) {
        NodeList contactNodeList = contactNodeListRoot.getChildNodes();

        List<Contact> contactList = new ArrayList<Contact>();
        for (int i = 0; i < contactNodeList.getLength(); ++i) {
            Node contactNode = contactNodeList.item(i);
            Assert.assertTrue(contactNode.getNodeName().equals(CONTACT_TAG)
                && contactNode.hasChildNodes());

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

            if (attributeName.equals(CONTACT_NAME_ATTRIBUTE)) {
                contactName = attributeValue;
            } else if (attributeName.equals(CONTACT_PHONE_NUMBER_ATTRIBUTE)) {
                contactPhoneNumber = attributeValue;
            } else if (attributeName.equals(CONTACT_MESSAGE_LIST_FILENAME)) {
                contactConversationFile = attributeValue;
            } else {
                Assert.assertTrue(false);
            }
        }

        return new Contact(contactName, contactPhoneNumber, contactConversationFile);
    }

    public static Element generateContactElement(Document contactListDocument, Contact contact) {
        Element nameElement = contactListDocument.createElement(CONTACT_NAME_ATTRIBUTE);
        nameElement.setTextContent(contact.getName());

        Element numberElement = contactListDocument.createElement(CONTACT_PHONE_NUMBER_ATTRIBUTE);
        numberElement.setTextContent(contact.getNumber());

        Element conversationElement = contactListDocument.createElement(CONTACT_MESSAGE_LIST_FILENAME);
        conversationElement.setTextContent(contact.getMessageListFilename());

        Element contactElement = contactListDocument.createElement(CONTACT_TAG);
        contactElement.appendChild(nameElement);
        contactElement.appendChild(numberElement);
        contactElement.appendChild(conversationElement);

        return contactElement;
    }

    public static Message findMessage(NodeList messageAttributeList) {
        int date = 0;
        Direction direction = Direction.ME_TO_YOU;
        String text = "";
        for (int j = 0; j < messageAttributeList.getLength(); ++j) {
            Node attributeNode = messageAttributeList.item(j);
            String attributeName = attributeNode.getNodeName();
            String attributeValue = attributeNode.getTextContent();

            if (attributeName.equals(MESSAGE_DATE_ATTRIBUTE)) {
                date = Integer.parseInt(attributeValue);
            } else if (attributeName.equals(MESSAGE_DIRECTION_ATTRIBUTE)) {
                if (attributeValue.equals(MESSAGE_ME_TO_YOU)) {
                    direction = Direction.ME_TO_YOU;
                } else if (attributeValue.equals(MESSAGE_YOU_TO_ME)) {
                    direction = Direction.YOU_TO_ME;
                } else {
                    Assert.assertTrue(false);
                }
            } else if (attributeName.equals(MESSAGE_TEXT)) {
                text = attributeValue;
            } else {
                Assert.assertTrue(false);
            }
        }

        return new Message(date, direction, text);
    }

    public static Element generateMessageElement(Document messageListDocument, Message message) {
        Element dateElement = messageListDocument.createElement(MESSAGE_DATE_ATTRIBUTE);
        dateElement.setTextContent(Integer.toString(message.getDate()));

        Element directionElement = messageListDocument.createElement(MESSAGE_DIRECTION_ATTRIBUTE);
        String directionString = "";
        switch (message.getDirection()) {
            case ME_TO_YOU: {
                directionString = MESSAGE_ME_TO_YOU;
                break;
            }

            case YOU_TO_ME: {
                directionString = MESSAGE_YOU_TO_ME;
            }

            default: {
                Assert.assertTrue(false);
            }
        }
        directionElement.setTextContent(directionString);

        Element textElement = messageListDocument.createElement(MESSAGE_TEXT);
        textElement.setTextContent(message.getText());

        Element messageElement = messageListDocument.createElement(MESSAGE_TAG);
        messageElement.appendChild(dateElement);
        messageElement.appendChild(directionElement);
        messageElement.appendChild(textElement);

        return messageElement;
    }

    public static List<Message> findMessageList(Node messageNodeListRoot) {
        NodeList messageNodeList = messageNodeListRoot.getChildNodes();
        List<Message> messageList = new ArrayList<Message>();
        for (int i = 0; i < messageNodeList.getLength(); ++i) {
            Node messageNode = messageNodeList.item(i);
            Assert.assertTrue(messageNode.getNodeName().equals(MESSAGE_TAG) && messageNode.hasChildNodes());

            messageList.add(findMessage(messageNode.getChildNodes()));
        }
        return messageList;
    }

    public static String getStringFromXmlDocument(Document xmlDocument)
            throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(xmlDocument);
        StreamResult result =  new StreamResult(new StringWriter());
        transformer.transform(source, result);
        String outputString = result.getWriter().toString();

        return outputString;
    }
}
