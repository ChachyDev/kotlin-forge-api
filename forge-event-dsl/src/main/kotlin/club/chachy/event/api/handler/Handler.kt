package club.chachy.event.api.handler

/**
 * Handlers are a simple abstract class to hold handler data
 * HandlerData contains things such as filters, what to be executed when an event occurs and much more
 * Handlers help with abstraction to move away from keeping library-specific code inside the core code.
 *
 * @author ChachhyDev
 */
abstract class Handler<T> internal constructor() {
    /**
     * HandlerData contains data such as what should be executed and the filter that should be executed
     * before attempting to executte the stored action.
     */
    data class HandlerData<T>(val execute: (T) -> Unit, val filter: (T) -> Boolean)

    /**
     * Stores the handlers in a generic ArrayList
     */
    protected val storage: MutableList<HandlerData<T>> = ArrayList()

    /**
     * A helper method to add handler data to the handler storage of the current Handler.
     */
    fun addHandler(data: HandlerData<T>) = storage.add(data)
}