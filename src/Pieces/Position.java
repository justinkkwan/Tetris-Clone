package Pieces;

/**
 * Created by Justin Kwan on 6/7/2016.
 */
public class Position {
    private int x=0;
    private int y=0;


    public Position(int x, int y){
        this.x=x;
        this.y=y;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

// Likely abandoning this approach