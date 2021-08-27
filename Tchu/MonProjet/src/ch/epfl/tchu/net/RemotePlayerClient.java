package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Class who creates a remote Player
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */
public final class RemotePlayerClient
{
    private final Player player ;
    private final String name;
    private final int port;

    /**
     *
     * @param player the givrn player
     * @param name the name of the player
     * @param port the port to connect to
     */
    public RemotePlayerClient(Player player,String name, int port)
    {
        this.player = player;
        this.name=name;
        this.port = port ;
    }

    /**
     * Executes the client
     *
     * 1) Waits for a message of the proxy
     * 2) Separates it with the given character
     * 3) Determines the type of the message
     * 4) Deserialazes the message and serializes the response
     *
     */
    public void run()
    {
        String message;
        try{
             Socket s = new Socket(name,port);
             BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream(), US_ASCII));
             BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), US_ASCII));
             {
                while( (message = r.readLine())!=null )
                {
                    String espace = " ";
                    String[] messageSpilted = message.split(Pattern.quote(espace), -1);
                    MessageId type = MessageId.valueOf(messageSpilted[0]);

                    String virgule = ",";
                    switch (type)
                    {
                        case INIT_PLAYERS:
                            PlayerId playerId = Serdes.SERDE_PLAYER_ID.deserialize(messageSpilted[1]);
                            String[] namePlayers = messageSpilted[2].split(Pattern.quote(virgule), -1);

                            Map<PlayerId, String> playerNamesMap = Map.of(PlayerId.PLAYER_1,
                                    Serdes.SERDE_STRING.deserialize(namePlayers[0]),
                                    PlayerId.PLAYER_2,
                                    Serdes.SERDE_STRING.deserialize(namePlayers[1]));
                            player.initPlayers(playerId, playerNamesMap);
                            break;


                        case RECEIVE_INFO:
                            String receivedInfo = Serdes.SERDE_STRING.deserialize(messageSpilted[1]);
                            player.receiveInfo(receivedInfo);
                            break;

                        case UPDATE_STATE:
                            PublicGameState gameState = Serdes.SERDE_PUBLIC_GAME_STATE.deserialize(messageSpilted[1]);
                            PlayerState ownState = Serdes.SERDE_PLAYER_STATE.deserialize(messageSpilted[2]);
                            Card card = Serdes.SERDE_CARD.deserialize(messageSpilted[3]);
                            player.updateState(gameState, ownState, card);
                            break;

                        case SET_INITIAL_TICKETS:
                            SortedBag<Ticket> initialTickets = Serde.bagOf(Serdes.SERDE_TICKET, virgule).deserialize(messageSpilted[1]);
                            player.setInitialTicketChoice(initialTickets);
                            break;

                        case CHOOSE_INITIAL_TICKETS:
                            SortedBag<Ticket> ticketsChoice = player.chooseInitialTickets();
                            String serialiseTicketsChoice = Serde.bagOf(Serdes.SERDE_TICKET, virgule).serialize(ticketsChoice);
                            sendMessage(w, serialiseTicketsChoice);
                            break;

                        case NEXT_TURN:
                            Player.TurnKind nextTurn = player.nextTurn();
                            String serialisedTurn = Serdes.SERDE_TURN_KIND.serialize(nextTurn);
                            sendMessage(w, serialisedTurn);
                            break;

                        case CHOOSE_TICKETS:
                            Serde<SortedBag<Ticket>> serdeTicketsBag = Serde.bagOf(Serdes.SERDE_TICKET, virgule);
                            SortedBag<Ticket> options = serdeTicketsBag.deserialize(messageSpilted[1]);
                            SortedBag<Ticket> choice = player.chooseTickets(options);
                            String serialisedChoice = serdeTicketsBag.serialize(choice);
                            sendMessage(w, serialisedChoice);
                            break;

                        case DRAW_SLOT:
                            int slot = player.drawSlot();
                            String serialisedSlot = Serdes.SERDE_INT.serialize(slot);
                            sendMessage(w, serialisedSlot);
                            break;

                        case ROUTE:
                            Route routeToClaim = player.claimedRoute();
                            String serialisedRoute = Serdes.SERDE_ROUTE.serialize(routeToClaim);
                            sendMessage(w, serialisedRoute);
                            break;

                        case CARDS:
                            Serde<SortedBag<Card>> serdeCardBag = Serde.bagOf(Serdes.SERDE_CARD, virgule);
                            SortedBag<Card> initialClaimCards = player.initialClaimCards();
                            String serialisedInitialClaimCards = serdeCardBag.serialize(initialClaimCards);
                            sendMessage(w, serialisedInitialClaimCards);
                            break;

                        case CHOOSE_ADDITIONAL_CARDS:
                            String stringListAdditionalCardsBags = messageSpilted[1];
                            List<SortedBag<Card>> listAdditionalCardsBags = Serdes.SERDE_LIST_SORTEDBAG.deserialize(stringListAdditionalCardsBags);
                            SortedBag<Card> choosenBag = player.chooseAdditionalCards(listAdditionalCardsBags);
                            String serialisedChoosenBag = Serde.bagOf(Serdes.SERDE_CARD, virgule).serialize(choosenBag);
                            sendMessage(w, serialisedChoosenBag);
                    }
                }
            }
        }
        catch (IOException ioException)
        {
            throw new UncheckedIOException(ioException);
        }
    }

    private void sendMessage(BufferedWriter w, String message)
    {
        try
        {
            w.write(message);
            w.write("\n");
            w.flush();
        }
        catch (IOException e)
        {
            throw  new UncheckedIOException(e);
        }
    }
}

