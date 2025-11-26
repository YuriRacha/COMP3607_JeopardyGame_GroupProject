package uwi.comp3607.jeopardy.logging;

import uwi.comp3607.jeopardy.model.Player;

import java.time.Instant;

/**
 * Represents a single event in the Jeopardy gameplay session for process mining.
 * <p>
 * Each event corresponds to a user or system action, such as "Start Game",
 * "Select Question", "Answer Question", or "Generate Report". Events are
 * ultimately written to {@code game_event_log.csv} with columns matching
 * the project handout.
 * </p>
 */
public class GameEvent {
    /** Identifier for the game session (Case_ID in the event log). */
    private final String caseId;
    /** Name of the player who triggered the event, or empty for system events. */
    private final String playerName;
    /** Short label describing the activity, e.g. "Start Game" or "Answer Question". */
    private final String activity;
    /** Timestamp of the event. */
    private final Instant timestamp;
    /** Question category associated with this event, if applicable. */
    private final String category;
    /** Question value (points) associated with this event, if applicable. */
    private final Integer questionValue;
    /** Answer provided by the player, if this is an answer-related event. */
    private final String answerGiven;
    /** Outcome of the action, e.g. "Correct" or "Incorrect". */
    private final String result;
     /** Player's score after this event has been applied. */
    private final Integer scoreAfter;

    /**
     * Creates a new game event with all fields specified.
     *
     * @param caseId       identifier for the game session
     * @param playerName   name of the player who triggered the event (or empty)
     * @param activity     short description of the activity
     * @param timestamp    time at which the event occurred
     * @param category     question category, if applicable
     * @param questionValue question value, if applicable
     * @param answerGiven  answer text, if any
     * @param result       result of the action (e.g. "Correct", "Incorrect")
     * @param scoreAfter   player's score after this event
     */
    public GameEvent(String caseId,
                     String playerName,
                     String activity,
                     Instant timestamp,
                     String category,
                     Integer questionValue,
                     String answerGiven,
                     String result,
                     Integer scoreAfter) {
        this.caseId = caseId;
        this.playerName = playerName;
        this.activity = activity;
        this.timestamp = timestamp;
        this.category = category;
        this.questionValue = questionValue;
        this.answerGiven = answerGiven;
        this.result = result;
        this.scoreAfter = scoreAfter;
    }

    /**
     * Convenience factory method for a simple event without question details.
     *
     * @param caseId   game session ID
     * @param player   player associated with the event, or {@code null} for system events
     * @param activity activity description
     * @param ts       event timestamp
     * @return a new {@link GameEvent} with minimal fields populated
     */
    public static GameEvent simple(String caseId, Player player,
                                   String activity, Instant ts) {
        return new GameEvent(
                caseId,
                player == null ? "" : player.getName(),
                activity,
                ts,
                "",
                null,
                "",
                "",
                null
        );
    }

    /**
     * Convenience factory method for a question-related event.
     *
     * @param caseId      game session ID
     * @param player      player associated with the event
     * @param activity    activity description
     * @param ts          event timestamp
     * @param category    question category
     * @param value       question value
     * @param answer      answer text
     * @param result      result of the action
     * @param scoreAfter  player's score after this event
     * @return a new {@link GameEvent} with question details populated
     */
    public static GameEvent withQuestion(String caseId, Player player,
                                         String activity, Instant ts,
                                         String category, Integer value,
                                         String answer, String result,
                                         Integer scoreAfter) {
        return new GameEvent(
                caseId,
                player == null ? "" : player.getName(),
                activity,
                ts,
                category,
                value,
                answer,
                result,
                scoreAfter
        );
    }

    /** @return the game session identifier (Case_ID) */
    public String getCaseId() { return caseId; }
    /** @return the player name associated with this event, or empty if none */
    public String getPlayerName() { return playerName; }
    /** @return short description of the activity */
    public String getActivity() { return activity; }
    /** @return timestamp of the event */
    public Instant getTimestamp() { return timestamp; }
    /** @return question category for this event, if any */
    public String getCategory() { return category; }
     /** @return question value for this event, if any */
    public Integer getQuestionValue() { return questionValue; }
    /** @return answer given, if any */
    public String getAnswerGiven() { return answerGiven; }
    /** @return result of the action (e.g. "Correct", "Incorrect") */
    public String getResult() { return result; }
    /** @return player's score after this event */
    public Integer getScoreAfter() { return scoreAfter; }
}