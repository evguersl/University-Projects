package ch.epfl.tchu.game;
import java.util.*;



public final class Trail
{
    private final List<Route> trail;
    private final List<Boolean> sens;
    private final boolean initialSens;

    private Trail(List<Route> routes, boolean initialSens)
    {
        List<Boolean> tempSens = new ArrayList<>();
        this.initialSens = initialSens;

        if (routes.size() != 0)
        {
            boolean previousSens = initialSens;
            Route previousRoute = routes.get(0);
            tempSens.add(initialSens);
            List<Route> r =routes.subList(1, routes.size());

            if (routes.size() > 1)
            {
                for (Route r1 : r)
                {
                    if (previousSens)
                    {
                        previousSens = r1.station1().id() == previousRoute.station2().id();
                    }
                    else
                    {
                        previousSens = r1.station1().id() == previousRoute.station1().id();
                    }
                    previousRoute = r1;
                }
                tempSens.add(previousSens);
            }
        }
        trail = List.copyOf(routes);
        sens = tempSens;
    }

    /**
     * Gives the length of a Trail
     *
     * @return the requested length
     */
    public int length ()
    {
        int length =0;

        for (Route r1: this.trail)
        {
            length+=r1.length();
        }

        return length;
    }

    /**
     * Gives the longest possible Trail with all the Routes that the player owns
     *
     * @param routes a List of the captured Routes by the player
     * @return the longest possible Trail
     */
    public static Trail longest(List<Route> routes)
    {
        List<Trail> cs = new ArrayList<>();

        int max = 0;
        Trail maxTrail = null;

        if (routes.size()==0)
        {
            return new Trail(routes,true);
        }

        for (Route r1 : routes)
        {
            cs.add(new Trail(Collections.singletonList(r1), true));
            cs.add(new Trail(Collections.singletonList(r1), false));

            if(r1.length()>max)
            {
                maxTrail = new Trail(Collections.singletonList(r1), true);
                max = maxTrail.length();
            }
        }

        while (cs.size() != 0)
        {
            List<Trail> csTemp = new ArrayList<>();

            for (Trail c : cs)
            {
                for (Route r1 : routes )
                {
                    if (!c.trail.contains(r1) && canComplete(c,r1))
                    {
                        csTemp.add(allongeTrail(c, r1));

                        if (allongeTrail(c, r1).length()> max)
                        {
                            maxTrail= allongeTrail(c, r1);
                            max = maxTrail.length();
                        }
                    }
                }
            }

            cs=csTemp;
        }
        return maxTrail;
    }

    /**
     * Gives the first Station of the Trail
     *
     * @return the requested Station but null if the Trail size is 0
     */
    public Station station1()
    {
        if(this.trail.size()==0)
        {
            return null;
        }
        else
        {
            return this.sens.get(0) ? this.trail.get(0).station1(): this.trail.get(0).station2();
            
        }
    }

    /**
     * Gives the last Station of the Trail
     *
     * @return the requested Station but null if the Trail size is 0
     */
    public Station station2()
    {
        int trailSize = this.trail.size();

        if (trailSize==0)
        {
            return null;
        }
        else
        {
            return this.sens.get(this.sens.size()-1) ? 
                    this.trail.get(trailSize-1).station2() : this.trail.get(trailSize-1).station1();
        }
    }

    private static Trail allongeTrail (Trail t1, Route r1)
    {
        List<Route> longerRoute = new ArrayList<>(t1.trail);
        longerRoute.add(r1);

        return new Trail(longerRoute,t1.initialSens);
    }

    private static boolean canComplete(Trail t1, Route r1)
    {
        Route lastRoute = t1.trail.get(t1.trail.size()-1);

        if (t1.sens.get(t1.sens.size()-1))
        {
            return lastRoute.station2().id()==(r1.station1().id()) || lastRoute.station2().id()==(r1.station2().id());
        }
        else
        {
            return lastRoute.station1().id()==(r1.station1().id()) || lastRoute.station1().id()==(r1.station2().id());
        }
    }

    /**
     * Displays the Trail
     *
     * @return the String display of the Trail
     */
    public String toString ()
    {
        StringBuilder result= new StringBuilder(" ");
        int trailSize = this.trail.size();

        if (trailSize==0)
        {
            return "Void Trail";
        }

        if (this.sens.get(0))
        {
            result.append(this.trail.get(0).station1());
        }
        else
        {
            result.append(this.trail.get(0).station2());
        }

        for (int i=0; i<this.sens.size(); i++)
        {
            if (this.sens.get(i))
            {
                result.append(" - ")
                        .append(this.trail.get(i).station2().name());
            }
            else
            {
                result.append(" - ")
                        .append(this.trail.get(i).station1().name());
            }
        }

        result.append(" (")
                .append(this.length())
                .append(")");

        return result.toString();
    }


}
