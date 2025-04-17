package org.nordea.handler;

import org.nordea.model.Sentence;

import java.io.IOException;

public interface SentenceHandler {
    void handleSentence(Sentence sentence);
    void start() throws IOException;
    void end();
}