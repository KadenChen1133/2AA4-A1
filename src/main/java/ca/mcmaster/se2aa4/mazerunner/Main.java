package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.cli.*;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        // Define the command line options
        Options options = new Options();
        Option inputOpt = Option.builder("i")
                .longOpt("input")
                .hasArg()
                .argName("maze-file")
                .desc("Maze file path")
                .required()
                .build();
        options.addOption(inputOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Error parsing command line arguments: " + e.getMessage());
            
            return;
        }

        String mazeFile = cmd.getOptionValue("i");
        logger.info("** Starting Maze Runner");
        logger.info("**** Reading the maze from file: " + mazeFile);

        try (BufferedReader reader = new BufferedReader(new FileReader(mazeFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                for (int idx = 0; idx < line.length(); idx++) {
                    if (line.charAt(idx) == '#') {
                        sb.append("WALL ");
                    } else if (line.charAt(idx) == ' ') {
                        sb.append("PASS ");
                    }
                }
                
                logger.trace(sb.toString());
            }
        } catch (Exception e) {
            logger.error("/!\\ An error has occurred while reading the maze /!\\", e);
        }

        logger.info("**** Computing path");
        logger.info("PATH NOT COMPUTED");
        logger.info("** End of MazeRunner");
    }
}
