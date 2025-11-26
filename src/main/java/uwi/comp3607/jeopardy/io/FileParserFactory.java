package uwi.comp3607.jeopardy.io;

/**
 * Factory for creating {@link FileParser} instances based on file extension.
 * <p>
 * This class encapsulates the logic for choosing the correct parser implementation
 * (CSV, JSON, XML) and keeps the rest of the application decoupled from
 * concrete parser classes.
 * </p>
 */
public class FileParserFactory {

    /**
     * Creates an appropriate {@link FileParser} for the given filename.
     *
     * @param filename the name or path of the game data file
     * @return a {@link FileParser} suitable for the file's extension
     * @throws IllegalArgumentException if the file type is unsupported
     */
    public static FileParser createParser(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".csv")) {
            return new CsvFileParser();
        } else if (lower.endsWith(".json")) {
            return new JsonFileParser();
        } else if (lower.endsWith(".xml")) {
            return new XmlFileParser();
        }
        throw new IllegalArgumentException("Unsupported file type: " + filename);
    }
}
