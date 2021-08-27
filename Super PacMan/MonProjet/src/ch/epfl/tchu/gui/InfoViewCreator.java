package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.Map;

/**
 * Class who creates the Info View of the GUI
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */
abstract class InfoViewCreator
{
    /**
     *
     * @param player the identity of the player
     * @param namePlayer the name of the player
     * @param gameState the current game state
     * @param list a list that contains the information on the current game in the form of instances of TEXT
     * @return the corresponding VBox
     */
    public static VBox createInfoView(PlayerId player, Map<PlayerId,String> namePlayer, ObservableGameState gameState, ObservableList<Text> list)
    {
        VBox mainBox = new VBox();
        mainBox.getStylesheets().addAll("info.css","colors.css");
        mainBox.getChildren().add(initialisePlayerBox(player,namePlayer,gameState));
        mainBox.getChildren().add(initialisePlayerBox(player.next(),namePlayer,gameState));

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        mainBox.getChildren().add(separator);


        TextFlow textFlow = new TextFlow();
        textFlow.setId("game-info");
        mainBox.getChildren().add(textFlow);
        Bindings.bindContent(textFlow.getChildren(),list);

        return mainBox;
    }

    private static VBox initialisePlayerBox(PlayerId playerId,Map<PlayerId,String> namePlayer, ObservableGameState gameState)
    {
        String playerName = namePlayer.get(playerId);
        ReadOnlyIntegerProperty numberTicket = gameState.numberTicketPlayerProperty(playerId);
        ReadOnlyIntegerProperty numberCards = gameState.numberCardPlayerProperty(playerId);
        ReadOnlyIntegerProperty numberCar = gameState.numberCarPlayerProperty(playerId);
        ReadOnlyIntegerProperty numberPoints = gameState.numberConstructPointPlayerProperty(playerId);

        VBox playerBox = new VBox();
        playerBox.setId("player-stats");

        TextFlow textFlow = new TextFlow();
        textFlow.getStyleClass().add(playerId.name());

        Circle circleColor = new Circle();
        circleColor.setRadius(Constants.CIRCLE_RADIUS_PLAYER);
        circleColor.getStyleClass().add("filled");

        Text textStat = new Text();
        StringExpression expression = Bindings.format(StringsFr.PLAYER_STATS,playerName, numberTicket,numberCards,numberCar,numberPoints);
        textStat.textProperty().bind(expression);

        textFlow.getChildren().addAll(circleColor,textStat);

        playerBox.getChildren().addAll(textFlow);

        return playerBox;
    }
}
