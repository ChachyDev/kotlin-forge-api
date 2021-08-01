package club.chachy.event.impl.forge

import club.chachy.event.api.bus.EventBus
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event

object ForgeEventBus : EventBus<Event> {
    override fun <T : Event> createHandler(event: Class<T>) = ForgeHandler(event)
    override fun register(any: Any) = MinecraftForge.EVENT_BUS.register(any)
}