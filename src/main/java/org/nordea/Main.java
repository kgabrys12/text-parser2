package org.nordea;

import org.nordea.handler.CSVSentenceHandler;
import org.nordea.handler.CompositeHandler;
import org.nordea.handler.SentenceHandler;
import org.nordea.handler.XMLSentenceHandler;
import org.nordea.parser.TextParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            log.error("Usage: java org.nordea.Main <input-file>");
            System.exit(1);
        }

        String inputFile = args[0];
        File input = new File(inputFile);
        String baseName = input.getName().substring(0, input.getName().lastIndexOf('.'));

        try {
            List<SentenceHandler> handlers = createHandlers(baseName);
            SentenceHandler compositeHandler = new CompositeHandler(handlers);

            TextParser parser = new TextParser();
            parser.parse(inputFile, compositeHandler);

            log.info("Output files have been saved successfully.");
        } catch (IOException e) {
            log.error("Error processing files", e);
        }
    }

    private static List<SentenceHandler> createHandlers(String baseName) throws IOException {
        List<SentenceHandler> handlers = new ArrayList<>();

        // Add handlers for different output formats
        handlers.add(new XMLSentenceHandler(baseName + ".xml"));
        handlers.add(new CSVSentenceHandler(baseName + ".csv"));

        // Additional handlers can be added here in the future

        return handlers;
    }
}