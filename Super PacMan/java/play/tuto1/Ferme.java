package ch.epfl.cs107.play.tuto1;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;

public class Ferme extends SimpleArea{
	public void createArea () {
	    registerActor(new Background((this)));
		
	}
	public String getTitle() {
		return "zelda/Ferme"; }

    @Override
    public float getCameraScaleFactor() {
        return 0;
    }
}
