package com.github.vini2003.blade.common.utilities

import net.minecraft.profiler.IProfiler

import net.minecraft.resources.IFutureReloadListener
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.resource.ISelectiveResourceReloadListener
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

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

		@JvmStatic
		fun initialize() {
		}
	}
}