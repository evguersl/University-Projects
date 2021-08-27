package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.ChMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;


public class Annexe   extends Application{
    public static void main (String [] args )
    {


            launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();

        ImageView view = new ImageView();
        pane.getStylesheets().add("map.css");
        pane.getChildren().add(view);
        PrintWriter writer = new PrintWriter("fichierCss.txt", StandardCharsets.UTF_8);

        AtomicInteger i = new AtomicInteger();

        pane.setOnMouseClicked(e ->
        {
            writer.println(String.format("#%s { -fx-translate-x: %s; -fx-translate-y: %s;}",ChMap.getAllStationsWithoutCountries().get(i.get()).name(),e.getX(),e.getY()));
            i.getAndIncrement();
            if (i.get()==ChMap.getAllStationsWithoutCountries().size())
            {
                writer.close();
                System.out.println("hello");
            }
        });
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
