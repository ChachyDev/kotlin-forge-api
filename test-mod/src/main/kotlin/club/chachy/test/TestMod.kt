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
    private val minecraft get() = Minecraft.getMinecraft()
    private val player get() = minecraft.thePlayer

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        // event-dsl testing
        // Listen for "Secret Fullscreen Tactic" and if the messages passes the filter toggle fullscreen
        on<ClientChatReceivedEvent>()
            .filter { it.message.unformattedText.contains("Secret Fullscreen Tactic") }
            .subscribe {
                it.isCanceled = true
                minecraft.toggleFullscreen()
            }

        // Chat dsl testing
        on<ClientChatReceivedEvent>()
            .filter { it.message.unformattedText.contains("Send the test chat message") }
            .subscribe {
                it.isCanceled = true
                player.sendMessage {
                    withStyle(green + bold + italic) {
                        +"It's green, bold AND italic!"
                    }
                    style(red) {
                        +"This is a normal red message"
                    }
                    withStyle(red, bold) {
                        +"This is red AND bold!"
                    }
                    +"Now this is just normal (BORING!)"
                }
            }
    }
}