package pwalch.net.opensms.storage;

/**
 * Created by pierre on 09/08/14.
 */
public class Examples {
    public static final String MESSAGE_FIRST_XML =
            "<message>"
                    + "<date>1000</date>"
                    + "<direction>me_to_you</direction>"
                    + "<text>Vous voulez un whisky ?</text>"
                    + "</message>";

    public static final String MESSAGE_SECOND_XML =
            "<message>"
                    + "<date>2000</date>"
                    + "<direction>you_to_me</direction>"
                    + "<text>Oui juste un doigt.</text>"
                    + "</message>";

    public static final String MESSAGE_THIRD_XML =
            "<message>"
                    + "<date>3000</date>"
                    + "<direction>me_to_you</direction>"
                    + "<text>Vous voulez pas un whisky d'abord ?</text>"
                    + "</message>";

    public static final String MESSAGE_FOURTH_XML =
            "<message>"
                    + "<date>1000</date>"
                    + "<direction>me_to_you</direction>"
                    + "<text>Luke, je suis ton père.</text>"
                    + "</message>";

    public static final String MESSAGE_FIFTH_XML =
            "<message>"
                    + "<date>2000</date>"
                    + "<direction>you_to_me</direction>"
                    + "<text>NOOOOOOOOOOON !</text>"
                    + "</message>";

    public static final String MESSAGE_LIST_1_FILENAME = "messages_1.xml";

    public static final String MESSAGE_LIST_1_XML =
            "<?xml version=\"1.0\" ?><messageList>"
                    + MESSAGE_FIRST_XML
                    + MESSAGE_SECOND_XML
                    + MESSAGE_THIRD_XML
                    + "</messageList>";

    public static final String MESSAGE_LIST_2_FILENAME = "messages_2.xml";

    public static final String MESSAGE_LIST_2_XML =
            "<?xml version=\"1.0\" ?><conversation>"
                    + MESSAGE_FOURTH_XML
                    + MESSAGE_FIFTH_XML
                    + "</conversation>";

    public static final String CONTACT_ENTRY_PIERRE = "<contact>"
            + "<name>Pierre</name>"
            + "<phoneNumber>+33 6 95 95 95</phoneNumber>"
            + "<conversationFilename>messages_1.xml</conversationFilename>"
            + "</contact>";

    public static final String CONTACT_ENTRY_JON = "<contact>"
            + "<name>Jon</name>"
            + "<phoneNumber>+41 6 85 85 85</phoneNumber>"
            + "<conversationFilename>messages_2.xml</conversationFilename>"
            + "</contact>";

    public static final String CONTACT_LIST_XML =
            "<?xml version=\"1.0\" ?><contactList>"
                    + CONTACT_ENTRY_PIERRE
                    + CONTACT_ENTRY_JON
                    + "</contactList>";
}
