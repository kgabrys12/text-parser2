package org.nordea.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.nordea.handler.SentenceHandler;
import org.nordea.model.Sentence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

class TextParserTest {

    @TempDir
    Path tempDir;

    @Test
    void testParseEmptyFile() throws IOException {
        Path inputFile = tempDir.resolve("empty.txt");
        Files.writeString(inputFile, "");

        SentenceHandler mockHandler = mock(SentenceHandler.class);
        TextParser parser = new TextParser();

        parser.parse(inputFile.toString(), mockHandler);

        verify(mockHandler, never()).handleSentence(any(Sentence.class));
        verify(mockHandler).end();
    }

    @Test
    void testParseSimpleSentences() throws IOException {
        Path inputFile = tempDir.resolve("simple.txt");
        Files.writeString(inputFile, "Hello world.\nThis is a test.");

        SentenceHandler mockHandler = mock(SentenceHandler.class);
        TextParser parser = new TextParser();

        parser.parse(inputFile.toString(), mockHandler);

        verify(mockHandler, times(2)).handleSentence(any(Sentence.class));
        verify(mockHandler).end();
    }

    @Test
    void testParseSentenceWithMultiplePunctuation() throws IOException {
        Path inputFile = tempDir.resolve("complex.txt");
        Files.writeString(inputFile, "Hello, \nworld! This: a Dr. Prof. Mr. and Mrs. Ms. Test.");

        SentenceHandler mockHandler = mock(SentenceHandler.class);
        TextParser parser = new TextParser();

        parser.parse(inputFile.toString(), mockHandler);

        verify(mockHandler, times(2)).handleSentence(any(Sentence.class));
        verify(mockHandler).end();
    }
}