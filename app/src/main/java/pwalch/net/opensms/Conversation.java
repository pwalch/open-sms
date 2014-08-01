package pwalch.net.opensms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pierre on 31.07.14.
 */
public class Conversation {

    private int mPhoneNumber;
    private List<Message> mMessageList;

    public Conversation(int phoneNumber) {
        mPhoneNumber = phoneNumber;
        mMessageList = new ArrayList<Message>();
    }

    public void addMessage(Message message) {
        mMessageList.add(message);
    }

}
