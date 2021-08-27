package ch.epfl.cs107.play.game.tutos.area.tuto2;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.tuto1.Ferme;
import ch.epfl.cs107.play.tuto1.Village;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.security.Key;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.game.areagame.actor.Orientation.LEFT;
import static ch.epfl.cs107.play.game.areagame.actor.Orientation.RIGHT;

public class GhostPlayer extends MovableAreaEntity {
    private final static int ANIMATION_DURATION = 8;
    private TextGraphics hpText;
    private Sprite representation;
    protected float energie;


    public GhostPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String sprite){
        super(owner,orientation,coordinates);


        representation = new Sprite(sprite, 1, 1.f, this);
        strengthen();
        hpText=new TextGraphics(Integer.toString((int)energie), 0.4f, Color.BLUE);
        this.hpText.setAnchor(new Vector(-0.3f, 0.1f));
        hpText.setParent(this);
    }

    public boolean isWeak() {
        if (energie<=0) {
            return true;
        }
        return false;
    }
    public void strengthen() {
        energie=10;
    }



    public void moveUp(){
        setCurrentPosition(getPosition().add(0.f, (float)0.05));
    }
    public void moveDown(){
        setCurrentPosition(getPosition().add(0.f, -(float)0.05));
    }
    public void moveRight(){
        setCurrentPosition(getPosition().add((float) 0.05, 0.f));
    }
    public void moveLeft(){
        setCurrentPosition(getPosition().add(-(float)0.05, 0.f));
    }
    @Override
    public void draw(Canvas canvas) {
        representation.draw(canvas);


    }
    public void update (float deltaTime) {

        Keyboard keyboard = this.getOwnerArea().getKeyboard();
        Button keyl = keyboard.get(Keyboard.LEFT);
        Button keyd = keyboard.get(Keyboard.RIGHT);
        Button keyu= keyboard.get(Keyboard.UP);
        Button keyc= keyboard.get(Keyboard.DOWN);


        if(keyl.isDown())
        {
            if (this.getOrientation()==LEFT){
                this.move(8);
            }
            else {
                this.orientate(LEFT);
            }
        }
        else if(keyd.isDown())
        {
            if (this.getOrientation()==RIGHT){
                this.move(8);
            }
            else {
                this.orientate(RIGHT);
            }
        }
        else if(keyc.isDown())
        {
            if (this.getOrientation()==Orientation.DOWN)
            {
                this.move(ANIMATION_DURATION);
            }
            else
            {
                this.orientate(Orientation.DOWN);
            }
        }
        else if(keyu.isDown())
        {
            if (this.getOrientation()==Orientation.UP)
            {
                this.move(ANIMATION_DURATION);
            }
            else
            {
                this.orientate(Orientation.UP);
            }
        }

        super.update(deltaTime);
        energie-= deltaTime;
        if (energie<0) {
            energie=0;

        }
        hpText.setText(Integer.toString((int) energie));
    }


    public void enterArea(Area area, DiscreteCoordinates position){

        area.registerActor(this);
        setOwnerArea(area);

        area.setViewCandidate(this);
        this.setCurrentPosition(position.toVector());
        this.resetMotion();


    }





    @Override
    public boolean takeCellSpace() {
        return false;
    }
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return
                Collections.singletonList(getCurrentMainCellCoordinates());
    }
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }
}
