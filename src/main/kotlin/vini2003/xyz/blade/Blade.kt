package vini2003.xyz.blade

import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Resources
import com.github.vini2003.blade.testing.kotlin.DebugCommands
import com.github.vini2003.blade.testing.kotlin.DebugContainers
import com.github.vini2003.blade.testing.kotlin.DebugScreens
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import vini2003.xyz.blade.common.registry.NetworkRegistry

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

			DistExecutor.safeRunWhenOn(Dist.CLIENT) {
				DistExecutor.SafeRunnable {
					DebugScreens.initialize()
				}
			}
		}
	}
}
