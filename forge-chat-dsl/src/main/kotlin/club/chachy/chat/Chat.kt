package club.chachy.chat

import club.chachy.chat.builder.MessageBuilder
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.ChatComponentText

fun message(space: String = " ", builder: MessageBuilder.() -> Unit): String {
    val message = MessageBuilder(space)
    builder(message)
    return message.build()
}

fun EntityPlayerSP.sendMessage(space: String = " ", builder: MessageBuilder.() -> Unit) =
    addChatComponentMessage(ChatComponentText(message(space, builder)))
