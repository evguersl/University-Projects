package ch.epfl.tchu.net;

import java.util.List;
/**
 * Enum type representing the different method Player defined.
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public enum MessageId
{
    INIT_PLAYERS,
    RECEIVE_INFO,
    UPDATE_STATE,
    SET_INITIAL_TICKETS,
    CHOOSE_INITIAL_TICKETS,
    NEXT_TURN,
    CHOOSE_TICKETS,
    DRAW_SLOT,
    ROUTE,
    CARDS,
    CHOOSE_ADDITIONAL_CARDS;


    /**
 * List of all the elements in this enum class
 */
    public static final List<MessageId> ALL = List.of(MessageId.values());

    /**
     * Total number of messageid
     */
    public static final int COUNT = ALL.size();
}
