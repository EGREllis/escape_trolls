package net.ellise.escape.model;

import org.testng.annotations.*;

public class MazeTest {
    private static final String MAVEN_CLASSPATH_FIX = "../classes";

    @Test
    public void when_loadedFromClassPath_then_noExceptionsAndNotEmpty() {
        Maze maze = Maze.loadMazeFromClassPath(MAVEN_CLASSPATH_FIX + "/mazes/maze01.dat");
        assert maze.getWidth() == 73 : maze.getWidth();
        assert maze.getHeight() == 23 : maze.getHeight();
    }
}
