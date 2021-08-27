package ch.epfl.tchu.game;

import java.util.List;

public enum PlayerId
{
    PLAYER_1, //id player 1
    PLAYER_2; //id player 2

    public static final List<PlayerId> ALL = List.of(PlayerId.values()); //all of the elements of PlayerId

    public static int COUNT = ALL.size(); //size of ALL

    /**
     *
     * @return PLAYER_1 if PLAYER_2 calls the method and PLAYER_2 if PLAYER_1 calls the method
     */
    public PlayerId next()
    {
        int index = (ALL.indexOf(this)+1)%2;
        return (ALL.get(index));
    }

}
