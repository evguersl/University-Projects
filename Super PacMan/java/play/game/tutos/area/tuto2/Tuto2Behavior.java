package ch.epfl.cs107.play.game.tutos.area.tuto2;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;

import ch.epfl.cs107.play.window.Window;

public class Tuto2Behavior extends AreaBehavior {

    public Tuto2Behavior(Window window, String name) {
        super(window, name);
        for (int x=0; x<this.getWidth();x++){
            for (int y= 0;y<this.getHeight();y++){
                Tuto2CellType cellType = Tuto2CellType.toType(getRGB(this.getHeight()-1-y, x));


                this.setCell(x,y,new Tuto2Cell(x,y,cellType));
            }

        }







    }

    public enum Tuto2CellType {
        NULL(0, false),
        IMPASSABLE(-8750470, false),
        INTERACT(-256, true),
        WALL(-16777216, false),
        DOOR(-195580, true),
        WALKABLE(-1, true),;
        final int type;
        final boolean isWalkable;
        Tuto2CellType(int type, boolean isWalkable) {
            this.type = type;
            this.isWalkable = isWalkable;
        }
        public static Tuto2CellType toType (int type){
            Tuto2CellType [] value = values();
            for (Tuto2CellType b : value){
                if (b.type== type){
                    return b;
                }

            }
            return NULL;
        }
        }
}
