package uwi.comp3607.jeopardy.game;

import uwi.comp3607.jeopardy.model.Player;
import uwi.comp3607.jeopardy.model.QuestionBoard;

import java.util.List;

/**
 * Holds mutable state for a single Jeopardy game session.
 * <p>
 * The game state tracks:
 * </p>
 * <ul>
 *   <li>The case ID for this session (used in logs and reports).</li>
 *   <li>The list of players participating in the game.</li>
 *   <li>The shared question board.</li>
 *   <li>The index of the current player.</li>
 *   <li>Whether the game has been flagged to quit.</li>
 * </ul>
 */
public class GameState {
    /** Identifier for the game session, shared across logs and reports. */
    private final String caseId;
    /** List of players taking part in the game. */
    private final List<Player> players;
    /** The board containing all questions for this game. */
    private final QuestionBoard board;
    /** Index into {@link #players} indicating whose turn it is. */
    private int currentPlayerIndex = 0;
    /** Flag indicating if the game should terminate early. */
    private boolean quit = false;


     /**
     * Creates a new game state with the given case ID, players, and question board.
     *
     * @param caseId  unique identifier for this game session
     * @param players the players participating in the game
     * @param board   the shared question board
     */
    public GameState(String caseId, List<Player> players, QuestionBoard board) {
        this.caseId = caseId;
        this.players = players;
        this.board = board;
    }


     /**
     * Returns the case ID for this game session.
     *
     * @return the case ID string
     */
    public String getCaseId() { return caseId; }

    /**
     * Returns the list of players participating in this game.
     *
     * @return list of players
     */
    public List<Player> getPlayers() { return players; }

     /**
     * Returns the question board for this game.
     *
     * @return the question board
     */
    public QuestionBoard getBoard() { return board; }

    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Advances the current player index to the next player in the list.
     * <p>Wraps around to the first player after the last one.</p>
     */
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Returns whether the game has been flagged to quit.
     *
     * @return {@code true} if the game should end, {@code false} otherwise
     */
    public boolean isQuit() { return quit; }
    
    /**
     * Sets the quit flag for the game.
     *
     * @param quit {@code true} to end the game, {@code false} to continue
     */
    public void setQuit(boolean quit) { this.quit = quit; }
}
