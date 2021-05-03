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

    fun withStyle(vararg formatting: EnumChatFormatting, builder: StyleBuilder.() -> Unit) =
        createAndAppendStyle(listOf(*formatting), builder)

    fun withStyle(combinedStyle: CombinedStyle, builder: StyleBuilder.() -> Unit) =
        createAndAppendStyle(
            mutableListOf(combinedStyle.first).apply { addAll(combinedStyle.others) },
            builder
        )

    fun style(vararg formatting: EnumChatFormatting, builder: StyleBuilder.() -> Unit) =
        withStyle(
            CombinedStyle(
                formatting.firstOrNull() ?: error("You must specify a style"),
                ArrayList<EnumChatFormatting>().apply {
                    val list = formatting.toMutableList()
                    list.removeAt(0)
                    addAll(list)
                }), builder
        )

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