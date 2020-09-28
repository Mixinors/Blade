package vini2003.xyz.blade

import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.runWhenOn
import vini2003.xyz.blade.common.registry.NetworkRegistry
import vini2003.xyz.blade.common.utilities.Networks
import vini2003.xyz.blade.common.utilities.Resources
import vini2003.xyz.blade.testing.kotlin.DebugCommands
import vini2003.xyz.blade.testing.kotlin.DebugContainers
import vini2003.xyz.blade.testing.kotlin.DebugScreens

@Mod(Blade.MOD_ID)
object Blade {
	@SuppressWarnings
	const val MOD_ID = "blade"

	@JvmStatic
	val LOGGER: Logger = LogManager.getLogger(MOD_ID)

	@JvmStatic
	fun resourceLocation(string: String): ResourceLocation {
		return ResourceLocation(MOD_ID, string)
	}

	init {
		LOGGER.info("Blade is currently being loaded") // Added as a debug feature, just in case Kotlin for Forge decides to fuck up again

		NetworkRegistry.initialize()
		Networks.initialize()

		if (!FMLEnvironment.production) {
			DebugContainers.initialize()
			FORGE_BUS.addListener(DebugCommands::command)
			FORGE_BUS.addListener(Resources::reload)
			runWhenOn(Dist.CLIENT, DebugScreens::initialize)
		}
	}
}
