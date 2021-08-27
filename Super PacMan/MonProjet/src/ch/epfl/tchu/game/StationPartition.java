package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

public final class StationPartition implements StationConnectivity
{
    private final int [] links;

    private StationPartition(int [] links)
    {
        this.links = links;
    }

    @Override
    public boolean connected(Station s1, Station s2)
    {
        int s1Id = s1.id();
        int s2Id = s2.id();
        int linksLength = links.length;
        boolean s1IdOutOfLength = s1Id>=linksLength;
        boolean s2IdOutOfLength = s2Id>=linksLength;

        return s1IdOutOfLength || s2IdOutOfLength ? s1Id==s2Id : links[s1Id]==links[s2Id] ;
    }

    public static final class Builder
    {
        private final int stationCount;
        private final int[] links;

        /**
         * Builder to later create a StationPartition
         * @param stationCount the number of Stations
         * @throws IllegalArgumentException if stationCount is strictly negative
         */
        public Builder(int stationCount)
        {
            Preconditions.checkArgument(stationCount>=0);
            this.stationCount = stationCount;

            this.links = new int[stationCount];

            for(int i=0; i<stationCount; i++)
            {
                links[i] = i;
            }
        }

        /**
         * Connects two stations and we choose s1 as the representant of s2
         *
         * @param s1 the first station to connect
         * @param s2 the second station to connect
         * @return the Builder calling the method
         */
        public Builder connect(Station s1, Station s2)
        {
            links[representative(s2.id())] = representative(s1.id());
            return this;
        }

        /**
         * Creates a StationPartition with the Builder
         * @return the requested StationPartition
         */
        public StationPartition build()
        {
            for(int i=0; i<stationCount; i++)
            {
                int representant = representative(links[i]);
                links[i] = representant;
            }

            return new StationPartition(links);
        }

        private int representative(int id)
        {
            int representant = links[id];

            while(links[representant]!=representant)
            {
                representant = links[representant];
            }
            return representant;
        }
    }

}
