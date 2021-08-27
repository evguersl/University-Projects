package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Objects;

public final class Station
{
   private final int id;
   private final String name;

    /**
     * Station constructor
     *
     * @param id of the station
     * @param name of the station
     * @throws IllegalArgumentException if id>=0
     */
   public Station (int id,String name)
   {
       Preconditions.checkArgument(id>=0);
       this.id=id;
       this.name=name;
   }

    /**
     * Gives the station's id
     *
     * @return this.id
     */
    public int id() {
        return id;
    }

    /**
     * Gives the station's name
     *
     * @return this.name
     */
    public String name() {
        return name;
    }

    /**
     * Give the station's name
     *
     * @return this.name
     */
    public String toString()
    {
       return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return id == station.id && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
