package ch.epfl.tchu.game;

public interface StationConnectivity
{
     /**
      * Check if two stations are connected
      *
      * @param s1 Station from
      * @param s2 Station to
      * @return boolean whether they're connected or not
      */
     boolean connected(Station s1, Station s2) ;
}
