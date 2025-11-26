package uwi.comp3607.jeopardy.logging;

/**
 * Observer interface for components that want to receive game events.
 * <p>
 * Implementations can log events to a file, display them in a UI, or perform
 * additional analysis. Listeners are registered with a {@link GameEventBus}.
 * </p>
 */
public interface GameEventListener {

    /**
     * Called when a new {@link GameEvent} is published.
     *
     * @param event the event that occurred
     */
    void onEvent(GameEvent event);
}
