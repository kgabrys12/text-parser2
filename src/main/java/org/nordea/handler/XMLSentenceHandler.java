package org.nordea.handler;

import org.apache.commons.text.StringEscapeUtils;
import org.nordea.exception.XMLException;
import org.nordea.model.Sentence;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class XMLSentenceHandler implements SentenceHandler {
    private final Writer writer;

    public XMLSentenceHandler(String filePath) throws IOException {
        this.writer = Files.newBufferedWriter(Path.of(filePath));
    }

    @Override
    public void start() throws IOException {
        writer.write("""
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <text>
                """);
        writer.flush();
    }

    @Override
    public void handleSentence(Sentence sentence) {
        try {
            writer.write("    <sentence>\n");

            for (var word : sentence.getSortedWords()) {
                writer.write("        <word>" + StringEscapeUtils.escapeXml10(word) + "</word>\n");
            }

            writer.write("    </sentence>\n");
            writer.flush();
        } catch (IOException e) {
            throw new XMLException("Error writing XML", e);
        }
    }

    @Override
    public void end() {
        try (writer) { // try-with-resources will close the writer
            writer.write("</text>");
            writer.flush();
        } catch (IOException e) {
            throw new XMLException("Error closing XML file", e);
        }
    }
}