package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Main program for the server
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public final class ServerMain extends Application
{
    private static final int PORT_NUMBER = 5108;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            List<String> args = this.getParameters().getRaw();

            String name1 = args.isEmpty()? "Ada": args.get(0);
            String name2 = args.isEmpty()? "Charles": args.get(1);

            ServerSocket s0 = new ServerSocket(PORT_NUMBER);
            Socket s = s0.accept();

            GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
            RemotePlayerProxy remotePlayerProxy = new RemotePlayerProxy(s);

            Random rng = new Random();

            Map<PlayerId, Player> players = Map.of(PlayerId.PLAYER_1, graphicalPlayerAdapter, PlayerId.PLAYER_2, remotePlayerProxy);
            Map<PlayerId, String> playerNames = Map.of(PlayerId.PLAYER_1, name1, PlayerId.PLAYER_2, name2);

            new Thread(() -> Game.play(players, playerNames, SortedBag.of(ChMap.tickets()), rng)).start();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
