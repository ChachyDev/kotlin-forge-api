# kotlin-forge-api

kotlin-forge-api is a repository full of different APIs to be used by mods for Forge 1.8.9 to make modding easier!

**Warning!** To use any of these modules you must have the Jitpack repo added. To do this simply add
```groovy
maven {
    url "https://jitpack.io"
}
```
to your build.gradle!

## Modules

### forge-chat-dsl
This module simply adds a chat dsl making it easier to forge chat messages

To use this module simply add

```groovy
implementation "club.chachy.kotlin-forge-api:forge-chat-dsl:0.0.1"
```

Example:

```kotlin
Minecraft.getMinecraft().thePlayer.sendMessage {
    withStyle(green + bold + italic) {
        +"It's green, bold AND italic!"
    }
    +"Boring message"
}
```

### forge-event-dsl

To use this module simply add

```groovy
implementation "club.chachy.kotlin-forge-api:forge-event-dsl:0.0.1"
```
Example usage:

```kotlin
on<ClientChatReceivedEvent>()
    .filter { it.message.unformattedText.contains("Secret Fullscreen Tactic") }
    .subscribe {
        it.isCanceled = true
        Minecraft.getMinecraft().toggleFullscreen()
    }
```