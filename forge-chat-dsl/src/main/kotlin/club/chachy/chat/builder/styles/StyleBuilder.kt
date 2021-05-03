package club.chachy.chat.builder.styles

import net.minecraft.util.EnumChatFormatting

class StyleBuilder(formatting: List<EnumChatFormatting>) {
    internal val builder = StringBuilder()

    init {
        formatting.forEach {
            builder.append(it.toString())
        }
    }

    operator fun String.unaryPlus() {
        builder.append(this)
    }

    internal fun build() = builder.toString() + EnumChatFormatting.RESET
}