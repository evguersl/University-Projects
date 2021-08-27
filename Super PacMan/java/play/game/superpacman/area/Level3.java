package ch.epfl.cs107.play.game.superpacman.area;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Level3 extends SuperPacmanArea
{
    private final String name = "superpacman/Level3";
    protected final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(1, 1);

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
            SuperPacmanBehavior behave = new SuperPacmanBehavior(window, getTitle(),true);
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

