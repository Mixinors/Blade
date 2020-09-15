package vini2003.xyz.blade

import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Resources
import com.github.vini2003.blade.testing.kotlin.DebugCommands
import com.github.vini2003.blade.testing.kotlin.DebugContainers
import net.minecraft.util.ResourceLocation
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod("Blade")
class Blade {
	companion object {
		@SuppressWarnings
		const val MOD_ID = "blade"

		@JvmStatic
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		@JvmStatic
		fun resourceLocation(string: String): ResourceLocation {
			return ResourceLocation(MOD_ID, string)
		}
	}

	init {
		Resources.initialize()
		Networks.initialize()

		if (!FMLEnvironment.production) {
			DebugContainers.initialize()
			DebugCommands.initialize()


		}
	}
}