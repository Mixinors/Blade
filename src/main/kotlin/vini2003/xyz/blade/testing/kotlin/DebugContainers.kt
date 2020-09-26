package vini2003.xyz.blade.testing.kotlin

import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.common.extensions.IForgeContainerType

class DebugContainers {
	companion object {
		@JvmStatic
		val DEBUG_HANDLER: ContainerType<DebugContainer> = IForgeContainerType.create { windowId, inv, _ -> DebugContainer(windowId, inv.player) }

		@JvmStatic
		fun initialize() {

		}
	}
}
