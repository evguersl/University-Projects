package ch.epfl.cs107.play.game.tutos.area.tuto2;


import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;

import ch.epfl.cs107.play.math.Vector;

public class Villaget extends Tuto2Area {


    @Override
    protected  void createArea() {

        registerActor(new Background(this));
        registerActor(new Foreground((this)));
    }

    @Override
    public String getTitle() {
        return "zelda/Village";
    }



}


