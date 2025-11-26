package uwi.comp3607.jeopardy.report;

import uwi.comp3607.jeopardy.game.Turn;
import uwi.comp3607.jeopardy.model.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Generates a human-readable text summary report for a Jeopardy game session.
 * The report includes:
 * <ul>
 *   <li>Case ID and player names.</li>
 *   <li>A turn-by-turn narrative of the gameplay.</li>
 *   <li>Final scores for all players.</li>
 * </ul>
 * The output format is similar to the sample game report provided in the
 * project handout.
 */
public class TxtReportGenerator {

    /**
     * Generates a text report file for the completed game.
     *
     * @param file    the target file to write
     * @param caseId  the game session identifier
     * @param players the players who participated in the game
     * @param turns   the chronological list of turns played
     * @throws IOException if the report cannot be written
     */
    public void generate(File file,
                         String caseId,
                         List<Player> players,
                         List<Turn> turns) throws IOException {

        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file), StandardCharsets.UTF_8))) {

            out.println("JEOPARDY PROGRAMMING GAME REPORT");
            out.println("================================");
            out.println();
            out.println("Case ID: " + caseId);
            out.println();
            out.print("Players: ");
            for (int i = 0; i < players.size(); i++) {
                out.print(players.get(i).getName());
                if (i < players.size() - 1) out.print(", ");
            }
            out.println();
            out.println();
            out.println("Gameplay Summary:");
            out.println("-----------------");

            int turnNo = 1;
            for (Turn t : turns) {
                out.printf("Turn %d: %s selected %s for %d pts%n",
                        turnNo++,
                        t.getPlayer().getName(),
                        t.getQuestion().getCategory(),
                        t.getQuestion().getValue());
                out.println("Question: " + t.getQuestion().getQuestionText());
                out.printf("Answer: %s — %s (%+d pts)%n",
                        t.getGivenAnswer(),
                        t.isCorrect() ? "Correct" : "Incorrect",
                        t.getPointsEarned());
                out.printf("Score after turn: %s = %d%n",
                        t.getPlayer().getName(),
                        t.getScoreAfter());
                out.println();
            }

            out.println("Final Scores:");
            for (Player p : players) {
                out.printf("%s: %d%n", p.getName(), p.getScore());
            }
        }
    }
}
