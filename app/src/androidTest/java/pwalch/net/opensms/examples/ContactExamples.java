package pwalch.net.opensms.examples;

/**
 * Created by pierre on 11.09.14.
 */
public class ContactExamples {
    public static final String CONTACT_PIERRE_NAME = "Pierre";
    public static final String CONTACT_PIERRE_PHONE_NUMBER = "+33695959595";
    public static final String CONTACT_PIERRE_MESSAGE_LIST_FILENAME = "messages_0.xml";

    public static final String CONTACT_JON_NAME = "Jon";
    public static final String CONTACT_JON_PHONE_NUMBER = "+41791234567";
    public static final String CONTACT_JON_MESSAGE_LIST_FILENAME = "messages_1.xml";

    public static final String CONTACT_ENTRY_PIERRE = "<contact>"
            + "<name>" + CONTACT_PIERRE_NAME + "</name>"
            + "<phoneNumber>" + CONTACT_PIERRE_PHONE_NUMBER + "</phoneNumber>"
            + "<conversationFilename>" + CONTACT_PIERRE_MESSAGE_LIST_FILENAME + "</conversationFilename>"
            + "</contact>";

    public static final String CONTACT_ENTRY_JON = "<contact>"
            + "<name>" + CONTACT_JON_NAME + "</name>"
            + "<phoneNumber>" + CONTACT_JON_PHONE_NUMBER + "</phoneNumber>"
            + "<conversationFilename>" + CONTACT_JON_MESSAGE_LIST_FILENAME + "</conversationFilename>"
            + "</contact>";
}
