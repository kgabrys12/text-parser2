package org.nordea.handler;

import org.nordea.exception.CSVException;
import org.nordea.model.Sentence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.StringJoiner;

public class CSVSentenceHandler implements SentenceHandler {
    private final Path finalPath;
    private final Path tempPath;
    private final BufferedWriter tempWriter;
    private int sentenceCount = 0;
    private int maxColumnCount = 0;

    public CSVSentenceHandler(String filePath) throws IOException {
        this.finalPath = Paths.get(filePath);
        this.tempPath = Paths.get(filePath + ".temp");
        this.tempWriter = Files.newBufferedWriter(tempPath, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void start() {
        // Header will be written at the end
    }

    @Override
    public void handleSentence(Sentence sentence) {
        try {
            sentenceCount++;
            int wordCount = sentence.getSortedWords().size();

            // Update max columns if needed
            maxColumnCount = Math.max(wordCount, maxColumnCount);

            // Write the sentence data to temporary file
            StringJoiner line = new StringJoiner(", ");
            line.add("Sentence " + sentenceCount);

            sentence.getSortedWords().forEach(line::add);

            tempWriter.write(line + System.lineSeparator());
            tempWriter.flush();

        } catch (IOException e) {
            throw new CSVException("Error writing to temporary CSV", e);
        }
    }

    @Override
    public void end() {
        try (tempWriter) {
            createFinalFileWithHeader();
            Files.deleteIfExists(tempPath);
        } catch (IOException e) {
            throw new CSVException("Error finalizing CSV file", e);
        }
    }

    private void createFinalFileWithHeader() {
        try (BufferedWriter finalWriter = Files.newBufferedWriter(finalPath);
             BufferedReader tempReader = Files.newBufferedReader(tempPath)) {

            // Write header with final max column count
            StringJoiner header = new StringJoiner(", ");
            header.add(""); // First column is sentence identifier

            for (int i = 1; i <= maxColumnCount; i++) {
                header.add("Word " + i);
            }

            finalWriter.write(header.toString());
            finalWriter.newLine();

            // Copy all lines from temp file to final file
            tempReader.lines().forEach(line -> {
                try {
                    finalWriter.write(line);
                    finalWriter.newLine();
                } catch (IOException e) {
                    throw new CSVException("Error writing final CSV file", e);
                }
            });
        } catch (IOException e) {
            throw new CSVException("Error writing final CSV file", e);
        }
    }
}