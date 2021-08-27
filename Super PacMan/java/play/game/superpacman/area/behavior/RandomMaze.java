package ch.epfl.cs107.play.game.superpacman.area.behavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RandomMaze
{
    private int [][] map;//The final map,

    /**
     * Constructor for the randomMaze,
     * @param width
     * @param height
     */
    protected RandomMaze(int width, int height)
    {
        boolean [][] tab = new boolean[width][height];
        map = new int[width][height];
        generate(tab,map);
    }

    /**
     *
     * @param tab initial boolean 2 dimension Array
     * @param x the abscissa of the cell
     * @param y the ordinate of the cell
     * @return A list containing the Discrete Coordinates of the neighbour cell
     */
    private List<DiscreteCoordinates> voisin(boolean[][] tab, int x, int y) {
        List<DiscreteCoordinates> voisinage= new ArrayList<DiscreteCoordinates>();
        if(x+2<tab[0].length){
            voisinage.add(new DiscreteCoordinates(x+2,y));
        }
        if(x-2>=0){
            voisinage.add(new DiscreteCoordinates(x-2,y));
        }
        if(y-2>=0){
            voisinage.add(new DiscreteCoordinates(x,y-2));
        }
        if(y+2<tab.length){
            voisinage.add(new DiscreteCoordinates(x,y+2));
        }
        return voisinage;

    }

    /**
     *
     * @param voisn
     * @param tableau
     * @return True if one the cell in the given List was not visited yet
     */
    private boolean visitedVoison(List<DiscreteCoordinates> voisn, boolean[][] tableau)
    {
        for(DiscreteCoordinates b: voisn)
        {
            if(tableau[b.x][b.y]==false){
                return true;
            }
        }
        return false;
    }

    /**
     * Implementation of Randomized depth-first search algorithm
     * @param tableau
     * @param tab2
     */
    private void generate(boolean[][] tableau, int[][] tab2)
    {
        DiscreteCoordinates current = new DiscreteCoordinates(1,1);
        Stack stack = new Stack();
        tableau[1][1]=true;
        tab2[0][0]=0;
        tab2[1][1]=1;
        stack.push(current);
        while (!stack.isEmpty())
        {
            current = (DiscreteCoordinates) stack.pop();
            List<DiscreteCoordinates> voisin = voisin(tableau, current.x, current.y);
            if(visitedVoison(voisin, tableau))
            {
                DiscreteCoordinates coor;
                tab2[current.x][current.y]=1;
                stack.push(current);
                do {
                    int randomInt = RandomGenerator.getInstance().nextInt(voisin.size());
                    coor = voisin.get(randomInt);
                    }
                while(tableau[coor.x][coor.y]!=false);
                tab2[(coor.x+ current.x)/2][(coor.y+ current.y)/2]=1;
                tableau[coor.x][coor.y]=true;
                stack.push(coor);

            }

        }
    }
    /**
     *
     * @return the Maze
     */
    protected int[][] getMap()
    {
        return map;
    }
}
