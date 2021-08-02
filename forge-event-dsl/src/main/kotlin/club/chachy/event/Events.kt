package club.chachy.event

import club.chachy.event.api.builder.SubscriptionBuilder
import club.chachy.event.api.bus.EventBus
import club.chachy.event.impl.forge.ForgeEventBus
import club.chachy.event.api.handler.Handler
import net.minecraftforge.fml.common.eventhandler.Event

@DslMarker
internal annotation class Dsl

internal val handlerRegistry = HashMap<Class<*>, Handler<*>>()

// For those who want to use custom buses.
// Copy the default forge function if you would like to
// create your own type of method
@Dsl
inline fun <E, reified T : E> on(bus: EventBus<E>): SubscriptionBuilder<E, T> =
    SubscriptionBuilder(T::class.java, bus)

// Default Forge support
@Dsl
inline fun <reified T : Event> on() = on<Event, T>(ForgeEventBus)
