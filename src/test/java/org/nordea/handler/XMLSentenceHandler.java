package org.nordea.handler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.nordea.model.Sentence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class XMLSentenceHandlerTest {

    @TempDir
    Path tempDir;

    private File outputFile;
    private XMLSentenceHandler handler;

    @BeforeEach
    void setUp() throws IOException {
        outputFile = tempDir.resolve("test-output.xml").toFile();
        handler = new XMLSentenceHandler(outputFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    void testStart() throws IOException {
        handler.start();

        assertTrue(outputFile.exists(), "Output file should be created");
        String content = Files.readString(outputFile.toPath());
        assertTrue(content.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"), "Should contain XML declaration");
        assertTrue(content.contains("<text>"), "Should contain root element");
    }

    @Test
    void testHandleSentence() throws IOException {
        handler.start();

        Sentence sentence1 = new Sentence(Arrays.asList("Hello", "world"));
        handler.handleSentence(sentence1);

        Sentence sentence2 = new Sentence(Arrays.asList("Testing", "XML", "output"));
        handler.handleSentence(sentence2);

        handler.end();

        String content = Files.readString(outputFile.toPath());
        assertTrue(content.contains("<sentence>"), "Should contain sentence tag");
        assertTrue(content.contains("<word>Hello</word>"), "Should contain first word");
        assertTrue(content.contains("<word>world</word>"), "Should contain second word");
        assertTrue(content.contains("<word>Testing</word>"), "Should contain word from second sentence");
        assertTrue(content.contains("<word>XML</word>"), "Should contain word from second sentence");
        assertTrue(content.contains("<word>output</word>"), "Should contain word from second sentence");
    }

    @Test
    void testEnd() throws IOException {
        handler.start();
        handler.end();

        String content = Files.readString(outputFile.toPath());
        assertTrue(content.contains("</text>"), "Should close root element");
    }

    @Test
    void testSentenceEscaping() throws IOException {
        handler.start();

        Sentence sentence = new Sentence(Arrays.asList("<test>", "\"quote\"", "&special;"));
        handler.handleSentence(sentence);

        handler.end();

        String content = Files.readString(outputFile.toPath());
        assertTrue(content.contains("&lt;test&gt;"), "XML special characters should be escaped");
        assertTrue(content.contains("&quot;quote&quot;"), "Quotes should be escaped");
        assertTrue(content.contains("&amp;special;"), "Ampersands should be escaped");
    }

    @Test
    void testEmptySentence() throws IOException {
        handler.start();

        Sentence emptySentence = new Sentence();
        handler.handleSentence(emptySentence);

        handler.end();

        String content = Files.readString(outputFile.toPath());
        assertTrue(content.contains("<sentence>") && content.contains("</sentence>"),
                "Empty sentence should be properly represented");
    }
}