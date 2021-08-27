package ch.epfl.cs107.play.game.superpacman.area.behavior;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.Wall;
import ch.epfl.cs107.play.game.superpacman.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.Queue;

public class SuperPacmanBehavior extends AreaBehavior
{
    protected AreaGraph graph;
    private boolean random;

    /**
     * Constructor of SuperPacmanBehavior, associates to each cell it's type depending on the color
     *
     * @param window current window
     * @param name of the area
     */
    public SuperPacmanBehavior(Window window, String name)
    {
        super(window, name);
        graph = new AreaGraph();

        for (int x = 0; x < this.getWidth(); x++)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {
                SuperPacmanCellType cellType = SuperPacmanCellType.toType(getRGB(this.getHeight() - 1 - y, x));
                this.setCell(x, y, new SuperPacmanCell(x, y, cellType));
            }
        }
    }

    /**
     * Constructor of SuperPacmanBehavior, for a randomly generated maze.
     * @param window  current window
     * @param name  of the area
     * @param random
     */
    public SuperPacmanBehavior(Window window, String name, Boolean random) {
        super(window, name);
        this.random = random;
        RandomMaze maze = new RandomMaze(getWidth(),getHeight());
        int [][] tab2= maze.getMap();
        for (int x = 0; x < getHeight(); x++) {
            for (int y = 0; y < getWidth(); y++) {
                if (tab2[x][y] != 1) {
                    this.setCell(x, y, new SuperPacmanCell(x, y, SuperPacmanCellType.WALL));
                } else {
                    this.setCell(x, y, new SuperPacmanCell(x, y, SuperPacmanCellType.FREE_WITH_DIAMOND));
                }
            }

        }
    }

    /**
     * Checks if a cell is a wall
     *
     * @param coor coordinates of the cell
     * @return true if the cell is a wall
     */
    public boolean isWall(DiscreteCoordinates coor)
        {
        	return ((SuperPacmanCell) getCell(coor.x,coor.y)).nature==SuperPacmanCellType.WALL;
        }

    /**
     * Gets the neighbouring cells of a main cell to check whether walls are nearby
     *
     * @param coor, the cell around which we want to now the neighbouring cells
     * @param area, the current area
     * @return a boolean 2D tab indicating which cells are walls around  coor
     */
    protected   boolean[][] voisinnage(DiscreteCoordinates coor, Area area)
    {
        boolean [][] voisinnage = new boolean [3][3];

        for (int x=-1; x<=1;x++){
            for (int y= -1; y<=1; y++)
            {
                int voisin_x = coor.x + x;
                int voisin_y = coor.y + y;

                if ((voisin_x>=0&&voisin_x<area.getWidth())&&(voisin_y>=0 && voisin_y<area.getHeight()))
                {
                    if (((SuperPacmanCell) getCell(voisin_x,voisin_y)).nature==SuperPacmanCellType.WALL)
                    {
                        voisinnage[x+1][2-(y+1)] = true;
                    }
                    else
                    {
                        voisinnage[x+1][2-(y+1)] = false;
                    }
                }
            }
        }
        return voisinnage;
    }

    /**
     * Creates a path from position to cible
     * Can return null if no path exists
     *
     * @param position current position of the ghost
     * @param cible wished destination
     * @return Queue<Orientation> with the different orientations to get to the cible
     */
    public Queue<Orientation> getPath (DiscreteCoordinates position , DiscreteCoordinates cible)
    {
        AreaGraph graph = this.graph;
        Queue<Orientation> path = graph.shortestPath(position, cible);

        return path;
    }

    /**
     * Puts the actors in the grid
     *
     * @param area the corresponding area
     */
    protected void registerActors(Area area)
    {
        for (int x = 0; x < area.getWidth(); x++)
        {
            for (int y = 0; y < area.getHeight(); y++)
            {
                SuperPacmanCell cell = ((SuperPacmanCell) getCell(x,y));
                boolean hasLeftEdge = x > 0 && ((SuperPacmanCell) getCell(x-1,y)).nature != SuperPacmanCellType.WALL;
                boolean hasRightEdge = x+1 < area.getWidth() && ((SuperPacmanCell) getCell(x+1,y)).nature != SuperPacmanCellType.WALL;
                boolean hasUpEdge = y+1 < area.getHeight() && ((SuperPacmanCell) getCell(x,y+1)).nature != SuperPacmanCellType.WALL;
                boolean hasDownEdge = y > 0 && ((SuperPacmanCell) getCell(x,y-1)).nature != SuperPacmanCellType.WALL;

                if(cell.nature != SuperPacmanCellType.WALL&& !random)
                {
                    this.graph.addNode(new DiscreteCoordinates(x,y), hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
                }

                if (cell.nature== SuperPacmanCellType.WALL)
                {
                    Wall mur = new Wall(area,new DiscreteCoordinates(x,y),voisinnage(new DiscreteCoordinates(x,y),area));
                    area.registerActor(mur);
                }
                else if (cell.nature==SuperPacmanCellType.FREE_WITH_CHERRY)
                {
                    Cherry cerise = new Cherry(area, Orientation.UP,new DiscreteCoordinates(x,y));
                    area.registerActor(cerise);
                }
                else if (cell.nature==SuperPacmanCellType.FREE_WITH_DIAMOND)
                {
                    Diamond diamond = new Diamond(area,Orientation.UP, new DiscreteCoordinates(x,y));
                    area.registerActor(diamond);
                }
                else if (cell.nature==SuperPacmanCellType.FREE_WITH_BONUS)
                {
                    Coin coin = new Coin(area,Orientation.UP, new DiscreteCoordinates(x,y));
                    area.registerActor(coin);
                }
                else if(cell.nature==SuperPacmanCellType.FREE_WITH_BLINKY)
                {
                    Blinky blinky = new Blinky(area,new DiscreteCoordinates(x,y));
                    area.registerActor(blinky);
                    blinky.setFavouritePosition(new DiscreteCoordinates(x,y));
                }
                else if (cell.nature==SuperPacmanCellType.FREE_WITH_INKY)
                {
                    Inky inky = new Inky(area, new DiscreteCoordinates(x,y));
                    area.registerActor(inky);
                    inky.setFavouritePosition(new DiscreteCoordinates(x,y));
                }
                else if (cell.nature==SuperPacmanCellType.FREE_WITH_PINKY)
                {
                    Pinky pinky = new Pinky(area, new DiscreteCoordinates(x,y));
                    area.registerActor(pinky);
                    pinky.setFavouritePosition(new DiscreteCoordinates(x,y));
                }
            }
        }
    }

    protected AreaGraph getGraph (){
        return graph;
    }


    public class SuperPacmanCell extends Cell
    {
        protected SuperPacmanBehavior.SuperPacmanCellType nature ;

        /**
         * Default Cell constructor
         *
         * @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         */
        public SuperPacmanCell(int x, int y, SuperPacmanBehavior.SuperPacmanCellType nature)
        {
            super(x, y);
            this.nature=nature;
        }

        /**
         * Default acceptInteraction method
         * @param v (AreaInteractionVisitor) : the visitor
         */
        public void acceptInteraction(AreaInteractionVisitor v) {}

        /**
         * Booleans
         * @return
         */

        protected boolean canLeave(Interactable entity) {
            return true;
        }

        protected boolean canEnter(Interactable entity) {
            return !this.hasNonTraversableContent() ;
        }

        public boolean isCellInteractable() {
            return true;
        }

        public boolean isViewInteractable() {
            return true;
        }

    }

    /**
     * New type for the case colors in the grid
     */
    public enum SuperPacmanCellType
    {
        NULL(0), WALL(-16777216), FREE_WITH_DIAMOND(-1), FREE_WITH_BLINKY(-65536), FREE_WITH_PINKY(-157237), FREE_WITH_INKY(-16724737),
        FREE_WITH_CHERRY(-36752), FREE_WITH_BONUS(-16478723), FREE_EMPTY(-6118750);
        final int type;

        SuperPacmanCellType(int type)
        {
            this.type = type;
        }

        /**
         * Tells the kind of a cell if it is in SuperPacmanCellType
         * Or if it is not
         *
         * @param type the int associated with a SuperPacmanCellType
         * @return the corresponding SuperPacmanCellType
         */
        public static SuperPacmanCellType toType(int type)
        {
            SuperPacmanCellType[] value = values();

            for (SuperPacmanCellType b : value)
            {
                if (b.type == type)
                {
                    return b;
                }
            }
            return NULL;
        }

    }

}


