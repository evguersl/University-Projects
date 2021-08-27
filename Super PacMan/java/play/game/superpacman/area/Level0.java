package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Level0 extends SuperPacmanArea
{
    private final String name = "superpacman/Level0";
    private final String destination = "superpacman/Level1";

    protected final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(10, 1);

    /**
     * Creates the current area and registers the necessary actors
     */
    public void createArea()
    {
       super.createArea();
       Door porte = new Door(destination, Level1.PLAYER_SPAWN_POSITION, TRUE, this, Orientation.UP, new DiscreteCoordinates(5,9), new DiscreteCoordinates(6,9));
       this.registerActor(porte);
       Key key = new Key(this,Orientation.UP, new DiscreteCoordinates(3,4));
       this.registerActor(key);
       Gate gate = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(5,8),key);
       this.registerActor(gate);
       Gate gate2 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(6,8),key);
       this.registerActor(gate2);
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
            SuperPacmanBehavior behave = new SuperPacmanBehavior(window, getTitle());
            setBehavior(behave);
            createArea();
        }
        return false;
    }

    /**
     * Getters
     * @return
     */

    public DiscreteCoordinates getSp()
    {
        return  PLAYER_SPAWN_POSITION;
    }

    public String getTitle()
    {
        return name;
    }
}
