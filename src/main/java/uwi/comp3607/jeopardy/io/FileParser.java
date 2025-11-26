package uwi.comp3607.jeopardy.io;

import uwi.comp3607.jeopardy.model.QuestionBoard;
import java.io.File;
import java.io.IOException;

/**
 * Strategy interface for loading Jeopardy questions from different file formats.
 * <p>
 * Implementations of this interface support CSV, JSON, XML, or other formats,
 * and produce a populated {@link QuestionBoard} from the given file.
 * </p>
 */
public interface FileParser {

    /**
     * Parses the specified file and returns a question board.
     *
     * @param file the input file containing Jeopardy questions
     * @return a {@link QuestionBoard} populated with questions from the file
     * @throws IOException if the file cannot be read or parsed
     */
    QuestionBoard parse(File file) throws IOException;
}
