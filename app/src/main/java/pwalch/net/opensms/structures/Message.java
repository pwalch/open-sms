package pwalch.net.opensms.structures;

/**
 * Created by pierre on 31.07.14.
 */
public class Message {
    private int mDate;
    private Direction mDirection;
    private String mText;

    public Message(int date, Direction direction, String text) {
        mDate = date;
        mText = text;
        mDirection = direction;
    }

    public String getText() {
        return mText;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public int getDate() {
        return mDate;
    }

    public String toString() {
        return getDate() + " : " + getText();
    }
}
