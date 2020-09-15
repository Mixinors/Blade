package com.github.vini2003.blade.common.utilities

import com.github.vini2003.blade.Blade
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.ResourceLocation

class Resources {
	companion object {
		fun initialize() {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
				private val id: ResourceLocation = Blade.resourceLocation("reload_listener")

				override fun apply(manager: ResourceManager?) {
					Styles.clear()
					Styles.load(manager!!)
				}

				override fun getFabricId(): ResourceLocation {
					return id
				}
			})
		}
	}
}