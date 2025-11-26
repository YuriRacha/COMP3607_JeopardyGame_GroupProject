package uwi.comp3607.jeopardy.model;

import java.util.*;

/**
 * Stores and manages all questions for a Jeopardy game.
 * <p>
 * The board groups questions by category and point value, and provides methods
 * to look up categories, available question values, and to check whether all
 * questions have been used.
 * </p>
 */
public class QuestionBoard {
    // Map<Category, Map<Value, Question>>
    /**
     * Internal mapping from category name to a map of value &rarr; question.
     * <p>
     * Categories are kept in insertion order; values are kept sorted.
     * </p>
     */
    private final Map<String, Map<Integer, Question>> board = new LinkedHashMap<>();

    /**
     * Adds a question to the board under its category and value.
     *
     * @param q the question to add
     */
    public void addQuestion(Question q) {
        board
            .computeIfAbsent(q.getCategory(), c -> new TreeMap<>())
            .put(q.getValue(), q);
    }

    /**
     * Returns the set of all category names currently on the board.
     *
     * @return set of category names
     */
    public Set<String> getCategories() {
        return board.keySet();
    }

    /**
     * Returns a sorted set of all point values available for the given category.
     *
     * @param category the category to inspect
     * @return sorted set of point values, or an empty set if the category does not exist
     */
    public SortedSet<Integer> getValuesForCategory(String category) {
        Map<Integer, Question> inner = board.get(category);
        if (inner == null) return new TreeSet<>();
        return new TreeSet<>(inner.keySet());
    }

    /**
     * Retrieves the question for the specified category and value.
     *
     * @param category the category name
     * @param value    the point value
     * @return the {@link Question} for the given key, or {@code null} if none exists
     */
    public Question getQuestion(String category, int value) {
        Map<Integer, Question> inner = board.get(category);
        if (inner == null) return null;
        return inner.get(value);
    }

    /**
     * Checks whether all questions on the board have been used.
     *
     * @return {@code true} if every question is marked as used
     */
    public boolean allQuestionsUsed() {
        for (Map<Integer, Question> inner : board.values()) {
            for (Question q : inner.values()) {
                if (!q.isUsed()) return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the given category exists on the board.
     *
     * @param category category name
     * @return {@code true} if the category is present
     */
    public boolean hasCategory(String category) {
        return board.containsKey(category);
    }

    /**
     * Checks whether there is an unused question for the given category and value.
     *
     * @param category the category name
     * @param value    the point value
     * @return {@code true} if a question exists and has not been used
     */
    public boolean hasQuestion(String category, int value) {
        Question q = getQuestion(category, value);
        return q != null && !q.isUsed();
    }
}
