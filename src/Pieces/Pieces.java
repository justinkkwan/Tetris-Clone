package Pieces;

import java.awt.*;

/**
 * Created by Justin Kwan on 6/4/2016.
 */
public enum Pieces {
    L(0, Color.ORANGE),
    J(1, Color.BLUE),
    S(2, Color.GREEN),
    Z(3, Color.RED),
    T(4, Color.MAGENTA),
    O(5, Color.YELLOW),
    I(6, Color.CYAN);

    private final int index;
    private final Color color;

    Pieces(int i, Color c){
        this.index=i;
        this.color=c;
    }

    public int getIndex(){
        return index;
    }
    public Color getColor(){
        return color;
    }
}