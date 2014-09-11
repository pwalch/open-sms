package pwalch.net.opensms.structures;

import junit.framework.Assert;

/**
 * Created by pierre on 11.09.14.
 */
public class DirectionConverter {
    private static String MESSAGE_ME_TO_YOU = "me_to_you";
    private static String MESSAGE_YOU_TO_ME = "you_to_me";

    public static String directionToText(Direction direction) {
        String directionString = MESSAGE_ME_TO_YOU;
        switch (direction) {
            case ME_TO_YOU: {
                directionString = MESSAGE_ME_TO_YOU;
                break;
            }

            case YOU_TO_ME: {
                directionString = MESSAGE_YOU_TO_ME;
                break;
            }

            default: {
                directionString = MESSAGE_ME_TO_YOU;
            }
        }

        return directionString;
    }

    public static Direction textToDirection(String text) {
        Direction direction = Direction.ME_TO_YOU;

        if (text.equals(MESSAGE_ME_TO_YOU)) {
            direction = Direction.ME_TO_YOU;
        } else if (text.equals(MESSAGE_YOU_TO_ME)) {
            direction = Direction.YOU_TO_ME;
        } else {
            Assert.assertTrue(false);
        }

        return direction;
    }
}
