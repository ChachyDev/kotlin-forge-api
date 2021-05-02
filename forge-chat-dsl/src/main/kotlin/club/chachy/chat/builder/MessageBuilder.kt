package club.chachy.chat.builder

import club.chachy.chat.builder.styles.CombinedStyle
import club.chachy.chat.builder.styles.StyleBuilder
import net.minecraft.util.EnumChatFormatting

class MessageBuilder internal constructor(private val space: String) {
    private val builder = StringBuilder()

    operator fun EnumChatFormatting.plus(format: EnumChatFormatting) = CombinedStyle(this, listOf(format))

    operator fun CombinedStyle.plus(formatting: EnumChatFormatting) =
        CombinedStyle(first, ArrayList<EnumChatFormatting>().apply {
            addAll(others)
            add(formatting)
        })

    fun withStyle(formatting: EnumChatFormatting, builder: StyleBuilder.() -> Unit) =
        createAndAppendStyle(listOf(formatting), builder)

    fun withStyle(combinedStyle: CombinedStyle, builder: StyleBuilder.() -> Unit) =
        createAndAppendStyle(
            mutableListOf(combinedStyle.first).apply { combinedStyle.others.forEach { add(it) } },
            builder
        )

    fun style(formatting: EnumChatFormatting, builder: StyleBuilder.() -> Unit) = withStyle(formatting, builder)

    fun style(combinedStyle: CombinedStyle, builder: StyleBuilder.() -> Unit) = withStyle(combinedStyle, builder)

    private fun createAndAppendStyle(formats: List<EnumChatFormatting>, builder: StyleBuilder.() -> Unit) {
        val style = StyleBuilder(formats)
        builder(style)
        +style.build()
    }

    operator fun String.unaryPlus() {
        builder.append(this).append(space)
    }

    internal fun build() = builder.toString()
}