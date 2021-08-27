package ch.epfl.cs107.play.game.superpacman.area.behavior;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.Wall;
import ch.epfl.cs107.play.game.superpacman.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class FinalChanceBehavior extends SuperPacmanBehavior {
    public FinalChanceBehavior(Window window, String name) {
        super(window, name);
    }
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

                if(cell.nature != SuperPacmanCellType.WALL)
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
                    Clyde clyde = new Clyde(area, new DiscreteCoordinates(x,y));
                    area.registerActor(clyde);
                    clyde.setFavouritePosition(new DiscreteCoordinates(x,y));
                }
                else if (cell.nature==SuperPacmanCellType.FREE_WITH_INKY)
                {
                    Clyde clyde = new Clyde(area, new DiscreteCoordinates(x,y));
                    area.registerActor(clyde);
                    clyde.setFavouritePosition(new DiscreteCoordinates(x,y));
                }
                else if (cell.nature==SuperPacmanCellType.FREE_WITH_PINKY)
                {
                    Clyde clyde = new Clyde(area, new DiscreteCoordinates(x,y));
                    area.registerActor(clyde);
                    clyde.setFavouritePosition(new DiscreteCoordinates(x,y));
                }
            }
        }
    }
}
