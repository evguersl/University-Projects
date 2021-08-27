package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.area.behavior.FinalChanceBehavior;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanArea;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class LevelFinalChance extends SuperPacmanArea
{
    private final String name = "superpacman/LevelFinalChance";
    public static final DiscreteCoordinates PLAYER_SPAWN_POSITION= new DiscreteCoordinates(15,6);

    /**
     * Creates the current area and registers the necessary actors
     */
    public void createArea()
        {
            super.createArea();
        }

    /**
    * Launched to set the area
    *
    * @param window current Window
    * @param fileSystem current fileSystem
    * @return false
    */
    public boolean begin(Window window, FileSystem fileSystem)
    {
        if (super.begin(window, fileSystem))
        {
            FinalChanceBehavior behave = new FinalChanceBehavior(window, getTitle());
            setBehavior(behave);
            createArea();
            Gate gate = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(14,3),this);
            this.registerActor(gate);
            Gate gate2 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(15,3),this);
            this.registerActor(gate2);
        }
        return false;
    }

    /**
     * Getters
     * @return
     */

    public DiscreteCoordinates getSp()
        {
            return PLAYER_SPAWN_POSITION;
        }

    public String getTitle()
        {
            return name;
        }
}

