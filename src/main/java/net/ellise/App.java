package net.ellise;

import net.ellise.escape.model.Move;
import net.ellise.escape.model.Maze;
import net.ellise.escape.model.Player;
import net.ellise.escape.view.cli.Terminal;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Maze maze = Maze.loadMazeFromClassPath("mazes/maze02.dat");
        Terminal terminal = new Terminal();
        Player player = maze.randomlyPlacePlayer();
        while ( ! maze.isWon(player) ) {
            terminal.clearScreen();
            terminal.drawMaze(maze, player);
            Move move = terminal.getCommand();
            if (maze.isValid(player, move)) {
                player.move(move);
            }
        }
        System.out.println("Congratulations, you won!");
    }
}
