package uwi.comp3607.jeopardy;

import org.junit.jupiter.api.Test;
import uwi.comp3607.jeopardy.io.JsonFileParser;
import uwi.comp3607.jeopardy.io.XmlFileParser;
import uwi.comp3607.jeopardy.model.QuestionBoard;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {

    @Test
    public void testJsonParserLoadsQuestions() throws Exception {
        JsonFileParser parser = new JsonFileParser();
        File file = new File("sample_game_JSON.json");
        QuestionBoard board = parser.parse(file);
        assertFalse(board.getCategories().isEmpty());
    }

    @Test
    public void testXmlParserLoadsQuestions() throws Exception {
        XmlFileParser parser = new XmlFileParser();
        File file = new File("sample_game_XML.xml");
        QuestionBoard board = parser.parse(file);
        assertFalse(board.getCategories().isEmpty());
    }
}
