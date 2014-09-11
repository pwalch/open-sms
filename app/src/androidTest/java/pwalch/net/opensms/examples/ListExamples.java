package pwalch.net.opensms.examples;

import pwalch.net.opensms.examples.ContactExamples;
import pwalch.net.opensms.examples.MessageExamples;

/**
 * Created by pierre on 09/08/14.
 */
public class ListExamples {
    public static final String MESSAGE_LIST_0_XML =
            "<?xml version=\"1.0\" ?>"
                    + "<messageList>"
                    + MessageExamples.MESSAGE_FIRST_XML
                    + MessageExamples.MESSAGE_SECOND_XML
                    + MessageExamples.MESSAGE_THIRD_XML
                    + "</messageList>";

    public static final String MESSAGE_LIST_1_XML =
            "<?xml version=\"1.0\" ?><conversation>"
                    + MessageExamples.MESSAGE_FOURTH_XML
                    + MessageExamples.MESSAGE_FIFTH_XML
                    + "</conversation>";

    public static final String CONTACT_LIST_XML =
            "<?xml version=\"1.0\" ?>\n"
                    + "<contactList currentUid=\"2\">"
                    + ContactExamples.CONTACT_ENTRY_PIERRE
                    + ContactExamples.CONTACT_ENTRY_JON
                    + "</contactList>";
}
