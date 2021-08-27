package ch.epfl.cs107.play.game.tutos.area.tuto2;

import ch.epfl.cs107.play.game.areagame.actor.Background;

public class Ferme2 extends Tuto2Area {
    @Override




    public String getTitle() {
        return "zelda/Ferme";
    }



    @Override
    protected void createArea() {
        registerActor(new Background((this)));
    }
}



