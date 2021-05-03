package club.chachy.test.tweaker

import club.chachy.test.tweaker.transformer.TestTransformer
import net.minecraftforge.common.ForgeVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
class TestTweaker : IFMLLoadingPlugin {
    override fun getASMTransformerClass() = arrayOf(TestTransformer::class.java.name)

    override fun getModContainerClass() = null

    override fun getSetupClass() = null

    override fun injectData(data: MutableMap<String, Any>?) {}

    override fun getAccessTransformerClass() = null
}