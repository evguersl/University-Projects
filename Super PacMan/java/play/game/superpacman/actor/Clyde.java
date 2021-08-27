package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.LinkedList;
import java.util.Queue;

public class Clyde extends CleverGhost
{
    private final int MIN_AFRAID_DISTANCE = 10;
    private final int MAX_RANDOM_ATTEMPT = 200;

    /**
     * Default Player constructor
     * @param area        (Area): Owner Area, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public Clyde(Area area,  DiscreteCoordinates coordinates)
    {
        super(area, Orientation.RIGHT, coordinates, "superpacman/ghost.clyde");
        setNewValueForTargetPos(((SuperPacmanArea)getOwnerArea()).getSp());
    }

    /**
     * To make our clyde move
     * @param deltatime default update param
     */
    public void update(float deltatime)
    {
        clydeMotion(deltatime);
        super.update(deltatime);
    }

    /**
     * Method to make our clyde move, checks whether a displacement occurs or not
     * @param deltatime default update param
     */
    public void clydeMotion(float deltatime)
    {
        if (isDisplacementOccurs())
        {
            getAnim()[getCurrentOrientationInt()].update(deltatime);
        }
        else
        {
            motion(deltatime);
            setNewPositionWhenReachedTarget();
        }
    }

    /**
     * Method to set a new target position depending on whether clyde found the pacman or not
     * The new target is either away from the pacman if clyde found him or random if clyde didn't find the pacman
     *
     * @param scared if the ghost is scared or not //not used in clyde
     * @param foundPacman if the ghost found the pacman or not
     * @param cible if the ghost found the pacman, the coordinates of the pacman
     */
    public void setTargetPos(boolean scared, boolean foundPacman, DiscreteCoordinates cible)
    {
        randomOrEscapeCoordinates(cible,foundPacman);
    }

    /**
     * Method to set a new targetPosition that checks three criterias
     *
     * 1) Is there a wall
     * 2) Is the condition > distanceBetween
     *    If within 200 attemps no new valid target position has been set we don't use this condition
     * 3) Is the path null
     *
     * @return the new target position
     */
    private DiscreteCoordinates randomOrEscapeCoordinates(DiscreteCoordinates cible, boolean foundPacman)
    {
        DiscreteCoordinates coor;
        boolean isWall;
        boolean isSup = false;
        boolean pathNull;
        int compteur = 0;

        do
        {
            pathNull=false;
            isSup =false;

            coor = this.randomDiscretCoordinates();

            //1)
            isWall = ((SuperPacmanArea)getOwnerArea()).isWall(coor);

            //2)
            if(foundPacman && compteur<MAX_RANDOM_ATTEMPT)
            {
                isSup = MIN_AFRAID_DISTANCE > DiscreteCoordinates.distanceBetween(coor, cible);
                compteur++;
            }

            setNewValueForTargetPos(coor);

            //3)
            if(((SuperPacmanArea) getOwnerArea()).getPath(getCurrentMainCellCoordinates(),getTargetPos())==null)
            {
                pathNull=true;
            }

        }while( isWall || isSup || pathNull);

        return coor;
    }

    /**
     * Default accept interaction with a visitor
     * @param v (AreaInteractionVisitor) : the visitor
     */
    public void acceptInteraction(AreaInteractionVisitor v)
    {
        ( (SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    /**
     * Specific interact method of a clyde
     * Sets a new target position every time we interact with a superpacman
     * This target position is to get away from the pacman
     *
     * @param coor coordinates of the interactor
     */
    protected void interact(DiscreteCoordinates coor)
    {
        if(!getAwayPath())
        {
            this.setFoundPacman(true);
            this.setTargetPos(this.getScared(), this.getFoundPacman(), coor);
            this.setFoundPacman(false);
            this.setAwayPath(true);
        }
    }

    /**
     * Uncomment to draw the path
     *
     */

    /*


    private Graphics graphicPath;

    public void draw(Canvas canvas)
    {
        if(getScared())
        {
            getSpriteTabScared()[getCompteur()%getScaredRepetition()].draw(canvas);
            setCompteur();

            if (graphicPath!=null)
            {
                graphicPath.draw(canvas);
            }
        }
        else
        {
            if (graphicPath!=null)
            {
                graphicPath.draw(canvas);
            }

            getAnim()[getCurrentOrientationInt()].draw(canvas);
        }
    }

    public Orientation getNextOrientation()
    {
        Queue<Orientation> path = ((SuperPacmanArea) getOwnerArea()).getPath(getCurrentMainCellCoordinates(),getTargetPos());
        Orientation nextOrientation = null;

        if(path!=null)
        {
            this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(path));
            nextOrientation = path.poll();
            return nextOrientation;
        }
        else
        {
            setTargetPos(false, false, null);
            return getNextOrientation();
        }
    }

     */

}
