package vini2003.xyz.blade.testing.kotlin

import vini2003.xyz.blade.client.handler.BaseContainerScreen
import vini2003.xyz.blade.common.handler.BaseContainer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class DebugContainerScreen(handler: BaseContainer, inventory: PlayerInventory, title: ITextComponent) : BaseContainerScreen<DebugContainer>(handler, inventory, title) {
	init {
	}
}
