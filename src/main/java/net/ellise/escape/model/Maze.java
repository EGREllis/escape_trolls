package net.ellise.escape.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class Maze {
    private final Space[][] maze;
    private final int openSpaces;
    private final int exitX;
    private final int exitY;

    private Maze(Space[][] maze, int openSpaces, int exitX, int exitY) {
        this.maze = maze;
        this.openSpaces = openSpaces;
        this.exitX = exitX;
        this.exitY = exitY;
    }

    public int getHeight() {
        return maze.length;
    }

    public int getWidth() {
        return maze[0].length;
    }

    public Space getSpace(int x, int y) {
        return maze[y][x];
    }

    private void setSpace(int x, int y, Space space) {
        maze[y][x] = space;
    }

    private int random(int max) {
        return (int)(Math.random()*max);
    }

    public Player randomlyPlacePlayer() {
        int playerX = -1;
        int playerY = -1;
        int randomOpenIndex = random(openSpaces);
        int index = 0;
        for (int y = 0; y < getHeight() && index <= randomOpenIndex; y++) {
            for (int x = 0; x < getWidth() && index <= randomOpenIndex; x++) {
                if (Space.EMPTY.equals(getSpace(x, y))) {
                    if (index == randomOpenIndex) {
                        playerX = x;
                        playerY = y;
                    }
                    index++;
                }
            }
        }
        Cardinal playerOrientation = Cardinal.values()[random(4)];
        return new Player(playerX, playerY, playerOrientation);
    }

    public static Maze loadMazeFromClassPath(String path) {
        InputStream in = ClassLoader.getSystemResourceAsStream(path);
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        List<Space[]> lines = new LinkedList<Space[]>();
        int exitX = -1;
        int exitY = -1;

        String line;
        int openSpaces = 0;
        try {
            line = read.readLine();
            while (line != null) {
                Space[] data = new Space[line.length()];
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    switch (c) {
                        case '#':
                            data[i] = Space.WALL;
                            break;
                        case ' ':
                            data[i] = Space.EMPTY;
                            openSpaces++;
                            break;
                        case 'X':
                            data[i] = Space.EXIT;
                            exitX = i;
                            exitY = lines.size();
                            break;
                        default:
                            throw new RuntimeException(new ParseException(line, i));
                    }
                }
                lines.add(data);
                line = read.readLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        Space[][] maze = new Space[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            maze[i] = lines.get(i);
        }
        return new Maze(maze, openSpaces, exitX, exitY);
    }

    public boolean isValid(Player player, Move move) {
        return isValidNormalMove(player, move) || isValidBlockMove(player, move);
    }

    private boolean isValidNormalMove(Player player, Move move) {
        boolean result = false;
        switch (move) {
            case LEFT:
            case RIGHT:
                result = true;
                break;
            case FORWARD:
                int nextX = player.getForwardX();
                int nextY = player.getForwardY();
                Space nextSpace = getSpace(nextX, nextY);
                result = Space.EMPTY.equals(nextSpace) || Space.EXIT.equals(nextSpace);
                break;
        }
        return result;
    }

    private boolean isValidBlockMove(Player player, Move move) {
        if (!Move.FORWARD.equals(move)) {
            return false;
        }
        // We are moving forwards
        int nextX = player.getForwardX();
        int nextY = player.getForwardY();
        if (!Space.WALL.equals(getSpace(nextX, nextY))) {
            return false;
        }
        // The block ahead is a wall
        switch (player.getOrientation()) {
            case NORTH:
                if (nextY == 0) {
                    return false;
                }
                break;
            case SOUTH:
                if (nextY == getHeight()-1) {
                    return false;
                }
                break;
            case WEST:
                if (nextX == 0) {
                    return false;
                }
                break;
            case EAST:
                if (nextX == getWidth()-1) {
                    return false;
                }
        }
        // We will not push a block off the board
        int afterX = player.getForwardTwiceX();
        int afterY = player.getForwardTwiceY();
        return Space.EMPTY.equals(getSpace(afterX, afterY));    // The cell after next is empty
    }

    public void move(Player player, Move move) {
        if (isValidNormalMove(player, move)) {
            player.move(move);
        } else {
            int nextX = player.getForwardX();
            int nextY = player.getForwardY();
            int afterX = player.getForwardTwiceX();
            int afterY = player.getForwardTwiceY();
            setSpace(nextX, nextY, Space.EMPTY);
            setSpace(afterX, afterY, Space.WALL);
            player.move(move);
        }
    }

    public boolean isWon(Player player) {
        return player.getX() == exitX && player.getY() == exitY;
    }
}
