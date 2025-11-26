package uwi.comp3607.jeopardy.model;

/**
 * Represents a single Jeopardy question.
 * <p>
 * A question has a category, a point value, the question text, four
 * multiple-choice options (A–D), and a correct answer key. It also tracks
 * whether the question has already been used in the game.
 * </p>
 */
public class Question {
    /** Name of the category this question belongs to. */
    private final String category;
    /** Point value awarded or deducted for this question. */
    private final int value;
    /** The question text shown to the player. */
    private final String questionText;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;

    /**
     * The correct answer key for this question.
     * <p>
     * Expected values are "A", "B", "C", or "D".
     * </p>
     */
    private final String correctAnswer; // "A", "B", "C", or "D"
    /** Indicates whether this question has already been asked in the game. */
    private boolean used = false;

    /**
     * Constructs a new question with all required data.
     *
     * @param category      the question category
     * @param value         the point value for this question
     * @param questionText  the text of the question
     * @param optionA       option A text
     * @param optionB       option B text
     * @param optionC       option C text
     * @param optionD       option D text
     * @param correctAnswer the correct answer key ("A", "B", "C", or "D")
     */
    public Question(String category, int value, String questionText,
                    String optionA, String optionB, String optionC, String optionD,
                    String correctAnswer) {
        this.category = category;
        this.value = value;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() { return category; }
    public int getValue() { return value; }
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }

    /**
     * Indicates whether this question has been used in the game.
     *
     * @return {@code true} if the question has already been asked
     */
    public boolean isUsed() { return used; }
     /**
     * Marks this question as used so it cannot be selected again.
     */
    public void markUsed() { this.used = true; }
}
