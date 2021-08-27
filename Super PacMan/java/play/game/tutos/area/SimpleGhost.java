package ch.epfl.cs107.play.game.tutos.area;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class SimpleGhost extends Entity {
	private TextGraphics hpText;
	protected Sprite representation;
	protected float energie;
	public boolean isWeak() {
		if (energie<=0) {
			return true;
		}
		return false;
	}
	public void strengthen() {
		energie=10;
	}
	public SimpleGhost(Vector position, String spriteName) {
		super((Vector) position);
		representation = new Sprite(spriteName, 1, 1.f, this);
		strengthen();
		hpText=new TextGraphics(Integer.toString((int)energie), 0.4f, Color.BLUE);
        this.hpText.setAnchor(new Vector(-0.3f, 0.1f));
		hpText.setParent(this);
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
		hpText.draw(canvas);
		representation.draw(canvas);
		// TODO Auto-generated method stub
		
	}
	public void update (float deltaTime) {
		energie-= deltaTime;
		if (energie<0) {
			energie=0;
		}
		hpText.setText(Integer.toString((int) energie));
	}

}
