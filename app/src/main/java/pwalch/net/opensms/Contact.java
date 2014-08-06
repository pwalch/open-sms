package pwalch.net.opensms;

/**
 * Created by pierre on 02/08/14.
 */
public class Contact {
    private String mName;
    private String mPhoneNumber;
    private String mConversationFilename;

    public Contact(String name, String number, String conversationFilename) {
        mName = name;
        mPhoneNumber = number;
        mConversationFilename = conversationFilename;
    }

    public String getName() {
        return mName;
    }

    public String getNumber() {
        return mPhoneNumber;
    }

    public String getConversationFilename() {
        return mConversationFilename;
    }
}
