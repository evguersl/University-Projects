package ch.epfl.cs107.play.game.tutos.area.tuto2;




import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.area.SimpleGhost;
import ch.epfl.cs107.play.io.FileSystem;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import ch.epfl.cs107.play.game.areagame.AreaGame;

import ch.epfl.cs107.play.math.Vector;




import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme2;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Villaget;

public class tuto2  extends AreaGame {

    private GhostPlayer player;
    public String getTitle() {
        return "tuto2";
    }
    private final static float scale =13.f;
    private void createAreas(){

        addArea(new Ferme2());

        addArea(new Villaget());

    }
    public void end () {

    }
    @Override
    public void update(float deltatime) {

        if (player.isWeak()){
            switchArea();
        }
        super.update(deltatime);


    }
    public void switchArea(){
        if(getCurrentArea() instanceof Villaget){
            getCurrentArea().unregisterActor(player);
            player.enterArea(setCurrentArea("zelda/Ferme",true), new DiscreteCoordinates(2,10));
            player.strengthen();
            getCurrentArea().registerActor(player);
            getCurrentArea().setViewCandidate(player);
        }
        else{
            getCurrentArea().unregisterActor(player);
            player.enterArea(setCurrentArea("zelda/Village",true), new DiscreteCoordinates(8,15));
            player.strengthen();
            getCurrentArea().registerActor(player);
            getCurrentArea().setViewCandidate(player);
        }
    }
    public boolean begin (Window window, FileSystem fileSystem) {


        if (super.begin(window, fileSystem)){
           createAreas() ;
           setCurrentArea("zelda/Ferme",true);
           player = new GhostPlayer(getCurrentArea(), Orientation.LEFT, new DiscreteCoordinates(2,10), "ghost.1");
           this.getCurrentArea().registerActor(player);
           this.getCurrentArea().setViewCandidate(player);
            return true; }
        else return false;

    }

}
