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

public class Pinky extends CleverGhost
{
    private final int MIN_AFRAID_DISTANCE = 5;
    public final int MAX_RANDOM_ATTEMPT = 200;

    /**
     * Default Player constructor
     * @param area        (Area): Owner Area, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public Pinky(Area area, DiscreteCoordinates coordinates)
    {
        super(area, Orientation.RIGHT, coordinates, "superpacman/ghost.pinky");
        setNewValueForTargetPos(((SuperPacmanArea)getOwnerArea()).getSp());
    }

    /**
     * To make our pinky move
     * @param deltatime default update param
     */
    public void update(float deltatime)
    {
        pinkyMotion(deltatime);

        if(!((SuperPacmanArea)this.getOwnerArea()).voisinageGate(this) && getScared())
        {
            motionScared(deltatime);
        }
        super.update(deltatime);
    }

    /**
     * Method to make our pinky move, checks whether a displacement occurs or not
     * Special if when pinky is surrounded by doors, he will move like a blinky
     * @param deltatime default update param
     */
    public void pinkyMotion(float deltatime)
    {
        if (isDisplacementOccurs())
        {
            getAnim()[getCurrentOrientationInt()].update(deltatime);
        }
        else
        {
            if(((SuperPacmanArea)this.getOwnerArea()).voisinageGate(this))
            {
                setCurrentOrientation(this.getRandomNextOrientation());
                motion(deltatime);
            }
            else
            {
                setCurrentOrientation(this.getNextOrientation());
                motion(deltatime);
                setNewPositionWhenReachedTarget();
            }
        }
    }

    /**
     * Method to set a new target position depending on whether pinky is scared or not, or whether inky found the pacman or not
     * @param scared if the ghost is scared or not
     * @param foundPacman if the ghost found the pacman or not
     * @param cible if the ghost found the pacman, the coordinates of the pacman
     */
    public void setTargetPos(boolean scared, boolean foundPacman, DiscreteCoordinates cible)
    {
        if(!scared && foundPacman)
        {
            setNewValueForTargetPos(cible);
        }
        else
        {
            setNewValueForTargetPos(coordinatesDependingOnStatus(scared, foundPacman, cible));
        }
    }

    /**
     * Method to set a new targetPosition that checks three criterias
     *
     * 1) Is the distanceBetween < condition
     *    If within 200 attemps no new valid target position has been set we ignore this condition
     * 2) Is there a wall
     * 3) Is the path null
     *
     * @return the new target position
     */
    public DiscreteCoordinates coordinatesDependingOnStatus(boolean scared, boolean foundPacman, DiscreteCoordinates cible)
    {
        DiscreteCoordinates coor;
        int compteur = 0;
        boolean isSup;
        boolean isWall;
        boolean pathNull;

        do {
            pathNull = false;
            isSup = false;
            coor = this.randomDiscretCoordinates();

            //1)
            if(scared && foundPacman && compteur<MAX_RANDOM_ATTEMPT)
            {
                isSup = MIN_AFRAID_DISTANCE > DiscreteCoordinates.distanceBetween(coor, cible);
                compteur++;
            }

            //2)
            isWall = ((SuperPacmanArea) getOwnerArea()).isWall(coor);

            setNewValueForTargetPos(coor);

            //3)
            if(((SuperPacmanArea) getOwnerArea()).getPath(getCurrentMainCellCoordinates(),getTargetPos())==null)
            {
                pathNull=true;
            }

        }while( isSup || isWall || pathNull );

        return coor;
    }

    /**
     * Interact method of pinky
     * Sets variables and a target position depending on :
     *
     * 1) the scared status
     * 2) if the pinky is surrounded by gates
     * 3) if pinky is on an away path
     * 4) if pinky reached his target
     *
     * @param coor coordinates of the interactor
     */
    protected void interact(DiscreteCoordinates coor)
    {
        boolean nearGate = ((SuperPacmanArea)this.getOwnerArea()).voisinageGate(this);

        if ( !nearGate && this.getScared() && !this.getAwayPath() )
        {
            this.setAwayPath(true);
            this.setFoundPacman(true);
            this.setTargetPos(this.getScared(), this.getFoundPacman(), coor);
        }
        else if(!nearGate && !this.getScared())
        {
            this.setFoundPacman(true);
            this.setTargetPos(this.getScared(), this.getFoundPacman(), coor);
        }
        this.setFoundPacman(false);
    }

    /**
     * Getters
     * @return
     */

    public void acceptInteraction(AreaInteractionVisitor v)
    {
        ( (SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    public boolean wantsCellInteraction()
    {
        return true;
    }

    //end of getters



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
