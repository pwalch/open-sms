package pwalch.net.opensms.examples;

import pwalch.net.opensms.structures.Direction;
import pwalch.net.opensms.structures.DirectionConverter;

/**
 * Created by pierre on 11.09.14.
 */
public class MessageExamples {

    public static final int MESSAGE_FIRST_DATE = 1000;
    public static final Direction MESSAGE_FIRST_DIRECTION = Direction.ME_TO_YOU;
    public static final String MESSAGE_FIRST_TEXT = "Vous voulez un whisky ?";

    public static final String MESSAGE_FIRST_XML =
            "<message>"
                    + "<date>" + MESSAGE_FIRST_DATE + "</date>"
                    + "<direction>" + DirectionConverter.directionToText(MESSAGE_FIRST_DIRECTION) + "</direction>"
                    + "<text>" + MESSAGE_FIRST_TEXT + "</text>"
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
}
