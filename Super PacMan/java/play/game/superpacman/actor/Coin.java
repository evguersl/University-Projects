package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.CollectableAreaEntity;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Coin extends CollectableAreaEntity
{
	private final String name = "superpacman/coin";

	/**
	 * Coin constructor
	 *
	 * @param area        (Area): Owner area. Not null
	 * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
	 * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
	 */
	public Coin(Area area, Orientation orientation, DiscreteCoordinates position) 
	{
		super(area, orientation, position);
		setName(name);
		Sprite[] spriteTab = RPGSprite.extractSprites(getName(), 4, 1, 1, this, 16, 16);
		this.setAnimationSprite(spriteTab);
		this.setAnimationHappening(true);
	}

	/**
	 * Default interaction function to interact with other players (the visitors)
	 * @param v (AreaInteractionVisitor) : the visitor
	 */
    public void acceptInteraction(AreaInteractionVisitor v)
	{
        ((SuperPacmanInteractionVisitor)v).interactWith(this);
    }

}
