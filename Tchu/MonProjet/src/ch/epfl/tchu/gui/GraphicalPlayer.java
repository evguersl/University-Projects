package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.game.Color;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;


import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import static javafx.application.Platform.isFxApplicationThread;

/**
 * Class who creates the GUI and the needed functionalities
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public final class GraphicalPlayer
{
    private final ObservableGameState observableGameState;
    private final ObjectProperty<ActionHandler.DrawTicketsHandler> drawTicketsHProperty;
    private final ObjectProperty<ActionHandler.DrawCardHandler> drawCardHProperty;
    private final ObjectProperty<ActionHandler.ClaimRouteHandler> claimRouteHProperty;
    private final ObservableList<Text> text;
    private final Stage window;
    private final BorderPane borderPane;

    /**
     * Constructor of Graphical Player
     * @param myPlayerId the identity of the corresponding player
     * @param playerNames the names of the players
     */
    public GraphicalPlayer(PlayerId myPlayerId, Map<PlayerId, String> playerNames)
    {
        assert isFxApplicationThread();

        this.observableGameState = new ObservableGameState(myPlayerId) ;
        this.text = FXCollections.observableArrayList();
        this.drawTicketsHProperty = new SimpleObjectProperty<>();
        this.drawCardHProperty = new SimpleObjectProperty<>();
        this.claimRouteHProperty = new SimpleObjectProperty<>();

        Pane mapView= MapViewCreator.createMapView(this.observableGameState,this.claimRouteHProperty, this::chooseClaimCards);
        VBox cardsView = DecksViewCreator.createCardsView(this.observableGameState,this.drawTicketsHProperty,this.drawCardHProperty);
        HBox handView = DecksViewCreator.createHandView(this.observableGameState);
        VBox infoView  = InfoViewCreator.createInfoView(myPlayerId,playerNames,this.observableGameState,this.text);

        this.borderPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        Scene scene = new Scene(borderPane);
        this.window = new Stage();
        this.window.setScene(scene);
        this.window.setTitle(String.format("tChu \u2014 %s", playerNames.get(myPlayerId)));
        this.window.show();

    }

    /**
     * Calls the method of ObservableGameState
     * @param publicGameState the current publicGameState
     * @param myPlayersState the current playersState
     */
    public void setState(PublicGameState publicGameState, PlayerState myPlayersState, Card card)
    {
        assert isFxApplicationThread();
        this.observableGameState.setState(publicGameState, myPlayersState,card);

    }

    /**
     * Adds a message to the display
     * @param message the message to display
     */
    public void receiveInfo(String message)
    {
        assert isFxApplicationThread();

        Text textInfo = new Text(message);
        if(text.size()>=Constants.MAXIMUM_INFO_DISPLAYED)
        {
            text.remove(0);
        }
        text.add(textInfo);
    }

    /**
     * The player chooses one of the three actions
     * @param drawTicketsH the DrawTicketsHandler
     * @param drawCardH the DrawCardHandler
     * @param claimRouteH the ClaimRouteHandler
     */
    public void startTurn(ActionHandler.DrawTicketsHandler drawTicketsH, ActionHandler.DrawCardHandler drawCardH, ActionHandler.ClaimRouteHandler claimRouteH)
    {
        assert isFxApplicationThread();

        if(this.observableGameState.canDrawTickets())
        {
            this.drawTicketsHProperty.setValue( () ->
                {
                    drawTicketsH.onDrawTickets();
                    this.drawTicketsHProperty.setValue(null);
                    this.drawCardHProperty.setValue(null);
                    this.claimRouteHProperty.setValue(null);
                });
        }
        else
        {
            this.drawTicketsHProperty.setValue(null);
        }

        if(this.observableGameState.canDrawCards())
        {
            this.drawCardHProperty.setValue( (slot) ->
                {

                    animation(slot,drawCardH);
                    //drawCardH.onDrawCard(slot);
                    this.drawTicketsHProperty.setValue(null);
                    this.drawCardHProperty.setValue(null);
                    this.claimRouteHProperty.setValue(null);
                });
        }
        else
        {
            this.drawCardHProperty.setValue(null);
        }

        this.claimRouteHProperty.setValue( (claimRoute, cardsToClaimRoute) ->
            {
                claimRouteH.onClaimRoute(claimRoute, cardsToClaimRoute);
                this.drawTicketsHProperty.setValue(null);
                this.drawCardHProperty.setValue(null);
                this.claimRouteHProperty.setValue(null);
            });
    }

    /**
     * The player chooses tickets
     * @param myTickets contains three or five tickets that the player can choose
     * @param chooseTicketsH the corresponding handler
     */
    public void chooseTickets(SortedBag<Ticket> myTickets, ActionHandler.ChooseTicketsHandler chooseTicketsH)
    {
        assert isFxApplicationThread();

        int howManyTicketsToChoose = myTickets.size()-Constants.DISCARDABLE_TICKETS_COUNT;
        String titleText = String.format(StringsFr.CHOOSE_TICKETS, howManyTicketsToChoose, StringsFr.plural(howManyTicketsToChoose));
        String titleStage =StringsFr.TICKETS_CHOICE;

        VBox vBox = new VBox();
        Stage stage = new Stage(StageStyle.UTILITY);
        ListView<Ticket> listView = new ListView<>();
        Button button = new Button();
        BooleanBinding booleanBinding = Bindings.greaterThan(Bindings.size(listView.getSelectionModel().getSelectedItems()),howManyTicketsToChoose-1).not();
        button.disableProperty().bind(booleanBinding);
        Consumer<Event> consumer = (e ->
            {
                stage.hide();
                SortedBag<Ticket> chosenTickets = SortedBag.of(listView.getSelectionModel().getSelectedItems());
                chooseTicketsH.onChooseTickets(chosenTickets);
            });
        createInitialiseVBox(vBox,stage,titleStage ,titleText, SelectionMode.MULTIPLE,button,FXCollections.observableArrayList(myTickets.toList()),listView, consumer);
    }

    /**
     * Called when the player needs to take a second card
     * @param drawCardH the corresponding handler
     */
    public void drawCard(ActionHandler.DrawCardHandler drawCardH)
    {
        assert isFxApplicationThread();

        this.drawCardHProperty.setValue( (slot) ->
        {
            animation(slot,drawCardH);
            this.drawTicketsHProperty.setValue(null);
            this.drawCardHProperty.setValue(null);
            this.claimRouteHProperty.setValue(null);
        });
    }


    /**
     * Open a stage the player can use use to select the set set of Cards he want to use to initialy claim a route
     * @param myInitialCards List of <SortedBag<Card>> the options
     * @param chooseCardsH the handler use to select the cards
     */
    public void chooseClaimCards(List<SortedBag<Card>> myInitialCards, ActionHandler.ChooseCardsHandler chooseCardsH)
    {
        assert isFxApplicationThread();

        String titleText = StringsFr.CHOOSE_CARDS;
        String titleStage =StringsFr.CARDS_CHOICE;
        ListView<SortedBag<Card>> listView = new ListView<>();
        listView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        VBox vBox= new VBox();
        Stage stage = new Stage(StageStyle.UTILITY);
        Button button = new Button();
        BooleanBinding booleanBinding = Bindings.equal(Bindings.size(listView.getSelectionModel().getSelectedItems()),1).not();
        button.disableProperty().bind(booleanBinding);
        Consumer<Event> consumer = (e ->
            {
                stage.hide();
                SortedBag<Card> selectedItems = listView.getSelectionModel().getSelectedItem()==null ? SortedBag.of() : listView.getSelectionModel().getSelectedItem();
                chooseCardsH.onChooseCards(selectedItems);
            });
         createInitialiseVBox(vBox,stage,titleStage ,titleText, SelectionMode.SINGLE,button,FXCollections.observableArrayList(myInitialCards),listView, consumer);
    }

    /**
     * Open a Stage the player can use to select, or not,  one of the possible options of additional cards to claim a tunnel
     * @param myAdditionalCards List of <SortedBag<Card>> the options
     * @param chooseCardsH the handler use to choose the cards
     */

    public void chooseAdditionalCards(List<SortedBag<Card>> myAdditionalCards, ActionHandler.ChooseCardsHandler chooseCardsH)
    {
        assert isFxApplicationThread();

        String titleText = StringsFr.CHOOSE_ADDITIONAL_CARDS;
        String titleStage =StringsFr.CARDS_CHOICE;

        VBox vBox = new VBox();
        Stage stage = new Stage(StageStyle.UTILITY);
        ListView<SortedBag<Card>> listView = new ListView<>();
        listView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        Button button = new Button();
        Consumer<Event> consumer = (e ->
            {
                stage.hide();
                SortedBag<Card> selectedItems = listView.getSelectionModel().getSelectedItem()==null ? SortedBag.of() : listView.getSelectionModel().getSelectedItem();
                chooseCardsH.onChooseCards(selectedItems);

            });
        createInitialiseVBox(vBox,stage,titleStage ,titleText, SelectionMode.SINGLE,button,FXCollections.observableArrayList(myAdditionalCards),listView, consumer);
    }


    private  <T> void  createInitialiseVBox  (VBox vBox,Stage stage, String titleStage, String titleText, SelectionMode mode,Button button, ObservableList<T> list,ListView<T> listView, Consumer<Event> consumer)
    {
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");

        stage.setScene(scene);
        stage.initOwner(this.window);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setOnCloseRequest(Event::consume);
        stage.setTitle(titleStage);

        TextFlow textFlow = new TextFlow();
        Text text = new Text();
        text.setText(titleText);
        textFlow.getChildren().add(text);
        vBox.getChildren().add(textFlow);

        listView.setItems(list);
        listView.getSelectionModel().setSelectionMode(mode);
        vBox.getChildren().add(listView);

        button.setOnAction(consumer::accept);
        button.setText(StringsFr.CHOOSE);
        vBox.getChildren().add(button);
        stage.show();
    }

    /**
     * Class who defines a convinient way to display a SortedBag of Card
     * @author Jean-Francois rocher (316766)
     * @author Evgueni Rousselot (320195)
     */
    private static final class CardBagStringConverter extends StringConverter<SortedBag<Card>>
    {
        @Override
        public String toString(SortedBag<Card> cards)
        {
            return Info.cardDescription(cards);
        }

        @Override
        public SortedBag<Card> fromString(String string)
        {
            throw new UnsupportedOperationException();
        }
    }

    private void animation(int slot,ActionHandler.DrawCardHandler handler)
    {

            if(this.observableGameState.topCardProperty().get() != null) {

                StackPane pane = new StackPane();

                pane.getStylesheets().addAll("colors.css", "decks.css");

                Rectangle rectangleOutside = CreateShape.createRectangle(Constants.RECTANGLE_WIDTH_DECK_OUTSIDE, Constants.RECTANGLE_HIGH_DECK_OUTSIDE, List.of("outside"));


                Rectangle rectangleInside = CreateShape.createRectangle(Constants.RECTANGLE_WIDTH_DECK_INSIDE, Constants.RECTANGLE_HIGH_DECK_INSIDE, List.of("inside", "filled"));


                Rectangle rectangleImage = CreateShape.createRectangle(Constants.RECTANGLE_WIDTH_DECK_INSIDE, Constants.RECTANGLE_HIGH_DECK_INSIDE, List.of("train-image"));


                Card c = slot >= 0 ? observableGameState.faceUpCardsInfoProperty(slot).get() : observableGameState.topCardProperty().get();
                String colorName = c.color() != null ? c.color().name() : "NEUTRAL";

                pane.getStyleClass().addAll("card", colorName);

                pane.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage);
                rectangleInside.getStyleClass().addAll(colorName, "filled");
                pane.setTranslateX(Constants.FACE_UP_CARD_X);
                int slotCoordinates = slot >= 0 ? slot : Constants.CENTER_COORDINATES_Y.size() - 1;
                pane.setTranslateY(Constants.CENTER_COORDINATES_Y.get(slotCoordinates));


                this.borderPane.getChildren().add(pane);
                TranslateTransition transition = new TranslateTransition();
                transition.setDuration(Duration.seconds(Constants.ANIMATION_LENGTH));
                transition.setNode(pane);
                double xCoordinates = c.color() == null ? Constants.CENTER_COORDINATES_X.get(Constants.CENTER_COORDINATES_X.size() - 1) : Constants.CENTER_COORDINATES_X.get(Color.ALL.indexOf(c.color()));
                transition.setToX(xCoordinates);
                transition.setToY(Constants.HAND_VIEW_Y);
                transition.setOnFinished((e) ->
                {
                    handler.onDrawCard(slot);
                    this.borderPane.getChildren().remove(pane);

                });
                transition.play();
            }
            else
            {
                handler.onDrawCard(slot);
            }





    }
}




