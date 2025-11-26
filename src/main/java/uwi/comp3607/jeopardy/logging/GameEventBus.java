package uwi.comp3607.jeopardy.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple event bus used to implement the Observer pattern for game events.
 * <p>
 * The bus maintains a list of {@link GameEventListener} observers and forwards
 * events to each registered listener when {@link #publish(GameEvent)} is called.
 * </p>
 */
public class GameEventBus {
    /** List of registered event listeners. */
    private final List<GameEventListener> listeners = new ArrayList<>();

    /**
     * Registers a new listener to receive published events.
     *
     * @param listener the listener to register
     */
    public void register(GameEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Publishes an event to all registered listeners.
     *
     * @param event the event to distribute
     */
    public void publish(GameEvent event) {
        for (GameEventListener l : listeners) {
            l.onEvent(event);
        }
    }
}
