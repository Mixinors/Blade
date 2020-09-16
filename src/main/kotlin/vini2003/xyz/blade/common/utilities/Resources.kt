package com.github.vini2003.blade.common.utilities

import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.util.concurrent.CompletableFuture

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
class Resources {
	companion object {
		@JvmStatic
		@SubscribeEvent
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