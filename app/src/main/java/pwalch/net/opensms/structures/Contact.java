package pwalch.net.opensms.structures;

/**
 * Created by pierre on 02/08/14.
 */
public class Contact {
    private String mName;
    private String mPhoneNumber;
    private String mMessageListFilename;

    public Contact(String name, String phoneNumber, String conversationFilename) {
        mName = name;
        mPhoneNumber = phoneNumber;
        mMessageListFilename = conversationFilename;
    }

    public String getName() {
        return mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getMessageListFilename() {
        return mMessageListFilename;
    }
}
