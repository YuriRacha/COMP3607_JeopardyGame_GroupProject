package uwi.comp3607.jeopardy.io;

import uwi.comp3607.jeopardy.model.Question;
import uwi.comp3607.jeopardy.model.QuestionBoard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV implementation of {@link FileParser}.
 * <p>
 * This parser loads Jeopardy questions from a CSV file. It supports:
 * <ul>
 *     <li>Quoted fields (e.g., {@code "What is 2, 3, and 4?"})</li>
 *     <li>Files with 8 columns:
 *       Category, Value, Question, OptionA, OptionB, OptionC, OptionD, CorrectAnswer
 *     </li>
 * </ul>
 * <p>
 * If a line has fewer than 8 columns, it is skipped.
 * </p>
 */
public class CsvFileParser implements FileParser {

    @Override
    public QuestionBoard parse(File file) throws IOException {
        QuestionBoard board = new QuestionBoard();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            // Skip header row
            String line = reader.readLine();
            if (line == null) {
                return board;
            }

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // skip blank lines
                }

                List<String> parts = splitCsvLine(line);

                // We only need 8 columns
                if (parts.size() < 8) {
                    continue;
                }

                String category = parts.get(0).trim();
                int value = Integer.parseInt(parts.get(1).trim());
                String qText = parts.get(2).trim();
                String optionA = parts.get(3).trim();
                String optionB = parts.get(4).trim();
                String optionC = parts.get(5).trim();
                String optionD = parts.get(6).trim();
                String correct = parts.get(7).trim();

                Question q = new Question(
                        category,
                        value,
                        qText,
                        optionA,
                        optionB,
                        optionC,
                        optionD,
                        correct
                );

                board.addQuestion(q);
            }
        }

        return board;
    }

    /**
     * Splits a CSV line into fields while respecting double quotes.
     * This allows commas inside quotes to be handled correctly.
     *
     * Example:
     * <pre>
     *   "Hello, world",A,B,C → ["Hello, world", "A", "B", "C"]
     * </pre>
     *
     * @param line raw CSV line
     * @return list of parsed fields
     */
    private List<String> splitCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // toggle quoted state
            } else if (c == ',' && !inQuotes) {
                // End of field
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        // Add the final field
        fields.add(current.toString());

        return fields;
    }
}