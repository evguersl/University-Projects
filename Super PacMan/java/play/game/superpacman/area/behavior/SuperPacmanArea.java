package ch.epfl.cs107.play.game.superpacman.area.behavior;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.Area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

import java.util.Queue;


public class SuperPacmanArea extends Area implements Logic
{
    public  DiscreteCoordinates PLAYER_SPAWN_POSITION = null;
    protected final float scale =15.f;
    private final int defaultIntensity = 0;

    /**
     * Constructs our area game and associates a superPacmanBehavior
     *
     * @param window current window
     * @param fileSystem current fileSystem
     * @return true or false depending on super.begin
     */
    public boolean begin(Window window, FileSystem fileSystem)
    {
        if (super.begin(window, fileSystem))
        {
            setBehavior(new SuperPacmanBehavior(window, getTitle()));
            return true;
        }
            return false;
    }

    /**
     * Determines whether the ghost is surrounded by gates or not and if he is far from his spawn position
     *
     * @param ghost the ghost in question
     * @return true if surrounded by gates, false if near gates but far from it's spawn position
     */
    public boolean voisinageGate(Ghost ghost)
    {
        if(DiscreteCoordinates.distanceBetween(ghost.getFavouritePosition(),ghost.getCurrentCells().get(0))>3)
        {
            return false;
        }

        for(Actor b : this.getActors())
        {
            if (b instanceof Gate)
            {
                if (DiscreteCoordinates.distanceBetween(((Gate) b).getCurrentCells().get(0),ghost.getCurrentCells().get(0))<4&& !((Gate) b).isOn())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks all the actors
     *
     * @return true if diamonds are still registered
     */
    public boolean diamond ()
    {
        for (Actor b : this.getActors())
        {
            if (b instanceof Diamond)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Sends all ghosts to their spawn position
     */
    public void tousAlaMaison()
    {
        for (Actor b : getActors() )
        {
            if(b instanceof Ghost)
            {
                ((Ghost) b).toFavoritePosition();
            }
        }
    }



    /**
     * Desactivates or activates the nodes located on a gate
     *
     * @param position of the Gate
     * @param sign of the gate
     */
    public void setGraphNode (DiscreteCoordinates position,Signal sign )
    {
        ((SuperPacmanBehavior)getAreaBehavior()).getGraph().setSignal(position,(Logic)sign);
    }

    /**
     *  Calls the method of the super class
     *
     * @param position current position of the ghost
     * @param cible wished destination
     * @return Queue<Orientation> path to the destination
     */
    public Queue<Orientation> getPath (DiscreteCoordinates position , DiscreteCoordinates cible)
    {
        return ((SuperPacmanBehavior)getAreaBehavior()).getPath(position,cible);
    }

    /**
     * Creates an area and registers the actor calling the method
     */
    public void createArea()
    {
        ((SuperPacmanBehavior)this.getAreaBehavior()).registerActors(this);
    }

    /**
     * Getters and booleans
     * @return
     */

    public boolean isOn()
    {
        return !this.diamond();
    }

    public float getCameraScaleFactor()
    {
        return scale;
    }

    public String getTitle()
    {
        return null;
    }

    public boolean isWall(DiscreteCoordinates coor)
    {
        return ((SuperPacmanBehavior)getAreaBehavior()).isWall(coor);
    }

    public boolean isOff()
    {
        return false;
    }

    public float getIntensity()
    {
        return defaultIntensity;
    }

    public DiscreteCoordinates getSp()
    {
        return PLAYER_SPAWN_POSITION;
    }
}
