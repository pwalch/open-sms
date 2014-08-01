package pwalch.net.opensms;

enum Direction { ME_TO_YOU, YOU_TO_ME };

/**
 * Created by pierre on 31.07.14.
 */
public class Message {
    private String mText;
    private Direction mDirection;

    public Message(String text, Direction direction) {
        mText = text;
        mDirection = direction;
    }

    @Override
    public String toString() {
        return mText;
    }

    public Direction getDirection() {
        return mDirection;
    }
}
