package org.nordea.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SentenceTest {

    @Test
    void testSpecialSorting() {
        Sentence sentence = new Sentence();
        sentence.addWord("banana");
        sentence.addWord("Apple");
        sentence.addWord("apple");
        sentence.addWord("Banana");

        List<String> sortedWords = sentence.getSortedWords();
        assertEquals(Arrays.asList("apple", "Apple", "banana", "Banana"), sortedWords);
    }

    @Test
    void testCompareTo() {
        Sentence sentence1 = new Sentence(Arrays.asList("a", "b"));
        Sentence sentence2 = new Sentence(Arrays.asList("a", "c"));
        Sentence sentence3 = new Sentence(Arrays.asList("a", "b"));

        assertTrue(sentence1.compareTo(sentence2) < 0);
        assertTrue(sentence2.compareTo(sentence1) > 0);
        assertEquals(0, sentence1.compareTo(sentence3));
    }

    @Test
    void testConstructorWithList() {
        List<String> words = Arrays.asList("hello", "world");
        Sentence sentence = new Sentence(words);

        // Assert defensive copy was made (references are not the same)
        assertNotSame(words, sentence.getWords(), "The words list should be a defensive copy");

        // Additionally verify content is the same
        assertEquals(words, sentence.getWords(), "The content should match");

        // Verify changes so original don't affect sentence
        List<String> mutableWords = new ArrayList<>(words);
        Sentence sentenceWithMutable = new Sentence(mutableWords);
        mutableWords.add("test");

        assertNotEquals(mutableWords, sentenceWithMutable.getWords(), "Changes to original list should not affect the sentence");
    }
}