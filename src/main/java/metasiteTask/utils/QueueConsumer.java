package metasiteTask.utils;

/**
 * @author ben
 * @version 1.0
 */
public interface QueueConsumer<T>  {

    /**
     * Consumes an object and signals if it should be sent to next consumer in the queue
     *
     * @param consumable object to consume
     * @return supposed to return true if object is consumed, and false if rejected
     */
    public boolean consume(final T consumable);

}
