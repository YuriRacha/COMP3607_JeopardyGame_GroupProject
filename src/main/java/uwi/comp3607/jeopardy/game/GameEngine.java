package uwi.comp3607.jeopardy.game;

import uwi.comp3607.jeopardy.logging.*;
import uwi.comp3607.jeopardy.model.Player;
import uwi.comp3607.jeopardy.model.Question;
import uwi.comp3607.jeopardy.model.QuestionBoard;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates the core Jeopardy gameplay for a single session.
 * <p>
 * The {@code GameEngine} enforces the rules of the game: it handles selecting
 * questions, evaluating answers, updating scores, marking questions as used,
 * advancing turns, and publishing gameplay events for logging.
 * </p>
 * <p>
 * This class does not perform any console I/O itself. User interaction is
 * handled by higher-level code such as {@link uwi.comp3607.jeopardy.App}.
 * </p>
 */
public class GameEngine {
/** Current game state (players, board, and current player index). */
    private final GameState state;

    /** Event bus used to publish gameplay events for logging. */
    private final GameEventBus eventBus;
    /** Chronological history of all turns taken during this game. */
    private final List<Turn> turnHistory = new ArrayList<>();

     /**
     * Creates a new game engine wired to a game state and event bus.
     *
     * @param state    the game state to mutate during play
     * @param eventBus the event bus used for process-mining style logging
     */
    public GameEngine(GameState state, GameEventBus eventBus) {
        this.state = state;
        this.eventBus = eventBus;
    }

    /**
     * Returns the game state controlled by this engine.
     *
     * @return the game state
     */
    public GameState getState() {
        return state;
    }

    /**
     * Returns the ordered list of all turns that have been played so far.
     * <p>
     * This history is used by the report generator to create the final
     * turn-by-turn summary.
     * </p>
     *
     * @return immutable view of the turn history list
     */
    public List<Turn> getTurnHistory() {
        return turnHistory;
    }

    /**
     * Determines whether the game is over.
     * <p>
     * The game ends when either:
     * </p>
     * <ul>
     *   <li>All questions on the board have been answered, or</li>
     *   <li>The game has been flagged to quit.</li>
     * </ul>
     *
     * @return {@code true} if no more turns should be played
     */
    public boolean isGameOver() {
        return state.isQuit() || state.getBoard().allQuestionsUsed();
    }

    /**
     * Signals that the game should be terminated.
     * <p>
     * Sets the quit flag in the {@link GameState} and publishes an
     * "Exit Game" event on the event bus.
     * </p>
     */
    public void quitGame() {
        state.setQuit(true);
        eventBus.publish(GameEvent.simple(state.getCaseId(), null,
                "Exit Game", Instant.now()));
    }

    /**
     * Selects a question for the given player from the question board.
     * <p>
     * If the category or value is invalid, or the question has already been
     * used, this method returns {@code null}.
     * </p>
     * <p>
     * Successful selections result in "Select Category" and "Select Question"
     * events being published to the event bus.
     * </p>
     *
     * @param player   the player whose turn it is
     * @param category the question category name
     * @param value    the point value of the chosen question
     * @return the selected {@link Question}, or {@code null} if not available
     */
    public Question selectQuestion(Player player, String category, int value) {
        QuestionBoard board = state.getBoard();
        if (!board.hasQuestion(category, value)) {
            return null;
        }

        eventBus.publish(GameEvent.withQuestion(state.getCaseId(),
                player, "Select Category", Instant.now(),
                category, value, "", "", player.getScore()));

        eventBus.publish(GameEvent.withQuestion(state.getCaseId(),
                player, "Select Question", Instant.now(),
                category, value, "", "", player.getScore()));

        Question q = board.getQuestion(category, value);
        return q;
    }

    /**
     * Processes a player's answer to a question.
     * <p>
     * This method:
     * </p>
     * <ul>
     *   <li>Checks the answer against the question's correct option.</li>
     *   <li>Adjusts the player's score (positive for correct, negative for incorrect).</li>
     *   <li>Marks the question as used so it cannot be chosen again.</li>
     *   <li>Records a {@link Turn} in the history list.</li>
     *   <li>Publishes "Answer Question" and "Score Updated" events.</li>
     *   <li>Advances to the next player's turn.</li>
     * </ul>
     *
     * @param player the player who answered
     * @param q      the question that was answered
     * @param answer the answer given by the player (e.g., "A", "B", "C", or "D")
     * @return a {@link Turn} describing the outcome of this interaction
     */
    public Turn answerQuestion(Player player, Question q, String answer) {
        boolean correct = q.getCorrectAnswer()
                           .equalsIgnoreCase(answer.trim());
        int delta = correct ? q.getValue() : -q.getValue();
        player.addToScore(delta);
        q.markUsed();

        String result = correct ? "Correct" : "Incorrect";

        // log answer
        eventBus.publish(GameEvent.withQuestion(
                state.getCaseId(),
                player,
                "Answer Question",
                Instant.now(),
                q.getCategory(),
                q.getValue(),
                answer,
                result,
                player.getScore()
        ));

        // log score update
        eventBus.publish(GameEvent.withQuestion(
                state.getCaseId(),
                player,
                "Score Updated",
                Instant.now(),
                q.getCategory(),
                q.getValue(),
                answer,
                result,
                player.getScore()
        ));

        Turn t = new Turn(player, q, answer, correct, delta, player.getScore());
        turnHistory.add(t);
        state.nextPlayer();
        return t;
    }
}
