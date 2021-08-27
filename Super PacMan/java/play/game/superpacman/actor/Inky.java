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

public class Inky extends CleverGhost
{
    private final int MAX_DISTANCE_WHEN_SCARED = 5;
    private final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
    private final int scaredSpeed = 7; //frames when scared

    /**
     * Default Player constructor
     * @param area        (Area): Owner Area, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public Inky(Area area,  DiscreteCoordinates coordinates)
    {
        super(area, Orientation.RIGHT, coordinates, "superpacman/ghost.inky");
        setNewValueForTargetPos(((SuperPacmanArea)getOwnerArea()).getSp());
        setScaredSpeed(scaredSpeed);
    }

    /**
     * To make our inky move
     * @param deltatime default update param
     */
    public void update(float deltatime)
    {
        inkyMotion(deltatime);

        if(getScared())
        {
            motionScared(deltatime);
        }

        super.update(deltatime);
    }

    /**
     * Method to make our inky move, checks whether a displacement occurs or not
     * @param deltatime default update param
     */
    public void inkyMotion(float deltatime)
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
     * Method to set a new target position depending on whether inky is scared or not, or whether inky found the pacman or not
     * @param scared if the ghost is scared or not
     * @param foundPacman if the ghost found the pacman or not
     * @param cible if the ghost found the pacman, the coordinates of the pacman
     */
    public void setTargetPos(boolean scared, boolean foundPacman, DiscreteCoordinates cible)
    {
    	if(!scared && !foundPacman)
    	{
            setNewValueForTargetPos(coordinatesWithConditions(MAX_DISTANCE_WHEN_NOT_SCARED));
        }
    	else if(!scared && foundPacman)
        {
            setNewValueForTargetPos(cible);
        }
    	else if(scared)
        {
            setNewValueForTargetPos(coordinatesWithConditions(MAX_DISTANCE_WHEN_SCARED));
        }
    }

    /**
     * Method to set a new targetPosition that checks three criterias
     *
     * 1) Is there a wall
     * 2) Is the distanceBetween > condition
     * 3) Is the path null
     *
     * @param condition MAX_DISTANCE_WHEN_NOT_SCARED or MAX_DISTANCE_WHEN_SCARED
     * @return the new target position
     */
    public DiscreteCoordinates coordinatesWithConditions(int condition)
    {
        DiscreteCoordinates coor;
        boolean isSup;
        boolean isWall;
        boolean pathNull;

        do
        {
            pathNull=false;
            coor = this.randomDiscretCoordinates();

            //1)
            isSup = condition < DiscreteCoordinates.distanceBetween(coor, this.getFavouritePosition());

            //2)
            isWall = ((SuperPacmanArea)getOwnerArea()).isWall(coor);

            setNewValueForTargetPos(coor);

            //3)
            if(((SuperPacmanArea) getOwnerArea()).getPath(getCurrentMainCellCoordinates(),getTargetPos())==null)
            {
                pathNull=true;
            }

        }while( isSup || isWall || pathNull);

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
     * Specific interact method of an inky
     * Sets the needed attributes when scared and not scared
     * Sets a new target position when not scared
     * @param coor coordinates of the interactor
     */
    protected void interact(DiscreteCoordinates coor)
    {
        if(!this.getScared())
        {
            this.setFoundPacman(true);
            this.setTargetPos(this.getScared(), this.getFoundPacman(), coor);
        }

        this.setFoundPacman(false);
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