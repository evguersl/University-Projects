package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.*;

public interface SuperPacmanInteractionVisitor extends RPGInteractionVisitor
{
    default void interactWith(SuperPacmanPlayer other){}

    default void interactWith(Diamond diamond) {}

    default void interactWith(Cherry cherry) {}

    default void interactWith(Coin coin) {}

    default void interactWith(Key key) {}

    default void interactWith(Ghost ghost){}
}
