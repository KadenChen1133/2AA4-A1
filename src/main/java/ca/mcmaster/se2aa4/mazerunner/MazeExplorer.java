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
        logger.info("Starting exploration at " + currentPos + " facing " + currentDir);
    }

    // returns true if the adjacent cell in direction 'd' is open
    private boolean canMove(Direction d) {
        Position newPos = currentPos.move(d);
        return maze.isValid(newPos) && !maze.isWall(newPos);
    }

    // Implements the right-hand rule to explore the maze
    // the algorithm will try turning right, else, just move forward ...
    public String explore() {
        int maxSteps = 100000;  // avoid infinite loops
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
        // factorize the path before returning
        return factorizePath(path.toString());
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

    // factorizes the path
    private String factorizePath(String canonical) {
        if (canonical.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int count = 1;
        char current = canonical.charAt(0);
        for (int i = 1; i < canonical.length(); i++) {
            char c = canonical.charAt(i);
            if (c == current) {
                count++;
            } else {
                if (count > 1) {
                    sb.append(count).append(current);
                } else {
                    sb.append(current);
                }
                current = c;
                count = 1;
            }
        }
        // append final group
        if (count > 1) {
            sb.append(count).append(current);
        } else {
            sb.append(current);
        }
        return sb.toString();
    }
}
