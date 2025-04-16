package org.nordea.generator;

import org.nordea.model.Sentence;

import java.util.List;

public class CSVGenerator {
    public String generateCSV(List<Sentence> sentences) {
        StringBuilder csv = new StringBuilder();

        // Determine the maximum number of words in any sentence
        int maxWords = sentences.stream()
                .mapToInt(sentence -> sentence.getWords().size())
                .max()
                .orElse(0);

        // Generate headers dynamically
        csv.append(",");
        for (int i = 1; i <= maxWords; i++) {
            csv.append(" Word ").append(i);
            if (i < maxWords) {
                csv.append(",");
            }
        }
        csv.append("\n");

        // Generate rows for each sentence
        int sentenceIndex = 1;
        for (Sentence sentence : sentences) {
            csv.append("Sentence ").append(sentenceIndex).append(", ");
            List<String> words = sentence.getWords();
            for (int i = 0; i < words.size(); i++) {
                csv.append(words.get(i));
                if (i < words.size() - 1) {
                    csv.append(", ");
                }
            }
            csv.append("\n");
            sentenceIndex++;
        }

        return csv.toString();
    }
}