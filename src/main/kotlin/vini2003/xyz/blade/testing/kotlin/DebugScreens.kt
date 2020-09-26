package vini2003.xyz.blade.testing.kotlin

import net.minecraft.client.gui.ScreenManager
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class DebugScreens {
	companion object {
		fun initialize() {
			ScreenManager.register(DebugContainers.DEBUG_HANDLER) { handler: DebugContainer, inventory: PlayerInventory, title: ITextComponent -> DebugContainerScreen(handler, inventory, title) }
		}
	}
}
