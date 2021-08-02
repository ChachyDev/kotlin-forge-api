package club.chachy.event.impl.forge

import club.chachy.event.api.handler.Handler
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ForgeHandler<T : Event>(private val eventClazz: Class<T>) : Handler<T>() {
    @SubscribeEvent
    fun onEvent(event: T) {
        if (eventClazz == event::class.java) {
            process(event)
        }
    }
}