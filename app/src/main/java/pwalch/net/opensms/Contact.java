package pwalch.net.opensms;

/**
 * Created by pierre on 02/08/14.
 */
public class Contact {
    private String mName;
    private int mNumber;

    public Contact(String name, int number) {
        mName = name;
        mNumber = number;
    }

    public String getName() {
        return mName;
    }

    public int getNumber() {
        return mNumber;
    }
}
