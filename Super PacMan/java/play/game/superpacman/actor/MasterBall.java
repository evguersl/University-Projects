package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.superpacman.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class MasterBall extends Player
{
    FireBallHandler handler = new FireBallHandler();
    Sprite sprite;
    Orientation direction;
    SuperPacmanPlayer player;

    private float lifeTime = (float) 0.8;

    /**
     * Default Player constructor
     *
     * @param area        (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    protected MasterBall(Area area, Orientation orientation, DiscreteCoordinates coordinates, SuperPacmanPlayer player) {
        super(area, orientation, coordinates);
        direction = player.getOrientation();
        this.player=player;
        sprite =  new Sprite("Masterball",(float)(0.8),(float) 0.8,this);

    }

    /**
     * To update our FireBall
     * @param deltatime default update param
     */
    public void update(float deltatime)
    {
        setLifeTime(deltatime);
        if (lifeTime < 0)
        {
            getOwnerArea().unregisterActor(this);
        }
        this.orientate(direction);
        this.move(1);

        super.update(deltatime);
    }

    /**
     * Draws FireBall
     * @param canvas target, not null
     */
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * defualt interact method with an interactable
     * @param other (Interactable). Not null
     */
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    /**
     * default acceptInteraction method with a visitor
     * @param v (AreaInteractionVisitor) : the visitor
     */
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    /**
     * Class to handle the interactions
     */
    class FireBallHandler implements SuperPacmanInteractionVisitor
    {
        /**
         * Default interact method with a ghost
         * @param ghost the interactor
         */
        public void interactWith(Ghost ghost)
        {
            ghost.toFavoritePosition();
            player.setFinalChanceScore();
        }
    }

    /**
     * Getters, setters and booleans
     * return
     */

    public void setLifeTime(float lifeTime) {
        this.lifeTime -= lifeTime;
    }

    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    public boolean wantsCellInteraction() {
        return true;
    }

    public boolean wantsViewInteraction() {
        return false;
    }

    public boolean takeCellSpace() {
        return false;
    }

    public boolean isCellInteractable() {
        return false;
    }

    public boolean isViewInteractable() {
        return false;
    }
}
