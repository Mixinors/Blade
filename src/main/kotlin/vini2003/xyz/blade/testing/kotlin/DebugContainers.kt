package com.github.vini2003.blade.testing.kotlin

import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.fml.network.IContainerFactory

class DebugContainers {
	companion object {
		@JvmStatic
		val DEBUG_HANDLER: ContainerType<DebugContainer> = IForgeContainerType.create { windowId, inv, _ -> DebugContainer(windowId, inv.player) }

		@JvmStatic
		fun initialize() {

		}
	}
}