package uwi.comp3607.jeopardy.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uwi.comp3607.jeopardy.model.Question;
import uwi.comp3607.jeopardy.model.QuestionBoard;

import java.io.File;
import java.io.IOException;

/**
 * {@link FileParser} implementation that loads questions from a JSON file.
 * <p>
 * The JSON structure is expected to be an array of objects, each with fields
 * such as {@code Category}, {@code Value}, {@code Question}, {@code Options},
 * and {@code CorrectAnswer}.
 * </p>
 */
public class JsonFileParser implements FileParser {

    /** Jackson object mapper used to read JSON input. */
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public QuestionBoard parse(File file) throws IOException {
        QuestionBoard board = new QuestionBoard();
        JsonNode root = mapper.readTree(file);
        if (!root.isArray()) return board;

        for (JsonNode node : root) {
            String category = node.get("Category").asText();
            int value = node.get("Value").asInt();
            String question = node.get("Question").asText();

            JsonNode options = node.get("Options");
            String optionA = options.get("A").asText();
            String optionB = options.get("B").asText();
            String optionC = options.get("C").asText();
            String optionD = options.get("D").asText();

            String correct = node.get("CorrectAnswer").asText();

            board.addQuestion(new Question(category, value, question,
                    optionA, optionB, optionC, optionD, correct));
        }

        return board;
    }
}
