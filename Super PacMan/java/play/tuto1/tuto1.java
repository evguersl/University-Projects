package ch.epfl.cs107.play.tuto1;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;

import ch.epfl.cs107.play.game.tutos.area.SimpleGhost;

import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;

import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;


public class tuto1 extends AreaGame{
	private SimpleGhost player;

	private void createAreas(){
		addArea(new Ferme());
		addArea(new Village());
		
	}
	public void end () {
		
	}
	@Override
    public void update(float deltatime) {
		super.update(deltatime);
        Keyboard keyboard = getWindow().getKeyboard() ; Button key = keyboard.get(Keyboard.UP) ;
        if(key.isDown()){
            player.moveUp();
        }

        Button keyd = keyboard.get(Keyboard.DOWN) ;
        if(keyd.isDown()){
            player.moveDown();
        }
         Button keyl = keyboard.get(Keyboard.LEFT) ;
        if(keyl.isDown()){
            player.moveLeft();
        }
         Button keyr = keyboard.get(Keyboard.RIGHT) ;
        if(keyr.isDown()){
            player.moveRight();
        }
        if (player.isWeak()){
            switchArea();
        }
	}
	public String getTitle() {
		return "tuto1";
	}
	public void switchArea(){
        if(getCurrentArea() instanceof Village){
            getCurrentArea().unregisterActor(player);
            setCurrentArea("zelda/Ferme",false);
            player.strengthen();
            getCurrentArea().registerActor(player);
            getCurrentArea().setViewCandidate(player);
        }
        else{
            getCurrentArea().unregisterActor(player);
            setCurrentArea("zelda/Village",false);
            player.strengthen();
            getCurrentArea().registerActor(player);
            getCurrentArea().setViewCandidate(player);
        }
    }
	public boolean begin (Window window, FileSystem fileSystem) {
		player = new SimpleGhost(new Vector(18,7),"ghost.1");

        if (super.begin(window, fileSystem)){ // traitement spécifiques à Tuto1
			createAreas();

			Area f= setCurrentArea("zelda/Ferme",true);
            f.registerActor(player);
            f.setViewCandidate(player);
			return true; }
			else return false;

	}

}
