package org.nordea.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sentence implements Comparable<Sentence> {
    private final List<String> words;

    public Sentence() {
        this.words = new ArrayList<>();
    }

    public void addWord(String word) {
        words.add(word);
    }

    public List<String> getWords() {
        List<String> sortedWords = new ArrayList<>(words);
        sortedWords.sort((word1, word2) -> {
            int caseInsensitiveComparison = String.CASE_INSENSITIVE_ORDER.compare(word1, word2);
            if (caseInsensitiveComparison == 0) {
                // If words are equal ignoring case, prioritize uppercase
                return word2.compareTo(word1);
            }
            return caseInsensitiveComparison;
        });
        return sortedWords;
    }

    @Override
    public int compareTo(Sentence other) {
        return this.getWords().toString().compareTo(other.getWords().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        return Objects.equals(getWords(), sentence.getWords());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWords());
    }
}