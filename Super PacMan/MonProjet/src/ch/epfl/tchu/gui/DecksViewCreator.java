package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Class who creates the Deck View of the GUI
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

abstract class DecksViewCreator
{
    /**
     *
     * @param gameState the ObservableGameState of the Player
     * @return the JavaFx HBox correspond to the handView of GUI
     */
    public static HBox createHandView(ObservableGameState gameState)
    {
        HBox mainBox = new HBox();
        ListView<Ticket> listView = new ListView<>();
        listView.setId("tickets");
        listView.setItems(gameState.ticketPlayer());
        ObservableList<Ticket> ticketSelected = listView.getSelectionModel().getSelectedItems();
        ListChangeListener<Ticket> ticketListChangeListener = (t)-> {gameState.updateSelectedStation(ticketSelected);};
        ticketSelected.addListener(ticketListChangeListener);

        mainBox.getChildren().add(listView);
        HBox box = new HBox();
        mainBox.getStylesheets().addAll("decks.css","colors.css");
        mainBox.getChildren().add(box);
        box.setId("hand-pane");

        for(Card card : Card.ALL)
        {
            StackPane stackPane = new StackPane();
            String colorName = card.color()!=null ? card.color().name():"NEUTRAL";
            stackPane.getStyleClass().addAll(colorName,"card");
            ReadOnlyIntegerProperty propertyNumber = gameState.cardInfoProperty(Card.ALL.indexOf(card));
            stackPane.visibleProperty().bind(Bindings.greaterThan((propertyNumber), 0));

            addRectangle(stackPane);

            Text text = new Text();
            text.textProperty().bind(Bindings.convert(propertyNumber));
            text.getStyleClass().add("count");
            text.visibleProperty().bind(Bindings.greaterThan(propertyNumber, 1));
            stackPane.getChildren().add(text);
            box.getChildren().add(stackPane);
        }
        VBox extensionBox = new VBox();

        Text text1 = new Text();
        text1.setId("time-info");
        StringExpression expression1 = Bindings.format("\n\nTotal time : %s s !",gameState.timeProperty());
        text1.textProperty().bind(expression1);
        extensionBox.getChildren().add(text1);

        Text text2 = new Text();
        text2.setId("points-info");
        StringExpression expression2 = Bindings.format("Total points : %s !",gameState.pointsProperty());
        text2.textProperty().bind(expression2);
        extensionBox.getChildren().add(text2);

        mainBox.getChildren().add(extensionBox);
        return  mainBox;
    }

    /**
     *
     * @param gameState the gameState the ObservableGameState of the Player
     * @param drawTicketH the Handler use to draw the tickets
     * @param drawCardH the Handler use to draw the Cards
     * @return the JavaFx VBox correspond to the cardsView of the Player in the GUI
     */
    public static VBox createCardsView(ObservableGameState gameState, ObjectProperty<ActionHandler.DrawTicketsHandler> drawTicketH,ObjectProperty<ActionHandler.DrawCardHandler> drawCardH)
    {
        VBox box = new VBox();
        box.setId("card-pane");
        box.getStylesheets().addAll("decks.css","colors.css");

        Button buttonTicket = buttonCreator(gameState.ticketPercentageProperty(),StringsFr.TICKETS,drawTicketH);
        buttonTicket.setOnMouseClicked(e -> drawTicketH.get().onDrawTickets());
        box.getChildren().add(buttonTicket);

        for (int i=0; i< Constants.FACE_UP_CARDS_COUNT;i++)
        {
            Card c = gameState.faceUpCardsInfoProperty(i).get();
            StackPane pane = new StackPane();
            String colorName;
            pane.disableProperty().bind(drawCardH.isNull());

            if (c!=null)
            {
                colorName = c.color() != null ? c.color().name() : "NEUTRAL";
                pane.getStyleClass().addAll(colorName,"card");
            }
            else
            {
                pane.getStyleClass().add("card");
            }

            int finalI = i;
            pane.setOnMouseClicked(e -> {
                drawCardH.get().onDrawCard(finalI);

            });

            gameState.faceUpCardsInfoProperty(i).addListener((o, Ov, nV)->
                {
                    String newName = nV.color()!=null ? nV.color().name():"NEUTRAL";
                    pane.getStyleClass().setAll(newName,"card");

                });

            addRectangle(pane);
            box.getChildren().add(pane);
        }

        Button buttonCard = buttonCreator(gameState.cardPercentageProperty(),StringsFr.CARDS,drawCardH);
        buttonCard.setOnMouseClicked(e -> drawCardH.get().onDrawCard(Constants.DECK_SLOT));
        box.getChildren().add(buttonCard);

        return box;
    }

    private static void addRectangle(StackPane stack)
    {
        Rectangle rectangleOutside = CreateShape.createRectangle(Constants.RECTANGLE_WIDTH_DECK_OUTSIDE,Constants.RECTANGLE_HIGH_DECK_OUTSIDE,List.of("outside"));


        Rectangle rectangleInside = CreateShape.createRectangle(Constants.RECTANGLE_WIDTH_DECK_INSIDE,Constants.RECTANGLE_HIGH_DECK_INSIDE,List.of("filled","inside"));


        Rectangle rectangleImage = CreateShape.createRectangle(Constants.RECTANGLE_WIDTH_DECK_INSIDE,Constants.RECTANGLE_HIGH_DECK_INSIDE,List.of("train-image"));


        stack.getChildren().addAll(rectangleOutside,rectangleInside,rectangleImage);
    }




    private static Node buttonNode(ReadOnlyIntegerProperty property)
    {
        Group group =new Group();

        Rectangle rectangleBackGround = new Rectangle();
        rectangleBackGround.getStyleClass().add("background");
        rectangleBackGround.setHeight(Constants.RECTANGLE_HIGH_BUTTON_POP_UP);
        rectangleBackGround.setWidth(Constants.RECTANGLE_WIDTH_BUTTON_POP_UP);

        Rectangle rectangleForeGround = new Rectangle();
        rectangleForeGround.getStyleClass().add("foreground");
        rectangleForeGround.setHeight(Constants.RECTANGLE_HIGH_GAUGE);
        rectangleForeGround.widthProperty().bind(property.multiply(Constants.RECTANGLE_WIDTH_BUTTON_POP_UP).divide(100));

        group.getChildren().addAll(rectangleBackGround,rectangleForeGround);

        return group;
    }

    private static <T> Button buttonCreator(ReadOnlyIntegerProperty percentageProperty,String buttonName, ReadOnlyObjectProperty<T> drawProperty)
    {
        Button button = new Button();
        button.getStyleClass().add("gauged");
        button.setGraphic(buttonNode(percentageProperty));
        button.disableProperty().bind(drawProperty.isNull());
        button.setText(buttonName);
        return button;
    }
}
