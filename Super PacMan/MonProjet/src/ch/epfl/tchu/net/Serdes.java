package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Class who countains all the Serdes needed for the game.
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */
abstract public class Serdes
{
    private final static String DEUX_POINTS = ":";
    private final static String VIRGULE = ",";
    private final static String POINT_VIRGULE = ";";

    /**
     * Serde for Integer
     */
    public final static Serde<Integer> SERDE_INT = Serde.of(integer->Integer.toString(integer), Integer::parseInt);

    /**
     * Serde for String
     */
    public final static Serde<String> SERDE_STRING =
            Serde.of(str -> Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8)),
                     s -> new String(Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8)),
                     StandardCharsets.UTF_8));

    /**
     * Serde for PlayerId
     */
    public final static Serde<PlayerId> SERDE_PLAYER_ID = Serde.oneOf(PlayerId.ALL);

    /**
     * Serde for Player.TurnKind
     */
    public final static Serde<Player.TurnKind> SERDE_TURN_KIND = Serde.oneOf(Player.TurnKind.ALL);

    /**
     * Serde for Card
     */
    public final static Serde<Card> SERDE_CARD = Serde.oneOf(Card.ALL);

    /**
     * Serde for Route
     */
    public final static Serde<Route> SERDE_ROUTE = Serde.oneOf(ChMap.routes());

    /**
     * Serde for Ticket
     */
    public final static Serde<Ticket> SERDE_TICKET = Serde.oneOf(ChMap.tickets());

    /**
     * Serde for List<String>
     */
    public final static Serde<List<String>> SERDE_LIST_STRING = Serde.listOf(SERDE_STRING,VIRGULE);

    /**
     * Serde for List<Card>
     */
    public final static Serde<List<Card>> SERDE_LIST_CARD = Serde.listOf(SERDE_CARD,VIRGULE);

    /**
     * Serde for List<Route>
     */
    public final static Serde<List<Route>> SERDE_LIST_ROUTE = Serde.listOf(SERDE_ROUTE,VIRGULE);

    /**
     * Serde for List<SortedBag<Card>>
     */
    public final static Serde<List<SortedBag<Card>>> SERDE_LIST_SORTEDBAG = Serde.listOf(Serde.bagOf(SERDE_CARD,VIRGULE),POINT_VIRGULE);

    /**
     * Serde for List<SortedBag<Ticket>>
     */
    public final static Serde<SortedBag<Ticket>> SERDE_SORTEDBAG_TICKET = Serde.bagOf(SERDE_TICKET, VIRGULE);

    /**
     * Serde for List<SortedBag<Card>>
     */
    public final static Serde<SortedBag<Card>> SERDE_SORTEDBAG_CARD = Serde.bagOf(SERDE_CARD, VIRGULE);

    /**
     * Serde for List<PublicCardState>
     */
    public final static Serde<PublicCardState> SERDE_PUBLIC_CARD_STATE =
            Serde.of( pgs ->
                {
                    StringJoiner joiner = new StringJoiner(POINT_VIRGULE);
                    joiner.add(SERDE_LIST_CARD.serialize(pgs.faceUpCards()))
                            .add(SERDE_INT.serialize(pgs.deckSize()))
                            .add(SERDE_INT.serialize(pgs.discardsSize()));

                    return joiner.toString();
                },
            str->
                {
                    String[] a = str.split(Pattern.quote(POINT_VIRGULE),-1);
                    return new PublicCardState(SERDE_LIST_CARD.deserialize(a[0]), SERDE_INT.deserialize(a[1]),
                            SERDE_INT.deserialize(a[2]));
                });

    /**
     * Serde for List<PublicPlayerState>
     */
    public final static Serde<PublicPlayerState> SERDE_PUBLIC_PLAYER_STATE =
            Serde.of(
                    pps ->
                        {
                            StringJoiner joiner = new StringJoiner(POINT_VIRGULE);
                            joiner.add(SERDE_INT.serialize(pps.ticketCount()))
                                    .add(SERDE_INT.serialize(pps.cardCount()))
                                    .add(SERDE_LIST_ROUTE.serialize(pps.routes()));

                            return joiner.toString();
                        },
                    s ->
                        {
                            String[] newTab = s.split(Pattern.quote(POINT_VIRGULE), -1);
                            return new PublicPlayerState(SERDE_INT.deserialize(newTab[0]),
                                    SERDE_INT.deserialize(newTab[1]), SERDE_LIST_ROUTE.deserialize(newTab[2]));
                        });

    /**
     * Serde for List<PublicPlayerState>
     *
     */
    public final static Serde<PlayerState> SERDE_PLAYER_STATE =
            Serde.of(
                    ps ->
                    {
                        StringJoiner joiner = new StringJoiner(POINT_VIRGULE);
                        joiner.add(Serde.bagOf(SERDE_TICKET,VIRGULE).serialize(ps.tickets()))
                                .add(Serde.bagOf(SERDE_CARD,VIRGULE).serialize(ps.cards()))
                                .add(Serdes.SERDE_LIST_ROUTE.serialize(ps.routes()))
                                .add(Serdes.SERDE_INT.serialize(ps.getTime()));
                        return joiner.toString();
                    },
                    s ->
                    {
                        String[] newTab = s.split(Pattern.quote(POINT_VIRGULE), -1);
                        return new PlayerState(Serde.bagOf(SERDE_TICKET,VIRGULE).deserialize(
                                newTab[0]),Serde.bagOf(SERDE_CARD,VIRGULE).deserialize(newTab[1]),
                                Serdes.SERDE_LIST_ROUTE.deserialize(newTab[2]),Serdes.SERDE_INT.deserialize(newTab[3]));
                    });

    /**
     * Serde for List<PublicGameState>
     */
    public final static Serde<PublicGameState> SERDE_PUBLIC_GAME_STATE =
            Serde.of(
                    pgs ->
                        {
                            StringJoiner joiner = new StringJoiner(DEUX_POINTS);
                            joiner.add(SERDE_INT.serialize(pgs.ticketsCount()))
                                    .add(SERDE_PUBLIC_CARD_STATE.serialize(pgs.cardState()))
                                    .add(SERDE_PLAYER_ID.serialize(pgs.currentPlayerId()))
                                    .add(SERDE_PUBLIC_PLAYER_STATE.serialize(pgs.playerState(PlayerId.PLAYER_1)))
                                    .add(SERDE_PUBLIC_PLAYER_STATE.serialize(pgs.playerState(PlayerId.PLAYER_2)))
                                    .add(SERDE_PLAYER_ID.serialize(pgs.lastPlayer()));
                            return joiner.toString();
                        },
                    s ->
                        {
                            String[] splitedSerializeArray = s.split(Pattern.quote(DEUX_POINTS), -1);
                            PlayerId currentPlayerId = SERDE_PLAYER_ID.deserialize(splitedSerializeArray[2]);
                            PlayerId lastPlayerId = SERDE_PLAYER_ID.deserialize(splitedSerializeArray[5]);
                            Map<PlayerId, PublicPlayerState> newMap = new HashMap<>();
                            newMap.put(PlayerId.PLAYER_1, SERDE_PUBLIC_PLAYER_STATE.deserialize(splitedSerializeArray[3]));
                            newMap.put(PlayerId.PLAYER_2, SERDE_PUBLIC_PLAYER_STATE.deserialize(splitedSerializeArray[4]));
                            return new PublicGameState(SERDE_INT.deserialize(splitedSerializeArray[0]),
                                    SERDE_PUBLIC_CARD_STATE.deserialize(splitedSerializeArray[1]),
                                    currentPlayerId, newMap, lastPlayerId);
                        });
}



