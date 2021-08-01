package club.chachy.event.api.builder

import club.chachy.event.api.bus.EventBus
import club.chachy.event.api.handler.Handler
import club.chachy.event.handlerRegistry
import net.minecraftforge.common.MinecraftForge

/**
 * The main part of the code which handles the DSL blocks and building the data into an "event-handler"
 * with assistants from oru [EventBus].
 *
 * @author ChachyDev
 * @since 0.2.0
 */
class SubscriptionBuilder<E, T : E>(private val eventClass: Class<T>, private val bus: EventBus<E>) {
    private var filter: (T) -> Boolean = { true }

    private var execute: (T) -> Unit = {}

    /**
     * A function to be used by the developer to filter
     * through an event.
     *
     * @author ChachyDev
     * @since 0.2.0
     */
    fun filter(block: (T) -> Boolean): SubscriptionBuilder<E, T> {
        filter = block
        return this
    }

    /**
     * Once this method is called there is no going back. We no longer return
     * [SubscriptionBuilder] as we should've finished with everything and we
     * now begin to build our eventhandler and register it to the library-specific
     * sides of the projet.
     *
     * @author ChachyDev
     * @since 0.2.0
     */
    fun subscribe(subscribe: (T) -> Unit) {
        execute = subscribe
        // End of the road load
        build()
    }

    /**
     * This is the main show now. This function builds our data into
     * an "event-handler" to be handled by the library-specific side.
     * As you can see here we currently have a registry where we attempt
     * to fetch a cached handler from. If we could not have find a cached handler
     * we then begin to create a new one via [EventBus.createHandler] and cache it.
     * After we make sure to register the handler to the eventbus ([EventBus.register]).
     * Once this has been completed we are down with all our operations until the cycle
     * starts again and we get called again!
     *
     * @author ChachyDev
     * @since 0.2.0
     */
    @Suppress("UNCHECKED_CAST")
    private fun build() {
        val h = handlerRegistry[eventClass] as? Handler<T>
        if (h != null) {
            h.addHandler(Handler.HandlerData(execute, filter))
        } else {
            val handler = bus.createHandler(eventClass)
            handler.addHandler(Handler.HandlerData(execute, filter))
            handlerRegistry[eventClass] = handler
            bus.register(handler)
        }
    }
}