package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.text.Normalizer;
import java.util.List;

/**
 * Class who creates the Map View of the GUI
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

abstract class MapViewCreator
{
    /**
     * Creates a Pane that contains the map of the game with the routes taken or note
     * @param gameState the current gameState
     * @param claimRouteH the claim route handler
     * @param cardChooser the card chooser handler
     * @return the ch.epfl.tchu.gui map of the tchu game
     */
    public static Pane createMapView(ObservableGameState gameState, ObjectProperty<ActionHandler.ClaimRouteHandler> claimRouteH,CardChooser cardChooser )
    {
        Pane pane = new Pane();

        pane.getStylesheets().addAll("map.css","colors.css");
        ImageView fond = new ImageView();
        pane.getChildren().add(fond);



        for (Station station: ChMap.getAllStationsWithoutCountries())
        {

            Circle stationCircle = new Circle();
            stationCircle.setRadius(Constants.CIRCLE_RADIUS_STATION);
            stationCircle.visibleProperty().bind(gameState.stationSelectedProperty(station));
            String s = Normalizer.normalize(station.name(), Normalizer.Form.NFD);
            s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            s = s.replaceAll(" ","");
            stationCircle.getStyleClass().add("gare-image");
            stationCircle.setId(s);
            pane.getChildren().add(stationCircle);
        }
        for (Route route : ChMap.routes())
        {
            Group routeGroup = new Group();
            routeGroup.disableProperty().bind(
                    claimRouteH.isNull().or(gameState.canCaptureRouteInfoProperty(route).not()));

            routeGroup.setOnMouseClicked(e ->
                {
                    List<SortedBag<Card>> possibleClaimCards = gameState.possibleClaimCards(route);

                    if(1==possibleClaimCards.size())
                    {
                        claimRouteH.get().onClaimRoute(route, possibleClaimCards.get(0));
                    }
                    else
                    {
                        ActionHandler.ChooseCardsHandler chooseCardsH = chosenCards -> claimRouteH.get().onClaimRoute(route, chosenCards);
                        cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
                    }
                });

            pane.getChildren().add(routeGroup);
            String color = (route.color()==null)? "NEUTRAL":String.valueOf(route.color());
            String level = (String.valueOf(route.level()).equals("UNDERGROUND"))? String.valueOf(route.level()):null;
            gameState.routeInfoProperty(route).addListener((o, Ov, nV) -> routeGroup.getStyleClass().add(nV.name()));
            routeGroup.getStyleClass().addAll("route",level,color);

            for (int i = 1; i<=route.length();i++)
            {
                Group oneCase = new Group();

                oneCase.setId(route.id()+"_"+(i));

                Rectangle voie = CreateShape.createRectangle(Constants.RECTANGLE_WIDTH_ROAD,Constants.RECTANGLE_HIGH_ROAD,List.of("track","filled"));
                oneCase.getChildren().add(voie);

                Group wagon = new Group();
                wagon.getStyleClass().add("car");

                Rectangle rectangleWagon = CreateShape.createRectangle(Constants.RECTANGLE_WIDTH_ROAD,Constants.RECTANGLE_HIGH_ROAD,List.of("filled"));

                gameState.routeInfoProperty(ChMap.routes().get(i)).addListener((o, Ov, nV) -> rectangleWagon.getStyleClass().add(nV.name()));

                Circle circle1 = CreateShape.createCircle(Constants.FIRST_CIRCLE_COORDINATES.get(0),Constants.FIRST_CIRCLE_COORDINATES.get(1),Constants.CIRCLE_RADIUS_CASE);

                Circle circle2 = CreateShape.createCircle(Constants.SECOND_CIRCLE_COORDINATES.get(0),Constants.SECOND_CIRCLE_COORDINATES.get(1),Constants.CIRCLE_RADIUS_CASE);

                wagon.getChildren().addAll(rectangleWagon,circle1, circle2);
                oneCase.getChildren().add(wagon);
                routeGroup.getChildren().add(oneCase);
            }
        }
        return pane;
    }

    /**
     * interface that has a method to be called when the player has multiple choice to claim a Route
     * @author Jean-Francois rocher (316766)
     * @author Evgueni Rousselot (320195)
     */
    @FunctionalInterface
    interface CardChooser
    {
        /**
         * To be called when the player has multiple choice to claim a Route
         * @param options the different options the player has
         * @param handler The handler to choose cards
         */
        void chooseCards(List<SortedBag<Card>> options, ActionHandler.ChooseCardsHandler handler);
    }
}
