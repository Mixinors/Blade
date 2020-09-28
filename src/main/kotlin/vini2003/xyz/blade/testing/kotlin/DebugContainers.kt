package vini2003.xyz.blade.testing.kotlin

import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.MOD_BUS

class DebugContainers {
    companion object {
        @JvmStatic
        val CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.CONTAINERS, "blade")

        @JvmStatic
        val DEBUG_HANDLER = CONTAINER_TYPE.register("debug") { IForgeContainerType.create { windowId, inv, _ -> DebugContainer(windowId, inv.player) } }

        @JvmStatic
        fun initialize() {
            CONTAINER_TYPE.register(MOD_BUS)
        }
    }
}
