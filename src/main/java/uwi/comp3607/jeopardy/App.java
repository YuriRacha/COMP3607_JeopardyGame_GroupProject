package uwi.comp3607.jeopardy;

import uwi.comp3607.jeopardy.game.GameEngine;
import uwi.comp3607.jeopardy.game.GameState;
import uwi.comp3607.jeopardy.game.Turn;
import uwi.comp3607.jeopardy.io.FileParser;
import uwi.comp3607.jeopardy.io.FileParserFactory;
import uwi.comp3607.jeopardy.logging.*;
import uwi.comp3607.jeopardy.model.Player;
import uwi.comp3607.jeopardy.model.Question;
import uwi.comp3607.jeopardy.model.QuestionBoard;
import uwi.comp3607.jeopardy.report.TxtReportGenerator;

import java.io.File;
import java.time.Instant;
import java.util.*;

/**
 * Console entry point for the multi-player Jeopardy game.
 * <p>
 * This class wires together the file parsers, game engine, event logging,
 * and report generator. It is responsible for all console interaction:
 * loading the game file, collecting player information, running the game
 * loop, and generating output files at the end of the session.
 * </p>
 */
public class App {
/**
     * Main entry point for the Jeopardy game application.
     * <p>
     * This method:
     * </p>
     * <ul>
     *   <li>Prompts the user for the path to the game data file (CSV/JSON/XML).</li>
     *   <li>Creates the appropriate {@link uwi.comp3607.jeopardy.io.FileParser}.</li>
     *   <li>Loads questions into a {@link uwi.comp3607.jeopardy.model.QuestionBoard}.</li>
     *   <li>Collects the number of players and their names.</li>
     *   <li>Runs the main gameplay loop using {@link uwi.comp3607.jeopardy.game.GameEngine}.</li>
     *   <li>Generates a text report and a process-mining event log CSV.</li>
     * </ul>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String caseId = "GAME" + System.currentTimeMillis();

        try {
            GameEventBus eventBus = new GameEventBus();
            CsvEventLogger logger = new CsvEventLogger(new File("game_event_log.csv"));
            eventBus.register(logger);

            eventBus.publish(GameEvent.simple(caseId, null,
                    "Start Game", Instant.now()));

            System.out.print("Enter path to game file (CSV/JSON/XML): ");
            String path = scanner.nextLine().trim();
            eventBus.publish(GameEvent.simple(caseId, null,
                    "Load File", Instant.now()));

            FileParser parser = FileParserFactory.createParser(path);
            QuestionBoard board = parser.parse(new File(path));

            eventBus.publish(GameEvent.simple(caseId, null,
                    "File Loaded Successfully", Instant.now()));

            // ---- safer player count input (whole numbers 1–4 only) ----
            int playerCount = 0;
            while (true) {
                System.out.print("Enter number of players (1-4): ");
                String input = scanner.nextLine().trim();
                try {
                    playerCount = Integer.parseInt(input);
                    if (playerCount >= 1 && playerCount <= 4) {
                        break;
                    } else {
                        System.out.println("Please enter a whole number between 1 and 4.");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input. Please enter a whole number between 1 and 4.");
                }
            }
            // -----------------------------------------------------------

            eventBus.publish(GameEvent.simple(caseId, null,
                    "Select Player Count", Instant.now()));

            List<Player> players = new ArrayList<>();
            for (int i = 1; i <= playerCount; i++) {
                System.out.print("Enter name for Player " + i + ": ");
                String name = scanner.nextLine().trim();
                players.add(new Player(i, name));
                eventBus.publish(GameEvent.simple(caseId,
                        players.get(i - 1),
                        "Enter Player Name", Instant.now()));
            }

            GameState state = new GameState(caseId, players, board);
            GameEngine engine = new GameEngine(state, eventBus);

            gameLoop(scanner, engine);

            // Generate report
            TxtReportGenerator reportGen = new TxtReportGenerator();
            reportGen.generate(new File("game_report.txt"),
                    caseId,
                    state.getPlayers(),
                    engine.getTurnHistory());

            eventBus.publish(GameEvent.simple(caseId, null,
                    "Generate Report", Instant.now()));
            eventBus.publish(GameEvent.simple(caseId, null,
                    "Generate Event Log", Instant.now()));

            logger.close();
            System.out.println("Game over. Report saved to game_report.txt");
            System.out.println("Event log saved to game_event_log.csv");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }
     /**
     * Runs the main turn-based game loop on the console.
     * <p>
     * For each turn, this method:
     * </p>
     * <ul>
     *   <li>Displays the current player and scores.</li>
     *   <li>Shows available categories and question values.</li>
     *   <li>Allows the current player to select a question or quit.</li>
     *   <li>Displays the chosen question and options.</li>
     *   <li>Captures the player's answer and delegates to {@link GameEngine}.</li>
     *   <li>Prints immediate feedback (correct/incorrect) and updated score.</li>
     * </ul>
     *
     * @param scanner the console scanner used to read user input
     * @param engine  the game engine that manages game state and scoring
     * @param eventBus the event bus used for publishing gameplay events
     */

    private static void gameLoop(Scanner scanner,
                                 GameEngine engine) {

        while (!engine.isGameOver()) {
            var state = engine.getState();
            var player = state.getCurrentPlayer();
            System.out.println();
            System.out.println("Current player: " + player.getName());
            System.out.println("Current score: " + player.getScore());

            System.out.println("Categories:");
            for (String cat : state.getBoard().getCategories()) {
                System.out.print(" - " + cat + " (");
                var values = state.getBoard().getValuesForCategory(cat);
                var iter = values.iterator();
                while (iter.hasNext()) {
                    int v = iter.next();
                    String marker = state.getBoard().hasQuestion(cat, v) ? "" : "X";
                    System.out.print(v + marker);
                    if (iter.hasNext()) System.out.print(", ");
                }
                System.out.println(")");
            }

            System.out.println("Options: ");
            System.out.println(" 1. Choose a question");
            System.out.println(" 2. Quit game");

            System.out.print("Select option: ");
            String opt = scanner.nextLine().trim();
            if (opt.equals("2")) {
                engine.quitGame();
                break;
            }

            System.out.print("Enter category exactly as shown: ");
            String category = scanner.nextLine().trim();

            System.out.print("Enter question value (e.g., 100): ");
            int value = Integer.parseInt(scanner.nextLine().trim());

            Question q = engine.selectQuestion(player, category, value);
            if (q == null || q.isUsed()) {
                System.out.println("Invalid selection or question already used.");
                continue;
            }

            System.out.println("Question: " + q.getQuestionText());
            System.out.println("A) " + q.getOptionA());
            System.out.println("B) " + q.getOptionB());
            System.out.println("C) " + q.getOptionC());
            System.out.println("D) " + q.getOptionD());
            System.out.print("Your answer (A/B/C/D): ");
            String ans = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

            // ---- show immediate feedback after answering ----
            Turn turn = engine.answerQuestion(player, q, ans);

            if (turn.isCorrect()) {
                System.out.printf("✅ Correct! You gained %d points.%n",
                        turn.getPointsEarned());
            } else {
                System.out.printf("❌ Incorrect. You lost %d points.%n",
                        Math.abs(turn.getPointsEarned()));
            }
            System.out.printf("Your new score: %d%n",
                    turn.getScoreAfter());
            // ------------------------------------------------
        }
    }
}
