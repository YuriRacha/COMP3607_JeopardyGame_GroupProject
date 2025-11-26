package uwi.comp3607.jeopardy.model;

/**
 * Represents a player in the Jeopardy game.
 * <p>
 * A player has a numeric identifier used internally and a display name
 * used in the console UI and logs, along with a current score.
 * </p>
 */
public class Player {
    /** Internal numeric identifier for this player. */
    private final int id;          // Player_ID for logs
    /** Display name entered by the user. */
    private final String name;
    /** Current score for this player. */
    private int score = 0;

    /**
     * Creates a new player with the given identifier and name.
     *
     * @param id   numeric identifier for this player
     * @param name display name of the player
     */
    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    /**
     * Returns the player's current score.
     *
     * @return current score value
     */
    public int getScore() { return score; }

    /**
     * Adjusts the player's score by the given delta.
     * <p>
     * A positive delta increases the score; a negative delta decreases it.
     * </p>
     *
     * @param delta number of points to add (may be negative)
     */
    public void addToScore(int delta) {
        this.score += delta;
    }
}
