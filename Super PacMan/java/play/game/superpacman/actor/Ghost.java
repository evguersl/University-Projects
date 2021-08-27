package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Ghost extends Player implements Interactable, Interactor, AreaInteractionVisitor
{
    private Sprite[][] spriteTab;
    private Sprite[] spriteTabScared;
    private Animation anim[];
    public DiscreteCoordinates favouritePosition;
    private Orientation currentOrientation;
    private int currentOrientationInt;

    private final int score=500;
    private final int scaredRepetition = 2; //number of images for scared stage
    private static final int MAX_TIMER =20;
    private static float timer = MAX_TIMER; //used to control the scared stage of the ghosts
    private int compteur = 0; //used to control the repetition of the animation when the ghosts are scared
    private final int defaultNumberOfFrames = 18; //default number of frames
    private int numberOfFrames = defaultNumberOfFrames;

    private static boolean scared = false; //static boolean for all ghosts to determine whether they are scared or not
    private boolean foundPacman = false; //if a ghost found a pacman

    private boolean awayPath = false; //if the ghost is trying to escape a pacman

    /**
     * Default Player constructor
     *
     * @param area        (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates coordinates,String title) {

        super(area, orientation, coordinates);

        //We create the sprites for scared and not scared stages plus an animation
        spriteTab = RPGSprite.extractSprites(title, 2, 1, 1, this, 16, 16, new Orientation[]{Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT});
        spriteTabScared = RPGSprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16);
        anim = RPGSprite.createAnimations(2, spriteTab);
    }

    /**
     * To update our Ghost
     * @param deltatime default update param
     */
    public void update(float deltatime)
    {
        super.update(deltatime);
    }

    /**
     * Draws a ghost whether he is scared or not
     * @param canvas target, not null
     */
    public void draw(Canvas canvas)
    {
        if(getScared())
        {
            getSpriteTabScared()[getCompteur()%getScaredRepetition()].draw(canvas);
            setCompteur();
        }
        else
        {
            getAnim()[getCurrentOrientationInt()].draw(canvas);
        }
    }

    /**
     * Generates random DiscretCoordinates, that are contained within the size of the map
     * @return random DiscretCoordinates
     */
    public DiscreteCoordinates randomDiscretCoordinates()
    {
        DiscreteCoordinates coor;
        int randomX;
        int randomY;

        randomX = RandomGenerator.getInstance().nextInt(this.getOwnerArea().getWidth());
        randomY = RandomGenerator.getInstance().nextInt(this.getOwnerArea().getHeight());

        coor = new DiscreteCoordinates(randomX,randomY);

        return coor;
    }

    /**
     * Sets a ghost to it's spawn position
     */
    public void toFavoritePosition()
    {
       resetMotion();
       List <DiscreteCoordinates> coor = new ArrayList <DiscreteCoordinates>();

       //We review all grid cases to be sure to carefully unregister our ghost
       for(int x=0; x<getOwnerArea().getWidth();x++)
       {
           for (int y= 0; y<getOwnerArea().getHeight(); y++)
           {
               coor.add(new DiscreteCoordinates(x,y));
           }
       }

       //default unregister commands
       getOwnerArea().leaveAreaCells(this,coor);
       this.resetMotion();
       this.setCurrentPosition(new Vector(this.getFavouritePosition().x,this.getFavouritePosition().y));
       getOwnerArea().enterAreaCells(this, Collections.singletonList(this.getFavouritePosition()));

       //important to set the variables of the ghost
       this.setFoundPacman(false);
       this.setTargetPos(getScared(),getFoundPacman(),null);

       this.resetMotion();
    }

    /**
     * Default motion function the make a ghost move
     * @param deltatime param for the update function
     */
    public void motion(float deltatime)
    {
        setCurrentOrientation(this.getNextOrientation());
        setCurrentOrientationInt(getCurrentOrientation().ordinal());
        getAnim()[getCurrentOrientationInt()].update(deltatime);
        this.orientate(getCurrentOrientation());
        this.move(getNumberOfFrames());
        getAnim()[getCurrentOrientationInt()].reset();
    }

    /**
     * Default interaction function to interact with other players (the visitors)
     * @param v (AreaInteractionVisitor) : the visitor
     */
    public void acceptInteraction(AreaInteractionVisitor v)
    {
        ( (SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    /**
     * Each gosht must define how he will interact
     * @param other (Interactable): interactable to interact with, not null
     */
    public abstract void interactWith(Interactable other);

    /**
     * Call of the function in the upper class to use resetMotion() for every ghost we create
     */
    public void resetMotion()
    {
        super.resetMotion();
    }

    /**
     * Default interaction booleans
     * @return the desired boolean
     */

    public boolean wantsCellInteraction()
    {
        return false;
    }

    public boolean takeCellSpace()
    {
        return false;
    }

    public boolean isCellInteractable()
    {
        return true;
    }

    public boolean isViewInteractable()
    {
        return false;
    }

    //end of default interaction booleans

    /**
     * Each ghost must define how he will set a target, ie his new destination
     * @param scared if the ghost is scared or not
     * @param foundPacman if the ghost found the pacman or not
     * @param cible if the ghost found the pacman, the coordinates of the pacman
     */
    public abstract void setTargetPos(boolean scared, boolean foundPacman, DiscreteCoordinates cible);

    /**
     * Get the current position of the CleverGhost
     * @return List<DiscreteCoordinates> of the CleverGhost's position
     */
    public List<DiscreteCoordinates> getCurrentCells()
    {
        List<DiscreteCoordinates> coor = new ArrayList<DiscreteCoordinates>();
        coor.add(this.getCurrentMainCellCoordinates());

        return coor;
    }

    /**
     * Generates a random Orientation
     * @return a random Orientation
     */
    public Orientation getRandomNextOrientation()
    {
        int randomInt = RandomGenerator.getInstance().nextInt(4);
        return Orientation.fromInt(randomInt);
    }

    /**
     * All the getters and setters of ghost variables
     * @return
     */

    public static int getMAX_TIMER()
    {
        return MAX_TIMER;
    }

    public int getScore()
    {
        return score;
    }

    public static float getTimer()
    {
        return Ghost.timer;
    }

    public static void setTimer(float timer)
    {
        Ghost.timer = timer;
    }

    public abstract Orientation getNextOrientation();

    public int getCurrentOrientationInt()
    {
        return currentOrientationInt;
    }

    public void setCurrentOrientationInt(int currentOrientationInt)
    {
        this.currentOrientationInt = currentOrientationInt;
    }

    public Orientation getCurrentOrientation()
    {
        return currentOrientation;
    }

    public void setCurrentOrientation(Orientation currentOrientation)
    {
        this.currentOrientation = currentOrientation;
    }

    public Sprite[] getSpriteTabScared()
    {
        return spriteTabScared;
    }

    public Animation[] getAnim()
    {
        return anim;
    }

    public int getScaredRepetition()
    {
        return this.scaredRepetition;
    }

    public void setCompteur()
    {
        this.compteur++;
    }

    public int getCompteur()
    {
        return this.compteur;
    }

    public static void setScared(boolean newScared)
    {
        scared = newScared;
    }

    public static boolean getScared()
    {
        return scared;
    }

    public DiscreteCoordinates getFavouritePosition()
    {
        return favouritePosition;
    }

    public void setFavouritePosition(DiscreteCoordinates v)
    {
        favouritePosition=v;
    }

    public void setFoundPacman(boolean foundPacman)
    {
        this.foundPacman = foundPacman;
    }

    public boolean getFoundPacman()
    {
        return this.foundPacman;
    }

    public boolean getAwayPath()
    {
        return awayPath;
    }

    public void setAwayPath(boolean awayPath)
    {
        this.awayPath = awayPath;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public void setNumberOfFrames(int numberOfFrames) {
        this.numberOfFrames = numberOfFrames;
    }

    //end of getters and setters

}
