package org.nordea.parser;

import org.nordea.handler.SentenceHandler;
import org.nordea.model.Sentence;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.Reader;
import java.util.Set;
import java.util.regex.Pattern;

public class TextParser {
    // Find words including those with apostrophes and hyphens
    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{M}]+(['-][\\p{L}\\p{M}]+)?");

    // List of common title abbreviations
    private static final Set<String> TITLES = Set.of("Mr", "Mrs", "Ms", "Dr", "Prof");
    private static final String TITLES_REGEX = String.join("|", TITLES);

    // Pattern to detect sentence endings, but excluding when after titles
    private static final Pattern SENTENCE_ENDING = Pattern.compile(
            "([.!?])(?=(\\s+[A-Z]|\\s*$))(?<!\\b(?:" + TITLES_REGEX + ")\\.)");

    // Pattern to check if a word is a title
    private static final Pattern TITLE_PATTERN = Pattern.compile("^(" + TITLES_REGEX + ")$");

    // Buffer for text across line boundaries
    private final StringBuilder textBuffer = new StringBuilder();

    /**
     * Parse a file and process sentences through the handler
     */
    public void parse(String filePath, SentenceHandler handler) throws IOException {
        try (var reader = Files.newBufferedReader(Path.of(filePath))) {
            parse(reader, handler);
        }
    }

    /**
     * Parse from a reader and process sentences through the handler
     */
    public void parse(Reader reader, SentenceHandler handler) throws IOException {
        handler.start();
        var bufferedReader = new BufferedReader(reader);

        bufferedReader.lines().forEach(line -> {
            // Append current line to buffer
            textBuffer.append(line).append(" ");

            // Process any complete sentences in the buffer
            processBuffer(handler, false);
        });

        // Process any remaining text
        if (!textBuffer.isEmpty()) {
            processBuffer(handler, true);
        }

        handler.end();
    }

    /**
     * Process text from the buffer, extracting sentences
     * @param endOfText true if this is the final call (to handle any remaining text as a sentence)
     */
    private void processBuffer(SentenceHandler handler, boolean endOfText) {
        var text = textBuffer.toString();
        var sentenceMatcher = SENTENCE_ENDING.matcher(text);

        int lastPos = 0;
        boolean foundSentence = false;

        while (sentenceMatcher.find()) {
            foundSentence = true;
            // Include the punctuation in the sentence
            var sentenceText = text.substring(lastPos, sentenceMatcher.end()).trim();
            if (!sentenceText.isEmpty()) {
                processSentence(sentenceText, handler);
            }
            lastPos = sentenceMatcher.end();
        }

        if (foundSentence) {
            // Keep only text after the last sentence boundary
            textBuffer.delete(0, lastPos);
        } else if (endOfText && !textBuffer.isEmpty()) {
            // If we're at the end of text and have remaining content, process it
            processSentence(text, handler);
            textBuffer.setLength(0);
        }
    }

    /**
     * Process a single sentence text
     */
    private void processSentence(String sentenceText, SentenceHandler handler) {
        var sentence = new Sentence();

        // Normalize apostrophes
        var normalizedText = sentenceText.replace("’", "'");

        var wordMatcher = WORD_PATTERN.matcher(normalizedText);
        while (wordMatcher.find()) {
            var word = wordMatcher.group();
            // Re-append dots to recognized titles
            if (TITLE_PATTERN.matcher(word).matches()) {
                word += ".";
            }
            sentence.addWord(word);
        }

        if (!sentence.getSortedWords().isEmpty()) {
            handler.handleSentence(sentence);
        }
    }
}