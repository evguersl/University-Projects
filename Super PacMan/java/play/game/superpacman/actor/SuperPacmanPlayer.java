package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.window.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.game.areagame.actor.Orientation.*;

public class SuperPacmanPlayer extends Player implements Interactor, Interactable
{
    private SuperPacmanPlayerHandler handler = new SuperPacmanPlayerHandler();
    private Orientation desiredOrientation ;
    private Animation currentAnimation;
    private SuperPacmanStatusGUI status;
    private final int MAX_HP = 5;
    private final static int ANIMATION_DURATION = 4;
    private int vitesseDeplacement = 6;
    private int current_hp = 3;
    private int score = 10;
    private int finalChanceScore=0;

    private boolean isInFinalChance=false;

    private Sprite[][] sprites = RPGSprite.extractSprites("superpacman/pacman", 4, 1, 1, this, 64, 64, new Orientation[]{Orientation.DOWN, Orientation.LEFT, Orientation.UP, Orientation.RIGHT});
    private Animation[] animations = Animation.createAnimations(ANIMATION_DURATION / 2, sprites);

    /**
     * SuperPacmanPlayer Constructor
     *
     * @param area (Area): Owner Area, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public SuperPacmanPlayer(Area area, DiscreteCoordinates coordinates)
    {
        super(area, RIGHT, coordinates);
        currentAnimation = animations[UP.ordinal()];
        status = new SuperPacmanStatusGUI("superpacman/lifeDisplay", getOwnerArea().getWidth(), getOwnerArea().getHeight());
    }

    /**
     * Updates the Player by making him move
     *
     * @param deltatime default update param
     */
    public void update(float deltatime)
    {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        Mouse mouse = getOwnerArea().getMouse();
        Button click = mouse.getLeftButton();
        Button keyl = keyboard.get(Keyboard.LEFT);
        Button keyr = keyboard.get(Keyboard.RIGHT);
        Button keyu = keyboard.get(Keyboard.UP);
        Button keyd = keyboard.get(Keyboard.DOWN);

        if (click.isDown()&& isInFinalChance())
        {
            MasterBall ball = new MasterBall(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates(),this);
            getOwnerArea().registerActor(ball);
        }
        if (keyl.isDown())
        {
            desiredOrientation = LEFT;
            currentAnimation = animations[LEFT.ordinal()];
            currentAnimation.reset();
        }
        else if (keyr.isDown())
        {
            desiredOrientation = RIGHT;
            currentAnimation = animations[RIGHT.ordinal()];
            currentAnimation.reset();

        }
        else if (keyu.isDown())
        {
            desiredOrientation = UP;
            currentAnimation = animations[UP.ordinal()];
            currentAnimation.reset();

        }
        else if (keyd.isDown())
        {
            desiredOrientation = DOWN;
            currentAnimation = animations[DOWN.ordinal()];
            currentAnimation.reset();
        }

        if (isDisplacementOccurs())
        {
            currentAnimation.update(deltatime);
        }
        else if ((!isDisplacementOccurs()) && desiredOrientation!= null && getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()))))
        {
            this.orientate(desiredOrientation);
            this.move(vitesseDeplacement);
        }

        super.update(deltatime);
    }

    /**
     * Decreases the life points by 1 if they are above 0
     */
    public void looseLife ()
    {
        current_hp-=1;
        if(current_hp<0)
        {
            current_hp=0;
        }
    }

    /**
     * Draws the player and his life points
     *
     * @param canvas target, not null
     */
    public void draw(Canvas canvas)
    {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        currentAnimation.draw(canvas);
        status.drawLife(canvas, current_hp, MAX_HP);
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector((float) (width / 1.5), height / 2));
        TextGraphics nbr_points;

        if (!isInFinalChance)
        {
             nbr_points = new TextGraphics("Score:" + Integer.toString(score), (float) 1, Color.YELLOW, Color.BLACK, (float) 0.05, true, false, anchor.add(new Vector(10, height - 1.375f)));
        }
        else
        {
             nbr_points = new TextGraphics("Score:" + Integer.toString(getFinalChanceScore()) + "/10",(float) 1, Color.YELLOW, Color.BLACK, (float) 0.05, true, false, anchor.add(new Vector(10, height - 1.375f)));
        }
        nbr_points.draw(canvas);
    }

    /**
     * Default acceptInteraction method
     * @param v (AreaInteractionVisitor) : the visitor
     */
    public void acceptInteraction(AreaInteractionVisitor v)
    {
        ( (SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    /**
     * Brings a pacman to it's spawn position
     * And decreases life
     */
    public void sweetHome()
    {
        getOwnerArea().leaveAreaCells(this, getCurrentCells());
        Vector vectorLevel;
        this.resetMotion();
        vectorLevel=new Vector(((SuperPacmanArea)getOwnerArea()).getSp().x,((SuperPacmanArea)getOwnerArea()).getSp().y);
        this.setCurrentPosition(vectorLevel);
        this.looseLife();
    }

    /**
     * Interact method with an Interactable
     * @param other (Interactable). Not null
     */
    public void interactWith(Interactable other)
    {
        other.acceptInteraction(handler);
    }

    class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor
    {
        /**
         * Passes the Pacman to the next Level
         * @param door (Door), not null
         */
        public void interactWith(Door door)
        {
            setIsPassingADoor(door);
        }

        /**
         * Unregisters a diamond if a pacman eats it
         * @param diamond the concerned diamond
         */
        public void interactWith(Diamond diamond)
        {
            getOwnerArea().unregisterActor(diamond);
            setScore(diamond.getAugScore());
        }

        /**
         * Unregisters a cherry if a pacman eats it
         * @param cherry the concerned cherry
         */
        public void interactWith(Cherry cherry)
        {
            getOwnerArea().unregisterActor(cherry);
            setScore(cherry.getAugScore());
        }

        /**
         * Unregisters a coin if the pacman eats it, the ghosts are now scared
         * @param coin the concerned coin
         */
        public void interactWith(Coin coin)
        {
            getOwnerArea().unregisterActor(coin);
            Ghost.setScared(true);
        }

        /**
         * The player eats a scared ghost
         * otherwise the ghost eats the player
         *
         * @param ghost the concerned ghost
         */
        public void interactWith(Ghost ghost)
        {
            if (ghost.getScared())
            {
                ghost.toFavoritePosition();
                setScore(ghost.getScore());
            }
            else
            {
                ((SuperPacmanArea) getOwnerArea()).tousAlaMaison();
                sweetHome();
            }
        }

        /**
         * Unregisters a key if the pacman eats it, changes the signal to false
         * @param key the concerned key
         */
        public void interactWith(Key key)
        {
            key.setActivation(false);
            getOwnerArea().unregisterActor(key);
        }

    }

    /**
     * Getters, setters and booleans
     * @return
     */

    public List<DiscreteCoordinates> getCurrentCells()
    {
        List<DiscreteCoordinates> coor = new ArrayList<DiscreteCoordinates>();
        coor.add(this.getCurrentMainCellCoordinates());
        return coor;
    }

    public List<DiscreteCoordinates> getFieldOfViewCells()
    {
        return null;
    }

    public boolean wantsCellInteraction()
    {
        return true;
    }

    public boolean wantsViewInteraction()
    {
        return false;
    }

    public boolean takeCellSpace()
    {
        return false;
    }

    public boolean isCellInteractable()
    {
        return false;
    }

    public boolean isViewInteractable()
    {
        return true;
    }

    public void setScore(int valeur)
    {
        this.score += valeur;
    }

    public void resetScore()
    {
        this.score=0;
    }
    public void setVitesseDeplacement(int vitesseDeplacement){
        this.vitesseDeplacement=vitesseDeplacement;
    }
    public void resetFinalChanceScore(){
        finalChanceScore=0;
    }

    public int getCurrent_hp()
    {
        return current_hp;
    }

    public void setCurrent_hp(int Hp)
    {
        this.current_hp=Hp;
    }

    public void setInFinalChance(boolean isInFinalChance)
    {
        this.isInFinalChance=isInFinalChance;
    }

    public boolean isInFinalChance()
    {
        return isInFinalChance;
    }

    public void setFinalChanceScore()
    {
        finalChanceScore+=1;
    }

    public int getFinalChanceScore()
    {
        return finalChanceScore;
    }

}
