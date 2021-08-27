package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Attachable;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class SuperPacmanStatusGUI extends ImageGraphics implements Attachable, Positionable {

    /**
     * Constructor of SuperPacmanStatusGUI
     *
     * @param name of the GUI
     * @param width of the GUI
     * @param height of the GUI
     */
    protected SuperPacmanStatusGUI(String name, float width, float height)
    {
        super(name, width, height);
    }

    /**
     * Draw the player's life
     *
     * @param canvas target, not null
     * @param point_vie life points
     * @param maxhp max life points
     */
    public void drawLife (Canvas canvas, int point_vie,int maxhp)
    {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));

        for (int i=0; i<maxhp;i++)
        {
            int m;
            if (i<point_vie)
            {
                m= 0;
            }
            else
            {
                m =64;
            }

            ImageGraphics life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"), 1.f, 1.f, new RegionOfInterest(m, 0, 64, 64), anchor.add(new Vector(i, height - 1.375f)), 1, (float) 1);life.draw(canvas);
        }
    }

}