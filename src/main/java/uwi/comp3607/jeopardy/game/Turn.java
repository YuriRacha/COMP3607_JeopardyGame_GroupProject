package uwi.comp3607.jeopardy.game;

import uwi.comp3607.jeopardy.model.Player;
import uwi.comp3607.jeopardy.model.Question;


/**
 * Represents the outcome of a single player's turn in the game.
 * <p>
 * A turn records:
 * </p>
 * <ul>
 *   <li>The player who answered.</li>
 *   <li>The question that was asked.</li>
 *   <li>The answer given by the player.</li>
 *   <li>Whether the answer was correct.</li>
 *   <li>The number of points gained or lost.</li>
 *   <li>The player's score after the turn.</li>
 * </ul>
 * <p>
 * Turn objects are used both for game history and for generating the final
 * text report.
 * </p>
 */
public class Turn {
     /** Player who took this turn. */
    private final Player player;
    /** Question that was asked on this turn. */
    private final Question question;
    /** Answer provided by the player, e.g. "A", "B", "C", or "D". */
    private final String givenAnswer;
    /** Indicates whether the player's answer was correct. */
    private final boolean correct;
    /** Number of points gained (positive) or lost (negative) on this turn. */
    private final int pointsEarned;
    /** The player's score after this turn has been processed. */
    private final int scoreAfter;

    /**
     * Constructs a turn with all relevant outcome details.
     *
     * @param player       the player who answered
     * @param question     the question that was answered
     * @param givenAnswer  the answer text provided by the player
     * @param correct      whether the player's answer was correct
     * @param pointsEarned points gained (positive) or lost (negative)
     * @param scoreAfter   the player's score after this turn
     */
    public Turn(Player player, Question question, String givenAnswer,
                boolean correct, int pointsEarned, int scoreAfter) {
        this.player = player;
        this.question = question;
        this.givenAnswer = givenAnswer;
        this.correct = correct;
        this.pointsEarned = pointsEarned;
        this.scoreAfter = scoreAfter;
    }
/** @return the player who took this turn */
    public Player getPlayer() { return player; }
    /** @return the question that was asked on this turn */
    public Question getQuestion() { return question; }
    /** @return the answer provided by the player */
    public String getGivenAnswer() { return givenAnswer; }
     /** @return {@code true} if the answer was correct, {@code false} otherwise */
    public boolean isCorrect() { return correct; }
    /** @return the number of points gained or lost on this turn */
    public int getPointsEarned() { return pointsEarned; }
    /** @return the player's score after this turn */
    public int getScoreAfter() { return scoreAfter; }
}
