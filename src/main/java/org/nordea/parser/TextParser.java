package org.nordea.parser;

import org.nordea.model.Sentence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {
    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{M}]+(['’-][\\p{L}\\p{M}]+)?");
    private static final Pattern SENTENCE_PATTERN = Pattern.compile(
            "(?<!\\b(?:Mr|Mrs|Ms|Dr|Prof)\\.)\\s*(?<=[.!?])\\s+(?=[A-Z])"
    );

    public List<Sentence> parse(Reader reader) throws IOException {
        List<Sentence> sentences = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder text = new StringBuilder();
        int c;

        // Read the entire text
        while ((c = bufferedReader.read()) != -1) {
            text.append((char) c);
        }

        // Split text into potential sentences
        String[] sentenceChunks = SENTENCE_PATTERN.split(text.toString());
        for (String chunk : sentenceChunks) {
            if (!chunk.trim().isEmpty()) {
                processTextChunk(chunk.trim(), sentences);
            }
        }

        return sentences;
    }

    private void processTextChunk(String textChunk, List<Sentence> sentences) {
        Sentence sentence = new Sentence();
        // Replace special apostrophes with normal ones
        String normalizedText = textChunk.replace("’", "'");
        Matcher wordMatcher = WORD_PATTERN.matcher(normalizedText);
        while (wordMatcher.find()) {
            String word = wordMatcher.group();
            // Re-append dots to recognized titles
            if (word.matches("^(Mr|Mrs|Ms|Dr|Prof)$")) {
                word += ".";
            }
            sentence.addWord(word);
        }
        if (!sentence.getWords().isEmpty()) {
            sentences.add(sentence);
        }
    }
}