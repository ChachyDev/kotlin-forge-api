package club.chachy.chat.builder.styles

import net.minecraft.util.EnumChatFormatting

data class CombinedStyle(
    val first: EnumChatFormatting,
    val others: List<EnumChatFormatting>
)