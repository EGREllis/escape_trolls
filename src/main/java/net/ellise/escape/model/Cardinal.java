package net.ellise.escape.model;

public enum Cardinal {
    NORTH(0,-1, "WEST", "EAST"),
    SOUTH(0,1,"EAST", "WEST"),
    EAST(1,0,"NORTH","SOUTH"),
    WEST(-1,0,"SOUTH","NORTH");

    Cardinal(int dx, int dy, String left, String right) {
        this.dx = dx;
        this.dy = dy;
        this.left = left;
        this.right = right;
    }

    int dx;
    int dy;
    String left;
    String right;

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public Cardinal getLeft() {
        return Cardinal.valueOf(left);
    }

    public Cardinal getRight() {
        return Cardinal.valueOf(right);
    }
}
