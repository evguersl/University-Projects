package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.io.*;

import static ch.epfl.tchu.net.Serdes.*;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Class who creates a Proxy for the player
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public final class RemotePlayerProxy implements Player
{
    private final Socket proxySocket;

    /**
     * Create a RemotePlayerProxy and use the argument newProwySocket to establish a connection
     * @param newProxySocket the Socket to use for the connection
     */
    public RemotePlayerProxy(Socket newProxySocket)
    {
        this.proxySocket = newProxySocket;
    }

    private void sendMessage(String messageToSend)
    {
        try
        {
            BufferedWriter w =
                    new BufferedWriter(
                            new OutputStreamWriter(this.proxySocket.getOutputStream(), US_ASCII));
            w.write(messageToSend);
            w.write('\n');
            w.flush();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    private String receiveMessage()
    {
        try
        {
            BufferedReader r =
                new BufferedReader(
                        new InputStreamReader(this.proxySocket.getInputStream(), US_ASCII));
            return r.readLine();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    private void join(String str1, String str2)
    {
        join(str1, str2, "");
    }

    private void join(String str1, String str2, String str3)
    {
        String delimiter = " ";
        StringJoiner joiner2 = new StringJoiner(delimiter);
        joiner2.add(str1)
                .add(str2)
                .add(str3);

        sendMessage(joiner2.toString());
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames)
    {
        String messageIdName = MessageId.INIT_PLAYERS.name();
        String serializedOwnId = SERDE_PLAYER_ID.serialize(ownId);
        String serializedPlayerNames = SERDE_LIST_STRING.serialize(List.of(playerNames.get(PlayerId.PLAYER_1),playerNames.get(PlayerId.PLAYER_2)));

        join(messageIdName, serializedOwnId, serializedPlayerNames);
    }

    @Override
    public void receiveInfo(String info)
    {
        String messageIdName = MessageId.RECEIVE_INFO.name();
        String serializedInfo = SERDE_STRING.serialize(info);
        join(messageIdName, serializedInfo);
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState, Card card)
    {
        String messageIdName = MessageId.UPDATE_STATE.name();
        String serializedNewState = SERDE_PUBLIC_GAME_STATE.serialize(newState);
        String serializedOwnState = SERDE_PLAYER_STATE.serialize(ownState);
        serializedOwnState = serializedOwnState + " " +SERDE_CARD.serialize(card);

        join(messageIdName, serializedNewState, serializedOwnState);
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets)
    {
        String messageIdName = MessageId.SET_INITIAL_TICKETS.name();
        String serializedTickets = SERDE_SORTEDBAG_TICKET.serialize(tickets);

        join(messageIdName, serializedTickets);
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets()
    {
        String messageIdName = MessageId.CHOOSE_INITIAL_TICKETS.name();
        sendMessage(messageIdName);
        String serializedMessage = receiveMessage();
        return SERDE_SORTEDBAG_TICKET.deserialize(serializedMessage);
    }

    @Override
    public TurnKind nextTurn()
    {
        String messageIdName = MessageId.NEXT_TURN.name();
        sendMessage(messageIdName);
        String serializedMessage = receiveMessage();
        return SERDE_TURN_KIND.deserialize(serializedMessage);
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options)
    {
        String messageIdName = MessageId.CHOOSE_TICKETS.name();
        String serializedTickets = SERDE_SORTEDBAG_TICKET.serialize(options);

        join(messageIdName,serializedTickets);

        String serializedMessage = receiveMessage();
        return SERDE_SORTEDBAG_TICKET.deserialize(serializedMessage);
    }

    @Override
    public int drawSlot()
    {
        String messageIdName = MessageId.DRAW_SLOT.name();
        sendMessage(messageIdName);
        String serializedMessage = receiveMessage();
        return SERDE_INT.deserialize(serializedMessage);
    }

    @Override
    public Route claimedRoute()
    {
        String messageIdName = MessageId.ROUTE.name();
        sendMessage(messageIdName);
        String serializedMessage = receiveMessage();
        return SERDE_ROUTE.deserialize(serializedMessage);
    }

    @Override
    public SortedBag<Card> initialClaimCards()
    {
        String messageIdName = MessageId.CARDS.name();
        sendMessage(messageIdName);
        String serializedMessage = receiveMessage();
        return SERDE_SORTEDBAG_CARD.deserialize(serializedMessage);
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options)
    {
        String messageIdName = MessageId.CHOOSE_ADDITIONAL_CARDS.name();
        String serializedMessageToSend = SERDE_LIST_SORTEDBAG.serialize(options);

        join(messageIdName, serializedMessageToSend);

        String serializedMessageReceived = receiveMessage();
        return SERDE_SORTEDBAG_CARD.deserialize(serializedMessageReceived);
    }
}
