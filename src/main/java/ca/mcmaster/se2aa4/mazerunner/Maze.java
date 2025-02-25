package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Maze {
    private static final Logger logger = LogManager.getLogger();
    private char[][] grid;
    private Position entry;
    private Position exit;

    public Maze(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        // read line from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // do not skip blank lines
                lines.add(line);
            }
        }
        
        if (lines.isEmpty()) {
            logger.error("Maze file is empty.");
            throw new IllegalArgumentException("Maze file must not be empty.");
        }
        
        // determine the maximum row lengt
        int maxCols = 0;
        for (String s : lines) {
            if (s.length() > maxCols) {
                maxCols = s.length();
            }
        }
        
        // pad lines with spaces
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            if (s.length() < maxCols) {
                //right-pad with spaces
                lines.set(i, String.format("%-" + maxCols + "s", s));
            }
        }
        
        int rows = lines.size();
        int cols = maxCols;
        grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = lines.get(r).toCharArray();
        }
        
        /**for (int r = 0; r < rows; r++) {
            //System.out.println("Row " + r + ": [" + new String(grid[r]) + "]");
        }**/
        Position westGap = null;
        Position eastGap = null;
        for (int r = 0; r < rows; r++) {
            if (grid[r][0] == ' ') {
                westGap = new Position(r, 0);
                break;
            }
        }
        for (int r = 0; r < rows; r++) {
            if (grid[r][cols - 1] == ' ') {
                eastGap = new Position(r, cols - 1);
                break;
            }
        }
        if (westGap != null && eastGap != null) {
            entry = westGap;
            exit = eastGap;
        } else if (westGap != null) {
            entry = westGap;
            exit = westGap; // fallback if only one gap is found
        } else if (eastGap != null) {
            entry = eastGap;
            exit = eastGap;
        } else {
            logger.error("No entry or exit found in the maze file.");
            throw new IllegalArgumentException("Maze must have an entry and an exit.");
        }
        logger.info("Maze loaded. Entry: " + entry + ", Exit: " + exit);
    }

    public boolean isWall(Position pos) {
        if (!isValid(pos))
            return true;
        return grid[pos.getRow()][pos.getCol()] == '#';
    }
    
    public boolean isValid(Position pos) {
        int r = pos.getRow();
        int c = pos.getCol();
        return r >= 0 && r < grid.length && c >= 0 && c < grid[0].length;
    }
    
    public Position getEntry() {
        return entry;
    }
    
    public Position getExit() {
        return exit;
    }
}
