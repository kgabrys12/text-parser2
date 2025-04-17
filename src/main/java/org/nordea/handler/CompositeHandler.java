package org.nordea.handler;

import org.nordea.model.Sentence;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public record CompositeHandler(List<SentenceHandler> handlers) implements SentenceHandler {

    public CompositeHandler(List<SentenceHandler> handlers) {
        this.handlers = Objects.requireNonNull(handlers, "Handlers list cannot be null");
    }

    @Override
    public void handleSentence(Sentence sentence) {
        handlers.forEach(handler -> handler.handleSentence(sentence));
    }

    @Override
    public void start() throws IOException {
        for (SentenceHandler handler : handlers) {
            handler.start();
        }
    }

    @Override
    public void end() {
        handlers.forEach(SentenceHandler::end);
    }
}