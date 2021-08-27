package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class CollectableAreaEntity extends AreaEntity implements Interactable
{
    private String name;
    private Sprite sprite;
    private Sprite[] spriteTab;
    private int compteur = 0; //timer for the animation
    private boolean animationHappening = false;
    private int augScore = 0;
    private final int repetitionAnimation = 4;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position)
    {
        super(area, orientation, position);
    }

    /**
     * Method to draw the CollectableAreaEntity whether it has an animation or not
     * @param canvas target, not null
     */
    public void draw(Canvas canvas)
    {
    	if(animationHappening)
    	{
            spriteTab[compteur%repetitionAnimation].draw(canvas);
            compteur++;
    	}
    	else
    	{
            sprite.draw(canvas);
    	}
    }

    /**
     * Get the current position
     * @return List<DiscreteCoordinates> of the current position
     */
    public List<DiscreteCoordinates> getCurrentCells()
    {
        ArrayList<DiscreteCoordinates> list=new ArrayList<DiscreteCoordinates>();
        list.add(this.getCurrentMainCellCoordinates());
        return list;
    }

    /**
     * getters, setters and booleans
     * return
     */

    public void setAnimationHappening(boolean result)
    {
        this.animationHappening=result;
    }

    public boolean takeCellSpace()
    {
        return false;
    }

    public boolean isCellInteractable()
    {
        return true ;
    }

    public boolean isViewInteractable()
    {
        return false;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setSprite(Sprite sprite)
    {
        this.sprite = sprite;
    }
    
    public void setAnimationSprite(Sprite[] spriteTab)
    {
    	this.spriteTab = spriteTab;
    }
    
    public void acceptInteraction(AreaInteractionVisitor v)
    {
        ((SuperPacmanInteractionVisitor )v).interactWith(this);
    }

    public int getAugScore() {
        return augScore;
    }

    public void setAugScore(int aug_score) {
        this.augScore = aug_score;
    }
}
