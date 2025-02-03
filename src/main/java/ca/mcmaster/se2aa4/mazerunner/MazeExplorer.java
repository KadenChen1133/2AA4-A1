package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MazeExplorer {
    private static final Logger logger = LogManager.getLogger();
    
    private final Maze maze;
    private Position currentPos;
    private Direction currentDir;
    private final StringBuilder path;
    
    public MazeExplorer(Maze maze) {
        this.maze = maze;
        // start at the maze entry
        this.currentPos = maze.getEntry();
        if (maze.getEntry().getCol() == 0) {
            currentDir = Direction.EAST;
        } else {
            currentDir = Direction.WEST;
        }
        path = new StringBuilder();
        logger.info("Starting at " + currentPos + " facing " + currentDir);
    }
    
    private boolean canMove(Direction d) {
        Position newPos = currentPos.move(d);
        return maze.isValid(newPos) && !maze.isWall(newPos);
    }
    
    // right-hand rule to explore the maze
    public String explore() {
        //prevent an infinite loop
        int maxSteps = 100000;
        int steps = 0;
        
        while (!currentPos.equals(maze.getExit())) {
            if (steps++ > maxSteps) {
                logger.error("Exceeded maximum steps without finding the exit.");
                return "NO_PATH_FOUND";
            }
            Direction rightDir = currentDir.turnRight();
            if (canMove(rightDir)) {
                currentDir = rightDir;
                path.append("R");
                moveForward();
            } else if (canMove(currentDir)) {
                moveForward();
            } else {
                currentDir = currentDir.turnLeft();
                path.append("L");
            }
        }
        logger.info("Exit reached at " + currentPos);
        return path.toString();
    }
    
    // moves forward in the current direction
    private void moveForward() {
        if (canMove(currentDir)) {
            currentPos = currentPos.move(currentDir);
            path.append("F");
            logger.debug("Moved to " + currentPos + " facing " + currentDir);
        } else {
            logger.warn("Attempted to move into a wall at " + currentPos.move(currentDir));
        }
    }
}
