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

public class Level2 extends SuperPacmanArea
{
    private final String name = "superpacman/Level2";

    protected final static DiscreteCoordinates PLAYER_SPAWN_POSITION= new DiscreteCoordinates(15,29);

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
            Key key1 = new Key(this, Orientation.UP, new DiscreteCoordinates(3,16));
            this.registerActor(key1);
            Key key2 = new Key(this,Orientation.UP, new DiscreteCoordinates(26,16));
            this.registerActor(key2);
            Key key3 = new Key(this,Orientation.UP, new DiscreteCoordinates(2,8));
            this.registerActor(key3);
            Key key4 = new Key(this,Orientation.UP, new DiscreteCoordinates(27,8));
            this.registerActor(key4);
            Gate gate = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(8,14),key1);
            this.registerActor(gate);
            Gate gate1 = new Gate(this,Orientation.DOWN,new DiscreteCoordinates(5,12),key1);
            this.registerActor(gate1);
            Gate gate2 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(8,10),key1);
            this.registerActor(gate2);
            Gate gate3 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(8,8),key1);
            this.registerActor(gate3);
            Gate gate4 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(21,14),key2);
            this.registerActor(gate4);
            Gate gate5 = new Gate(this,Orientation.DOWN,new DiscreteCoordinates(24,12),key2);
            this.registerActor(gate5);
            Gate gate6 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(21,10),key2);
            this.registerActor(gate6);
            Gate gate7 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(21,8),key2);
            this.registerActor(gate7);
            Gate gate8 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(10,2),key3,key4);
            this.registerActor(gate8);
            Gate gate9= new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(19,2),key3,key4);
            this.registerActor(gate9);
            Gate gate10 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(12,8),key3,key4);
            this.registerActor(gate10);
            Gate gate11 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(17,8),key3,key4);
            this.registerActor(gate11);

            Gate gate13 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(14,3),this);
            this.registerActor(gate13);
            Gate gate14 = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(15,3),this);
            this.registerActor(gate14);
            Door porte =new Door("superpacman/Level3", Level3.PLAYER_SPAWN_POSITION, TRUE,
                    this, Orientation.UP, new DiscreteCoordinates(15,0));
            this.registerActor(porte);
            Door porte2 =new Door("superpacman/Level3", Level3.PLAYER_SPAWN_POSITION, TRUE,
                    this, Orientation.UP, new DiscreteCoordinates(14,0));
            this.registerActor(porte2);
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
