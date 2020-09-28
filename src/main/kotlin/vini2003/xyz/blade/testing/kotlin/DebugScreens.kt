package vini2003.xyz.blade.testing.kotlin

import net.minecraft.client.gui.ScreenManager
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

class DebugScreens {
	companion object {
		fun initialize() {
			MOD_BUS.addListener(this::onClientSetup)
		}

		fun onClientSetup(event: FMLClientSetupEvent) {
			ScreenManager.register(DebugContainers.DEBUG_HANDLER.get()) { handler: DebugContainer, inventory: PlayerInventory, title: ITextComponent -> DebugContainerScreen(handler, inventory, title) }
		}
	}
}
