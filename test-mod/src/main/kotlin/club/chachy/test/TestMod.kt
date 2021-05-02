package club.chachy.test

import club.chachy.chat.builder.colors.bold
import club.chachy.chat.builder.colors.green
import club.chachy.chat.builder.colors.italic
import club.chachy.chat.builder.colors.red
import club.chachy.chat.sendMessage
import club.chachy.event.on
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(name = "TestMod", modid = "testmod", version = "1.0.0")
class TestMod {
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        // event-dsl testing
        // Listen for "Secret Fullscreen Tactic" and if the messages passes the filter toggle fullscreen
        on<ClientChatReceivedEvent>()
            .filter { it.message.unformattedText.contains("Secret Fullscreen Tactic") }
            .subscribe {
                it.isCanceled = true
                Minecraft.getMinecraft().toggleFullscreen()
            }

        on<ClientChatReceivedEvent>()
            .filter { it.message.unformattedText.contains("Send the test chat message") }
            .subscribe {
                it.isCanceled = true
                Minecraft.getMinecraft().thePlayer.sendMessage {
                    withStyle(green + bold + italic) {
                        +"It's green, bold AND italic!"
                    }
                    +"Boring message"
                }
            }
    }
}