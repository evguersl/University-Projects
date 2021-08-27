package ch.epfl.cs107.play.game.superpacman.actor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Gate extends AreaEntity implements Interactable, Logic
{
    Sprite sprite;
    Logic logic;
    Logic logic2;
    boolean condition;
    private final int defaultIntensity = 0;

    /**
     * First Gate constructor, with only one signal
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param sign signal associated to the gate
     */
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, ch.epfl.cs107.play.signal.Signal sign )
    {
        super(area, orientation, position);
        logic = (Logic) sign;

        RegionOfInterest coordinates= null;
        if (orientation== orientation.UP|| orientation== orientation.DOWN){
            coordinates= new RegionOfInterest(0,0,64,64);
        }
        else {
            coordinates=new RegionOfInterest(0,64,64,64);
        }
        sprite = new Sprite("superpacman/gate",1.f,1.f,this,coordinates);

        ((SuperPacmanArea)getOwnerArea()).setGraphNode(position,this);
    }

    /**
     * Second Gate constructor, with only two signal
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param sign1 signal1 associated to the gate
     * @param sign2 signal2 associated to the gate
     */
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, ch.epfl.cs107.play.signal.Signal sign1,Signal sign2 )
    {
        super(area, orientation, position);
        logic = (Logic) sign1;
        logic2 = (Logic) sign2;

        RegionOfInterest coordinates= null;
        if (orientation== orientation.UP|| orientation== orientation.DOWN){
            coordinates= new RegionOfInterest(0,0,64,64);
        }
        else
        {
            coordinates=new RegionOfInterest(0,64,64,64);
        }

        sprite = new Sprite("superpacman/gate",1.f,1.f,this,coordinates);
        ((SuperPacmanArea)getOwnerArea()).setGraphNode(position,this);
    }

    /**
     * Updates a boolean variable linked to the signals associated to the gate
     * If the signals are off the condition will be true
     *
     * @param deltatime default update param
     */
    public void update (float deltatime)
    {
        if ( ((Logic)logic).isOn() && logic2==null)
        {
            condition = false;
        }
        else if (logic.isOn()&& logic2.isOn())
        {
            condition = false;
        }
        else
        {
            condition = true;
        }
    }

    /**
     * Draws the gate if the signals are off
     * @param canvas target, not null
     */
    public void draw(Canvas canvas)
    {
        if (condition)
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
     * Makes the gate walkable if the signals are off
     * @return corresponding boolean
     */
    public boolean takeCellSpace()
    {
        if(condition)
        {
            return true;
        }
        return false;
    }

    /**
     * The Gate's signal activates when the associated signals are Active
     * @return corresponding boolean
     */
    public boolean isOn()
    {
       if(logic==null)
       {
           if(logic2.isOn())
           {
               return true;
           }
       }
       else if(logic2==null)
       {
           if(logic.isOn())
           {
               return true;
           }
       }
       else if (logic.isOn()&&logic2.isOn())
       {
           return true;
       }

       return false;
    }

    /**
     The Gate's signal activates when the associated signals are Active
     * @return corresponding boolean
     */
    public boolean isOff()
    {
        if (logic.isOn()&&logic2.isOn())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * booleans and getters
     * @return
     */

    public boolean isCellInteractable()
    {
        return false;
    }

    public boolean isViewInteractable()
    {
        return false;
    }

    public float getIntensity()
    {
        return defaultIntensity;
    }

    public float getIntensity(float t)
    {
        return defaultIntensity;
    }
}
