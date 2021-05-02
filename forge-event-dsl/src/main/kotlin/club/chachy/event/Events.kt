package club.chachy.event

import club.chachy.event.builder.SubscriptionBuilder
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.Event

inline fun <reified T : Event> on(): SubscriptionBuilder<T> = SubscriptionBuilder(T::class.java)