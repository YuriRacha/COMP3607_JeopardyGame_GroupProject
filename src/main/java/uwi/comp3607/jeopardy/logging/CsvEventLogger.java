package uwi.comp3607.jeopardy.logging;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;


/**
 * Logs game events to a CSV file suitable for process mining analysis.
 * <p>
 * This listener writes one row per event to {@code game_event_log.csv},
 * including the case ID, player name, activity, timestamp, category,
 * question value, answer, result, and score after play.
 * </p>
 */
public class CsvEventLogger implements GameEventListener, Closeable {

    /** Writer used to append CSV rows to the output file. */
    private final PrintWriter writer;
    /** Formatter used to output timestamps in ISO-8601 format. */
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ISO_INSTANT;

    /**
     * Creates a new CSV event logger targeting the specified file.
     * <p>
     * Writes the header row immediately upon construction.
     * </p>
     *
     * @param file the file to which the event log should be written
     * @throws FileNotFoundException if the file cannot be opened for writing
     */
    public CsvEventLogger(File file) throws FileNotFoundException {
        this.writer = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file), StandardCharsets.UTF_8));

        // change column name to Player_Name (but structure is the same)
        writer.println("Case_ID,Player_Name,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play");
    }

    /**
     * Handles an incoming game event by writing a line to the CSV file.
     *
     * @param e the event to log
     */
    @Override
    public void onEvent(GameEvent e) {
        String ts = formatter.format(e.getTimestamp());
        writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                safe(e.getCaseId()),
                safe(e.getPlayerName()),
                safe(e.getActivity()),
                ts,
                safe(e.getCategory()),
                e.getQuestionValue() == null ? "" : e.getQuestionValue(),
                safe(e.getAnswerGiven()),
                safe(e.getResult()),
                e.getScoreAfter() == null ? "" : e.getScoreAfter()
        );
        writer.flush();
    }

    /**
     * Simple helper that returns a non-null string for CSV output.
     *
     * @param s the original string (may be {@code null})
     * @return the original string, or an empty string if {@code null}
     */
    private String safe(String s) {
        if (s == null) return "";
        return s;
    }

    /**
     * Flushes and closes the underlying writer.
     */
    @Override
    public void close() {
        writer.flush();
        writer.close();
    }
}