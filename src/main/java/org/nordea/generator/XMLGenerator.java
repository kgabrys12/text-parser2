package org.nordea.generator;

import org.apache.commons.text.StringEscapeUtils;
import org.nordea.model.Sentence;

import java.util.List;

public class XMLGenerator {
    public String generateXML(List<Sentence> sentences) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xml.append("<text>\n");
        for (Sentence sentence : sentences) {
            xml.append("    <sentence>\n");
            for (String word : sentence.getWords()) {
                xml.append("        <word>").append(StringEscapeUtils.escapeXml10(word)).append("</word>\n");
            }
            xml.append("    </sentence>\n");
        }
        xml.append("</text>");
        return xml.toString();
    }
}