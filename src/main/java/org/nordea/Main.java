package org.nordea;

import org.nordea.generator.CSVGenerator;
import org.nordea.generator.XMLGenerator;
import org.nordea.model.Sentence;
import org.nordea.parser.TextParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java org.nordea.Main <input-file>");
            System.exit(1);
        }

        String inputFile = args[0];
        File input = new File(inputFile);
        String baseName = input.getName().substring(0, input.getName().lastIndexOf('.'));

        try (FileReader reader = new FileReader(inputFile)) {
            TextParser parser = new TextParser();
            List<Sentence> sentences = parser.parse(reader);

            // Generate XML and save to file
            XMLGenerator xmlGenerator = new XMLGenerator();
            String xmlOutput = xmlGenerator.generateXML(sentences);
            try (FileWriter xmlWriter = new FileWriter(baseName + ".xml")) {
                xmlWriter.write(xmlOutput);
            }

            // Generate CSV and save to file
            CSVGenerator csvGenerator = new CSVGenerator();
            String csvOutput = csvGenerator.generateCSV(sentences);
            try (FileWriter csvWriter = new FileWriter(baseName + ".csv")) {
                csvWriter.write(csvOutput);
            }

            System.out.println("XML and CSV files have been saved as '" + baseName + ".xml' and '" + baseName + ".csv'.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}