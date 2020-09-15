package com.github.vini2003.blade.testing.kotlin

import com.github.vini2003.blade.Blade.Companion.identifier
import net.fabricmc.fabric.api.screenhandler.v1.ContainerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketBuffer
import net.minecraft.screen.ContainerType

class DebugContainers {
	companion object {
		@JvmStatic
		val DEBUG_HANDLER: ContainerType<DebugContainer> = ContainerRegistry.registerExtended(identifier("debug_handler")) { synchronizationID: Int, inventory: PlayerInventory, buffer: PacketBuffer? ->
			DebugContainer(
					synchronizationID,
					inventory.player
			)
		}

		@JvmStatic
		fun initialize() {

		}
	}
}