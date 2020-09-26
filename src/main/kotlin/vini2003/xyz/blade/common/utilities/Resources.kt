package vini2003.xyz.blade.common.utilities

import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import java.util.concurrent.CompletableFuture

class Resources {
	companion object {
		fun reload(event: AddReloadListenerEvent) {
			event.addListener { _, manager, _, _, _, _ ->
				CompletableFuture.runAsync {
					Styles.clear()
					Styles.load(manager)
				}
			}
		}
	}
}
