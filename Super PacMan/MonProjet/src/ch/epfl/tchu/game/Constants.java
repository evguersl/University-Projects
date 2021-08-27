package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;

public final class Constants
{
    private Constants() {}



    /**
     * Nombre de cartes wagon de chaque couleur.
     */
    public static final int CAR_CARDS_COUNT = 12;

    /**
     * Nombre de cartes locomotive.
     */
    public static final int LOCOMOTIVE_CARDS_COUNT = 14;

    /**
     * Nombre total de cartes wagon/locomotive.
     */
    public static final int TOTAL_CARDS_COUNT =
            LOCOMOTIVE_CARDS_COUNT + CAR_CARDS_COUNT * Color.COUNT;

    /**
     * Ensemble de toutes les cartes (110 au total).
     */
    public static final SortedBag<Card> ALL_CARDS = computeAllCards();

    private static SortedBag<Card> computeAllCards() {
        var cardsBuilder = new SortedBag.Builder<Card>();
        cardsBuilder.add(LOCOMOTIVE_CARDS_COUNT, Card.LOCOMOTIVE);
        for (Card card : Card.CARS)
            cardsBuilder.add(CAR_CARDS_COUNT, card);
        assert cardsBuilder.size() == TOTAL_CARDS_COUNT;
        return cardsBuilder.build();
    }

    /**
     * Numéro d'emplacement fictif désignant la pioche de cartes.
     */
    public static final int DECK_SLOT = -1;

    /**
     * Liste de tous les numéros d'emplacements de cartes face visible.
     */
    public static final List<Integer> FACE_UP_CARD_SLOTS = List.of(0, 1, 2, 3, 4);


    /**
     * Nombre d'emplacements pour les cartes face visible.
     */
     public static final int FACE_UP_CARDS_COUNT = FACE_UP_CARD_SLOTS.size();

    /**
     * Nombre de billets distribués à chaque joueur en début de partie.
     */
    public static final int INITIAL_TICKETS_COUNT = 5;


    /**
     * Nombre de cartes distribuées à chaque joueur en début de partie.
     */
    public static final int INITIAL_CARDS_COUNT = 4;

    /**
     * Nombre de wagons dont dispose chaque joueur en début de partie.
     */
    public static final int INITIAL_CAR_COUNT = 40;

    /**
     * Nombre de billets tirés à la fois en cours de partie.
     */
    public static final int IN_GAME_TICKETS_COUNT = 3;

    /**
     * Nombre maximum de billets qu'un joueur peut défausser lors d'un tirage.
     */
    public static final int DISCARDABLE_TICKETS_COUNT = 2;

    /**
     * Nombre de cartes à tirer lors de la construction d'un tunnel.
     */
    public static final int ADDITIONAL_TUNNEL_CARDS = 3;

    /**
     * Nombre de points obtenus pour la construction de routes de longueur 1 à 6.
     * (L'élément à l'index i correspond à une longueur de route i. Une valeur
     * invalide est placée à l'index 0, car les routes de longueur 0 n'existent pas).
     */
    public static final List<Integer> ROUTE_CLAIM_POINTS =
            List.of(Integer.MIN_VALUE, 1, 2, 4, 7, 10, 15);

    /**
     * Longueur minimum d'une route.
     */
    public static final int MIN_ROUTE_LENGTH = 1;

    /**
     * Longueur maximum d'une route.
     */
    public static final int MAX_ROUTE_LENGTH = ROUTE_CLAIM_POINTS.size() - 1;

    /**
     * Nombre de points bonus obtenus par le(s) joueur(s) disposant du plus long chemin.
     */

    public static final int LONGEST_TRAIL_BONUS_POINTS = 10;
    /**
     * Largeur rectangle route
     */
    public static final double RECTANGLE_WIDTH_ROAD = 36;
    /**
     * hauteur des rectangle route
     */
    public static final double RECTANGLE_HIGH_ROAD = 12;
    /**
     * largeur des rectangle des cartes exterieur
     */
    public static final double RECTANGLE_WIDTH_DECK_OUTSIDE = 60;
    /**
     * hauteur des rectangle des cartes exterieur
     */

    public static final double RECTANGLE_HIGH_DECK_OUTSIDE = 90;

    /**
     * largeur des rectangles des cartes intérieur
     */
    public static final double RECTANGLE_WIDTH_DECK_INSIDE = 40;
    /**
     * hauteur des rectangles des cartes intérieur
     */

    public static final double RECTANGLE_HIGH_DECK_INSIDE = 70;
    /**
     * nombres maximum d'info affiché
     */
    public static final int MAXIMUM_INFO_DISPLAYED =5;

    /**
     * rayon d'un cercle à l'interieur des cases de route
      */
    public static final double CIRCLE_RADIUS_CASE = 3;

    /**
     * rayon du cercle représentant les joueurs
     */
    public static final double CIRCLE_RADIUS_PLAYER = 5;

    /**
     * Largeur d'un bouton dans les fenetres pop up
     */

    public static final double RECTANGLE_WIDTH_BUTTON_POP_UP = 50;

    /**
     * hauteur d'un bouton dans les fenetres pop up
     */

    public static final double RECTANGLE_HIGH_BUTTON_POP_UP = 5;

    /**
     * Hauteur d'une jauge
     */

    public static final double RECTANGLE_HIGH_GAUGE = 5;


    /**
     * coordonnés du premier cercle à l'interieur d'une case de route (x,y)
     */
    public static final List<Double> FIRST_CIRCLE_COORDINATES = List.of(12.d,6.d);
    /**
     * coordonnés du premier cercle à l'interieur d'une case de route (x,y)
     */
    public static final List<Double> SECOND_CIRCLE_COORDINATES = List.of(24.d,6.d);


    /**
     * Nombre de points bonus obtenus par le(s) joueur(s) disposant du temps le plus court.
     */
    public static final int TIME_BONUS_POINTS = 20;

    /**
     * Rayon du cercle utilisé pour mettre en evidence les Stations
     */

    /**
     * Rayon des cercles utilisés pour mettre en evidence les gares
     */
    public static final int CIRCLE_RADIUS_STATION = 20;

    /**
     * Liste des coordonnées des face up card sur la scene principale
     */
    public static final List<Double> CENTER_COORDINATES_Y = List.of(175.d,275.d,375.d,475.d,575.d,775.d);

    /**
     * Liste des coordonnées des cartes dans la handView du joueur dans l'ordre du type enum Card
     */
    public static final List<Double> CENTER_COORDINATES_X = List.of(480.d,550.d,620.d,690.d,760.d,830.d,900.d,970.d,1040.d);

    /**
     * Coordonnée X fixe centale des faceUpCard
     */

    public static final int FACE_UP_CARD_X = 1394;

    /**
     * Coordonnée Y fixe centrale du handView
     */

    public static final int HAND_VIEW_Y = 790;

    public static final double ANIMATION_LENGTH = 0.7;




}