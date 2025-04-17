package org.nordea.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.nordea.model.Sentence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVSentenceHandlerTest {

    @TempDir
    Path tempDir;

    @Test
    void testHandleMultipleSentences() throws IOException {
        Path outputFile = tempDir.resolve("output.csv");
        CSVSentenceHandler handler = new CSVSentenceHandler(outputFile.toString());

        Sentence sentence1 = new Sentence(Arrays.asList("hello", "world"));
        Sentence sentence2 = new Sentence(Arrays.asList("testing", "csv", "handler"));

        handler.handleSentence(sentence1);
        handler.handleSentence(sentence2);
        handler.end();

        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(3, lines.size()); // 2 sentences + header
        assertEquals(", Word 1, Word 2, Word 3", lines.get(0));
        assertEquals("Sentence 1, hello, world", lines.get(1)); // first sentence
        assertEquals("Sentence 2, csv, handler, testing", lines.get(2)); // second sentence
    }
}