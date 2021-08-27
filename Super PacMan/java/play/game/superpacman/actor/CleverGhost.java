package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class CleverGhost extends Ghost
{
    private DiscreteCoordinates targetPos;
    private PacmanHandler handler = new PacmanHandler();

    private final int normalSpeed = 9; //frames when not scared
    private int scaredSpeed = 9; //frames when scared

    /**
     * Constructor of a CleverGhost (Pinky and Inky)
     * @param area (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param coordinates (Coordinates): Initial position, not null
     * @param title name of the image to extract Sprite
     */
    public CleverGhost(Area area, Orientation orientation, DiscreteCoordinates coordinates, String title)
    {
        super(area, orientation, coordinates, title);
        setNumberOfFrames(normalSpeed);
    }

    /**
     * To update our CleverGhost
     * @param deltatime default update param
     */
    public void update(float deltatime)
    {
        super.update(deltatime);
    }

    /**
     * Method to give our inky a first and last target when he is scared
     * @param deltatime default update param
     */
    public void motionScared(float deltatime)
    {
        //we enter the if() only one time after inky has been scared
        //we enter the else if() only one time just before inky is not scared anymore

        //each time we set the necessary variables and give inky a new target position

        if (getTimer() > getMAX_TIMER()-1.5*deltatime)
        {
            setNumberOfFrames(getScaredSpeed());
            setFoundPacman(false);
            this.setTargetPos(this.getScared(), this.getFoundPacman(), null);
        }
        else if (getTimer() < 0)
        {
            setNumberOfFrames(getNormalSpeed());
            setFoundPacman(false);
            this.setTargetPos(false, this.getFoundPacman(), null);
        }
    }

    /**
     * Each CleverGhost must define how he will interact in the interactWith method
     * @param coor coordinates of the interactor
     */
    protected abstract void interact(DiscreteCoordinates coor);

    /**
     * Default interaction function with a visitor
     * @param other (Interactable): interactable to interact with, not null
     */
    public void interactWith(Interactable other)
    {
        other.acceptInteraction(handler);
    }

    /**
     * Default acceptInteraction method of a CleverGhost
     * @param v (AreaInteractionVisitor) : the visitor
     */
    public void acceptInteraction(AreaInteractionVisitor v)
    {
        ( (SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    /**
     * Get cases from a radius of 5 around our ghost
     * @return A List of DiscreteCoordinates of nearby cases from a radius of 5 around our ghost
     */
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> field_of_view = new ArrayList<DiscreteCoordinates>();
        for(int x =-5; x<=5;x++)
        {
            for(int y =-5;y<=5;y++)
            {
                DiscreteCoordinates coor = new DiscreteCoordinates(getCurrentMainCellCoordinates().x+x, getCurrentMainCellCoordinates().y+y );
                field_of_view.add(coor);
            }
        }
        return field_of_view;
    }

    /**
     * We build a path to get the next orientation
     * if the path is null we set a new target position and call again getNextOrientation()
     * @return the next Orientation for the displacement
     */
    public Orientation getNextOrientation()
    {
        Queue<Orientation> path = ((SuperPacmanArea) getOwnerArea()).getPath(getCurrentMainCellCoordinates(),getTargetPos());
        Orientation nextOrientation = null;

        if(path!=null)
        {
            nextOrientation = path.poll();
            return nextOrientation;
        }
        else
        {
            setTargetPos(false, false, null);
            return getNextOrientation();
        }
    }

    /**
     * If no displacement occurs and we have reached our target then we set a new target position
     */
    public void setNewPositionWhenReachedTarget()
    {
        if (DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), getTargetPos()) <=1)
        {
            this.setAwayPath(false);
            this.setTargetPos(this.getScared(), this.getFoundPacman(), null);
        }
    }

    /**
     * Boolean default interaction variables
     * @return the corresponding boolean
     */

    public boolean wantsViewInteraction()
    {
        return true;
    }

    public boolean wantsCellInteraction()
    {
        return false;
    }

    //end of interaction booleans

    /**
     * Handler Class
     */
    class PacmanHandler implements SuperPacmanInteractionVisitor
    {
        public void interactWith(SuperPacmanPlayer superpacmanplayer)
        {
            interact(superpacmanplayer.getCurrentCells().get(0));
        }
    }

    /**
     * getters and setters of CleverGhost variables
     * @return
     */

    public DiscreteCoordinates getTargetPos()
    {
        return this.targetPos;
    }

    public void setNewValueForTargetPos(DiscreteCoordinates targetPos)
    {
        this.targetPos = targetPos;
    }

    public int getNormalSpeed()
    {
        return normalSpeed;
    }

    public int getScaredSpeed() {
        return scaredSpeed;
    }

    public void setScaredSpeed(int scaredSpeed) {
        this.scaredSpeed = scaredSpeed;
    }

    //end of getters and setters

}
