package uwi.comp3607.jeopardy.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uwi.comp3607.jeopardy.model.Question;
import uwi.comp3607.jeopardy.model.QuestionBoard;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * {@link FileParser} implementation that loads questions from an XML file.
 * <p>
 * The XML structure is expected to have a root element such as
 * {@code <JeopardyQuestions>} containing multiple {@code <QuestionItem>} elements,
 * each with child elements for category, value, question text, options and
 * correct answer.
 * </p>
 */
public class XmlFileParser implements FileParser {

    @Override
    public QuestionBoard parse(File file) throws IOException {
        QuestionBoard board = new QuestionBoard();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            // Root is <JeopardyQuestions>, children are <QuestionItem>
            NodeList questionNodes = doc.getElementsByTagName("QuestionItem");

            for (int i = 0; i < questionNodes.getLength(); i++) {
                Node node = questionNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element qElem = (Element) node;

                String category = getText(qElem, "Category");
                int value = Integer.parseInt(getText(qElem, "Value"));
                String questionText = getText(qElem, "QuestionText");

                Element optionsElem = (Element) qElem.getElementsByTagName("Options").item(0);
                String optionA = getText(optionsElem, "OptionA");
                String optionB = getText(optionsElem, "OptionB");
                String optionC = getText(optionsElem, "OptionC");
                String optionD = getText(optionsElem, "OptionD");

                String correctAnswer = getText(qElem, "CorrectAnswer");

                Question q = new Question(
                        category,
                        value,
                        questionText,
                        optionA,
                        optionB,
                        optionC,
                        optionD,
                        correctAnswer
                );

                board.addQuestion(q);
            }

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Failed to parse XML file: " + e.getMessage(), e);
        }

        return board;
    }

     /**
     * Helper method to get the text content of a child element by tag name.
     *
     * @param parent  the parent element
     * @param tagName the name of the child element
     * @return trimmed text content of the first matching child, or an empty string
     */
    private String getText(Element parent, String tagName) {
        if (parent == null) return "";
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return "";
        return list.item(0).getTextContent().trim();
    }
}