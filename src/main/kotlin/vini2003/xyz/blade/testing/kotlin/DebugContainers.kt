package com.github.vini2003.blade.testing.kotlin

import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.fml.network.IContainerFactory


class DebugContainers {
	companion object {
		@JvmStatic
		val DEBUG_HANDLER: ContainerType<DebugContainer> = ContainerType(::DebugContainer as IContainerFactory<DebugContainer>)

		@JvmStatic
		fun initialize() {

		}
	}
}