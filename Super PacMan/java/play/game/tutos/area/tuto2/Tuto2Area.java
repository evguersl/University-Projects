package ch.epfl.cs107.play.game.tutos.area.tuto2;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Tuto2Behavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;


public abstract  class Tuto2Area extends Area {


    protected abstract void createArea();
    /**
     * Create the area by adding all its actors
     * called by the begin method, when the area starts to play
     */



    public float getCameraScaleFactor() {
        return 10.f;
    }


    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)){
            setBehavior(new Tuto2Behavior(window, getTitle()));
            createArea();

        }
        return false;
    }
}