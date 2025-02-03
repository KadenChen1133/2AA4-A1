package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger();
    
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("i")
                .longOpt("input")
                .hasArg()
                .argName("maze-file")
                .desc("Path to the maze file")
                .required()
                .build());
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Failed to parse command line: " + e.getMessage());
            return;
        }
        
        String mazeFile = cmd.getOptionValue("i");
        logger.info("Loading maze from file: " + mazeFile);
        try {
            Maze maze = new Maze(mazeFile);
            MazeExplorer explorer = new MazeExplorer(maze);
            String computedPath = explorer.explore();
            System.out.println("Computed path: " + computedPath);
        } catch (Exception e) {
            logger.error("An error occurred while processing the maze.", e);
        }
    }
}
