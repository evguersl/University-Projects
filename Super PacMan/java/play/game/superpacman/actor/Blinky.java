
package ch.epfl.cs107.play.game.superpacman.actor;

import static ch.epfl.cs107.play.game.areagame.actor.Orientation.RIGHT;

import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Blinky extends Ghost
{
    /**
     * Constructor of a Blinky, that is attributed a random position at each update and can't find a pacman
     * @param area (Area): Owner Area, not null
     * @param coordinates Initial position, not null
     */
    public Blinky(Area area, DiscreteCoordinates coordinates) 
    {
        super(area, RIGHT, coordinates,"superpacman/ghost.blinky");
	}

    /**
     * To make our blinky move
     * @param deltatime default update param
     */
    public void update(float deltatime)
    {
        blinkyMotion(deltatime);
        super.update(deltatime);
    }

    /**
     * Motion method, checks whether a displacement occurs or not
     * @param deltatime default update param
     */
    public void blinkyMotion(float deltatime)
    {
        if ( isDisplacementOccurs() )
        {
            getAnim()[getCurrentOrientationInt()].update(deltatime);
        }
        else
        {
            motion(deltatime);
        }
    }

    /**
     * Default acceptInteraction method with a visitor
     * @param v (AreaInteractionVisitor) : the visitor
     */
    public void acceptInteraction(AreaInteractionVisitor v) 
    {
        ( (SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    /**
     * Derfaut interactWith method with an Interactable
     * @param other (Interactable): interactable to interact with, not null
     */
    public void interactWith(Interactable other) {}

    /**
     * Default interaction boolean
     * @return wantsViewInteraction boolean
     */
    public boolean wantsViewInteraction()
    {
        return false;
    }

    /**
     * Method to get the current positon
     * @return List<DiscreteCoordinates> of the current position
     */
	public List<DiscreteCoordinates> getFieldOfViewCells()
    {
		return this.getCurrentCells();
	}

	//method not especially used but must be defined
    public void setTargetPos(boolean scared, boolean foundPacman, DiscreteCoordinates cible) {}

    /**
     * Randomly chosen next Orientation
     * @return a random Orientation
     */
    public Orientation getNextOrientation()
    {
       return super.getRandomNextOrientation();
    }

}
