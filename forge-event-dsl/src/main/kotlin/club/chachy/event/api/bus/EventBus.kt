package club.chachy.event.api.bus

import club.chachy.event.api.handler.Handler

/**
 * The EventBus api is a rather simple but complicated abstraction if that makes sense?
 * It handles creating handlers and registering handlers are those are the two core libray-specific
 * operations we handle currently. This system has been tested on the Forge event bus and has proved
 * to work completely fine but feel free developers who use alternate eventbuses to make an issue
 * if you're struggling to add support for your eventbus. (It may not be designed for this system
 * as I may so so myself it is quite weird.)
 *
 * @author ChachyDev
 * @since 0.2.0
 */
interface EventBus<E> {
    /**
     * The [createHandler] function handles the library-specific principle of handlers. A handler
     * is supposed to also create an "event-handler" which will take and process the events given to it.
     * Take [club.chachy.event.impl.forge.ForgeHandler] as an example of how a handler in a perfect world
     * should be implemented.
     *
     * @author ChachyDev
     * @since 0.2.0
     */
    fun <T : E> createHandler(event: Class<T>): Handler<T>

    /**
     * This method is rather simple and just registers a handler (Which should have an "event-handler" onto the eventbus).
     * A very simple example of this simple operation would be showcased at [club.chachy.event.impl.forge.ForgeEventBus.register]
     * where it simply invokes [net.minecraftforge.fml.common.eventhandler.EventBus.register]. This means the operation has now been
     * successfully passed onto the library-specific method and can handle that.
     *
     * @author ChachyDev
     * @since 0.2.0
     */
    fun register(any: Any)
}