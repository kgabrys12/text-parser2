package org.nordea.handler;

import org.junit.jupiter.api.Test;
import org.nordea.model.Sentence;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CompositeHandlerTest {

    @Test
    void testStartDelegation() throws IOException {
        SentenceHandler handler1 = mock(SentenceHandler.class);
        SentenceHandler handler2 = mock(SentenceHandler.class);
        List<SentenceHandler> handlers = Arrays.asList(handler1, handler2);

        CompositeHandler compositeHandler = new CompositeHandler(handlers);

        compositeHandler.start();

        verify(handler1).start();
        verify(handler2).start();
    }

    @Test
    void testHandleSentenceDelegation() {
        SentenceHandler handler1 = mock(SentenceHandler.class);
        SentenceHandler handler2 = mock(SentenceHandler.class);
        List<SentenceHandler> handlers = Arrays.asList(handler1, handler2);

        CompositeHandler compositeHandler = new CompositeHandler(handlers);
        Sentence sentence = new Sentence(Arrays.asList("test", "sentence"));

        compositeHandler.handleSentence(sentence);

        verify(handler1).handleSentence(sentence);
        verify(handler2).handleSentence(sentence);
    }

    @Test
    void testEndDelegation() {
        SentenceHandler handler1 = mock(SentenceHandler.class);
        SentenceHandler handler2 = mock(SentenceHandler.class);
        List<SentenceHandler> handlers = Arrays.asList(handler1, handler2);

        CompositeHandler compositeHandler = new CompositeHandler(handlers);

        compositeHandler.end();

        verify(handler1).end();
        verify(handler2).end();
    }

    @Test
    void testEmptyHandlersList() {
        CompositeHandler compositeHandler = new CompositeHandler(List.of());
        Sentence sentence = new Sentence();

        assertDoesNotThrow(() -> compositeHandler.handleSentence(sentence));
        assertDoesNotThrow(compositeHandler::start);
        assertDoesNotThrow(compositeHandler::end);
    }
}