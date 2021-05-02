package club.chachy.event.builder

import club.chachy.event.handler.Handler
import club.chachy.event.handler.handlers
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class SubscriptionBuilder<T : Event>(private val clazz: Class<T>) {
    private var filter: (T) -> Boolean = { true }

    private var execute: (T) -> Unit = {}

    fun filter(block: (T) -> Boolean): SubscriptionBuilder<T> {
        filter = block
        return this
    }

    fun subscribe(subscribe: (T) -> Unit) {
        execute = subscribe
        // End of the road load
        build()
    }

    @Suppress("UNCHECKED_CAST")
    private fun build() {
        val h = handlers[clazz] as? Handler<T>
        if (h != null) {
            h.addHandler(Handler.HandlerData(execute, filter))
        } else {
            val handler = object : Handler<T>() {
                @SubscribeEvent
                fun handle(event: T) {
                    if (event::class.java == clazz) {
                        handlers.forEach {
                            if (it.filter(event)) {
                                it.execute(event)
                            }
                        }
                    }
                }
            }
            handler.addHandler(Handler.HandlerData(execute, filter))
            handlers[clazz] = handler
            MinecraftForge.EVENT_BUS.register(handler)
        }
    }
}