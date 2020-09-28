package vini2003.xyz.blade.testing.kotlin

import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.fml.loading.FMLEnvironment

class DebugCommands {
	companion object {
		fun command(event: RegisterCommandsEvent) {
			if (FMLEnvironment.production) return
			val debugNode = Commands.literal("debug_blade")
					.executes { context: CommandContext<CommandSource> ->
						context.source.playerOrException.openMenu(object : INamedContainerProvider {
							override fun getDisplayName(): ITextComponent {
								return StringTextComponent("")
							}

							override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): Container? {
								return DebugContainer(syncId, player)
							}
						})
						1
					}

            event.dispatcher.register(debugNode)
		}
	}
}
