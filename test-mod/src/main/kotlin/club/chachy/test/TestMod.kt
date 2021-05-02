package club.chachy.test

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
            .subscribe { Minecraft.getMinecraft().toggleFullscreen() }
    }
}