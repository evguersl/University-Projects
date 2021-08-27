
package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.CollectableAreaEntity;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Key extends CollectableAreaEntity implements Logic
{
    private boolean activation = true;
    private final String name = "superpacman/key";
    private final int defaultIntensity = 0;

    /**
     * Default Key constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public Key(Area area, Orientation orientation, DiscreteCoordinates position) 
	{
		super(area, orientation, position);
		setName(name);
        Sprite sprite = new Sprite(getName(),1.f,1.f,this);
        setSprite(sprite);
	}

    /**
     * Default interaction function to interact with other players (the visitors)
     * @param v (AreaInteractionVisitor) : the visitor
     */
    public void acceptInteraction(AreaInteractionVisitor v)
    {
        ((SuperPacmanInteractionVisitor)v).interactWith(this);
    }

    /**
     * getters, setters and booleans
     * return
     */

    public boolean isOn()
    {
        return !activation;
    }

    public void setActivation(boolean newActivation)
    {
	    activation=newActivation;
    }

    public boolean isOff()
    {
        return false;
    }

    public float getIntensity()
    {
        return defaultIntensity;
    }

}
