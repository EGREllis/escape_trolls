package net.ellise.escape.model;

public class Player {
    private int x;
    private int y;
    private Cardinal orientation;

    public Player(int x, int y, Cardinal orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cardinal getOrientation() {
        return orientation;
    }

    public int getForwardX() {
        return x + orientation.getDx();
    }

    public int getForwardY() {
        return y + orientation.getDy();
    }

    public void move(Move move) {
        switch (move) {
            case LEFT:
                orientation = orientation.getLeft();
                break;
            case RIGHT:
                orientation = orientation.getRight();
                break;
            case FORWARD:
                x = getForwardX();
                y = getForwardY();
                break;
        }
    }

    public String toString() {
        return orientation+"("+x+","+y+")";
    }
}
