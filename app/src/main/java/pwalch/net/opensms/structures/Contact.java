package pwalch.net.opensms.structures;

/**
 * Created by pierre on 02/08/14.
 */
public class Contact {
    private String mName;
    private String mPhoneNumber;
    private String mMessageListFilename;

    public Contact(String name, String number, String conversationFilename) {
        mName = name;
        mPhoneNumber = number;
        mMessageListFilename = conversationFilename;
    }

    public String getName() {
        return mName;
    }

    public String getNumber() {
        return mPhoneNumber;
    }

    public String getMessageListFilename() {
        return mMessageListFilename;
    }
}
