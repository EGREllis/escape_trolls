package net.ellise.escape.view.cli;

import net.ellise.escape.model.Move;
import net.ellise.escape.model.Maze;
import net.ellise.escape.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Terminal {
    private final BufferedReader in;
    private final PrintStream out;

    public Terminal() {
        this(new BufferedReader(new InputStreamReader(System.in)), System.out);
    }

    public Terminal(BufferedReader in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    public void drawMaze(Maze maze, Player player) {
        for (int height = 0; height < maze.getHeight(); height++) {
            StringBuilder line = new StringBuilder();
            for (int width = 0; width < maze.getWidth(); width++) {
                char display;
                switch (maze.getSpace(width,height)) {
                    case EXIT:
                        display = 'X';
                        break;
                    case WALL:
                        display = '#';
                        break;
                    case EMPTY:
                        display = ' ';
                        break;
                    default:
                        throw new IllegalStateException("Should never get here!");
                }
                if (height == player.getY() && width == player.getX()) {
                    switch (player.getOrientation()) {
                        case NORTH:
                            display = '^';
                            break;
                        case SOUTH:
                            display = 'v';
                            break;
                        case WEST:
                            display = '<';
                            break;
                        case EAST:
                            display = '>';
                            break;
                        default:
                            throw new IllegalStateException("Should never get here!");
                    }
                }
                line.append(display);
            }
            out.println(line);
        }
    }

    public Move getCommand() {
        Move move = null;
        try {
            while (move == null) {
                String line;
                do {
                    line = in.readLine();
                } while (line != null && line.length() <1);
                char input = line.charAt(0);

                switch (input) {
                    case 'q':
                        move = Move.LEFT;
                        break;
                    case 'w':
                        move = Move.FORWARD;
                        break;
                    case 'e':
                        move = Move.RIGHT;
                        break;
                    case ' ':
                        move = Move.NOTHING;
                        break;
                    case 'x':
                        System.exit(0);
                    default:
                        System.err.println("Commands:\n\tq - turn left\n\tw - move forward\n\te - turn right\n\t' ' - do nothing\n\tx - quit\n");
                }
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
        return move;
    }

    public void clearScreen() {
        out.print("\033[H\033[2J");
        out.flush();
    }
}