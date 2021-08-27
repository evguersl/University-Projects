package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
/**
 * The main program for the client
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public final class ClientMain extends Application
{

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
            String ipAdress = args.isEmpty()? "localhost": args.get(0);
            int port = args.isEmpty()? 5108: Integer.parseInt(args.get(1));
            RemotePlayerClient remotePlayerClient1 = new RemotePlayerClient(new GraphicalPlayerAdapter(),ipAdress ,port );

            new Thread(remotePlayerClient1::run).start();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
