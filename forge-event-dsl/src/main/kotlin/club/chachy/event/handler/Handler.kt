package club.chachy.event.handler

import net.minecraftforge.fml.common.eventhandler.Event

abstract class Handler<T : Event> {
    data class HandlerData<T : Event>(val execute: (T) -> Unit, val filter: (T) -> Boolean)

    protected val handlers: MutableList<HandlerData<T>> = ArrayList()

    fun addHandler(data: HandlerData<T>) = handlers.add(data)
}