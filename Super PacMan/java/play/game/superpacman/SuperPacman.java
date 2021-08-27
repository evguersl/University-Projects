package ch.epfl.cs107.play.game.superpacman;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.*;
import ch.epfl.cs107.play.game.superpacman.area.behavior.SuperPacmanArea;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import java.awt.*;

public class SuperPacman extends RPG
{
    private SuperPacmanPlayer player;

    private int timer=750; //duration of the final chance level
    private int needScore =10; //number of ghosts to kill in the final chance level

    private boolean pause =false;
    private boolean isInFinalChance= false;
    private boolean endGame=false;

    /**
     * Creates the desired areas
     */
    public void createAreas()
    {
        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
        addArea(new Level3());
        addArea(new LevelFinalChance());
    }

    /**
     * Draws the pause interface of the game
     * @param S the String you want to draw
     */
    public void drawPauseInterface(String S)
    {
        float width = getWindow().getScaledWidth();
        float height = getWindow().getScaledHeight();
        Vector anchor = getWindow().getTransform().getOrigin().sub(new Vector((float) (width ), height ));
        TextGraphics breakScreen = new TextGraphics(S, (float) 0.8, Color.YELLOW, Color.YELLOW, (float) 0.05, true, false, anchor.add(new Vector(8, height - 1.375f)));
        breakScreen.draw(getWindow());
    }


    /**
     * Changes the current area of the game
     * @param areaName the name of the area you want to set
     */
    private void changeArea(String areaName)
    {
        getCurrentArea().unregisterActor(player);
        setCurrentArea(areaName,true);
        player.enterArea(getCurrentArea(),((SuperPacmanArea) getCurrentArea()).getSp());
        getCurrentArea().setViewCandidate(player);
        player.setCurrent_hp(3);
    }

    /**
     * Method that loads the Final chance mode
     */
    private void loadFinalChance()
    {
        changeArea("superpacman/LevelFinalChance");
        player.setInFinalChance(true);
        player.setVitesseDeplacement(4);
        isInFinalChance = true;
        player.setCurrent_hp(5);
    }

    /**
     * Method that pauses the game
     */
    private void breakInitialiser()
    {
        pause= !pause;
        try
        {
            Thread.sleep(400);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method that handles the break screen depending on endGame variable
     */
    private void breakHandler()
    {
        if (!endGame)
        {
            drawPauseInterface("PRESS SPACE\n TO\n CONTINUE");
        }
        if (endGame)
        {
            drawPauseInterface("YOU SUCK ! PRESS R TO RESTART");
        }
    }

    /**
     * method that resets the game
     */
    private void resetGame()
    {
        changeArea("superpacman/Level0");
        endGame = false;
        pause = false;
        player.resetScore();
        player.setVitesseDeplacement(6);
        createAreas();
    }

    /**
     *Method who deals with the final chance mode, and checks if the player can respawn.
     * @param deltatime
     */
    private void finalChanceHandler(float deltatime)
    {
        timer -= deltatime;

        if ( timer == 0)
        {
            isInFinalChance = false;
            player.setInFinalChance(false);
            timer=500;

            if (player.getFinalChanceScore() >= needScore)
            {
                changeArea("superpacman/Level1");
                player.setCurrent_hp(3);
                player.setVitesseDeplacement(6);
            }
            else
            {
                endGame = true;
                pause = true;
            }
            player.resetFinalChanceScore();
        }
    }

    /**
     * To update our SuperPacman
     * Support the final chance mode
     * Support the endgame and break interface
     * Actualise the state of the ghosts.
     * @param deltatime default update param
     */
    public void update (float deltatime)
    {
        Keyboard keyboard = getWindow().getKeyboard();
        Button keyS= keyboard.get(Keyboard.SPACE);
        Button keyR = keyboard.get(Keyboard.R);

        if (isInFinalChance)
        {
            finalChanceHandler(deltatime);
        }

        if (player.getCurrent_hp()==0)
        {
            loadFinalChance();
        }

        if (keyR.isDown())
        {
            if (endGame)
            {
                resetGame();
                return;
            }
        }

        if (keyS.isDown())
        {
            breakInitialiser();
        }

        if (pause)
        {
            breakHandler();
            return;
        }

        if(Ghost.getScared() && Ghost.getTimer()>0)
        {
            Ghost.setTimer(Ghost.getTimer() - deltatime);
        }
        else if(Ghost.getScared())
        {
            Ghost.setScared(false);
            Ghost.setTimer(Ghost.getMAX_TIMER());
        }

        super.update(deltatime);
    }

    /**
     * To start in a requeired area
     *
     * @param window current window
     * @param fileSystem current fileSystem
     * @return true or false depending on the launch
     */
    public boolean begin (Window window, FileSystem fileSystem)
    {
        if (super.begin(window, fileSystem))
         {
            createAreas() ;
            setCurrentArea("superpacman/Level0",true);
            player= new SuperPacmanPlayer(getCurrentArea(),((SuperPacmanArea)getCurrentArea()).getSp());
            initPlayer(player);
            return true;
         }
        else
        {
            return false;
        }
    }

    /**
     * Getters
     * @return
     */

    public String getTitle()
    {
        return "Super Pac-Man";
    }

}
