package uwi.comp3607.jeopardy;

import org.junit.jupiter.api.Test;
import uwi.comp3607.jeopardy.game.GameEngine;
import uwi.comp3607.jeopardy.game.GameState;
import uwi.comp3607.jeopardy.logging.GameEventBus;
import uwi.comp3607.jeopardy.model.Player;
import uwi.comp3607.jeopardy.model.Question;
import uwi.comp3607.jeopardy.model.QuestionBoard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTests {

    @Test
    public void testCorrectAnswerIncreasesScore() {
        QuestionBoard board = new QuestionBoard();
        Question q = new Question("Test", 100, "Q?",
                "A", "B", "C", "D", "A");
        board.addQuestion(q);

        Player p = new Player(1, "Alice");
        GameState state = new GameState("CASE1", List.of(p), board);
        GameEventBus bus = new GameEventBus();
        GameEngine engine = new GameEngine(state, bus);

        Question selected = engine.selectQuestion(p, "Test", 100);
        assertNotNull(selected);

        engine.answerQuestion(p, selected, "A");
        assertEquals(100, p.getScore());
        assertTrue(selected.isUsed());
    }

    @Test
    public void testIncorrectAnswerDecreasesScore() {
        QuestionBoard board = new QuestionBoard();
        Question q = new Question("Test", 200, "Q?",
                "A", "B", "C", "D", "A");
        board.addQuestion(q);

        Player p = new Player(1, "Bob");
        GameState state = new GameState("CASE2", List.of(p), board);
        GameEventBus bus = new GameEventBus();
        GameEngine engine = new GameEngine(state, bus);

        Question selected = engine.selectQuestion(p, "Test", 200);
        engine.answerQuestion(p, selected, "B");

        assertEquals(-200, p.getScore());
    }
}
