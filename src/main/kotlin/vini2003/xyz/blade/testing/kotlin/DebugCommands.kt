package com.github.vini2003.blade.testing.kotlin

import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.event.CommandEvent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class DebugCommands {
	companion object {
		@JvmStatic
		@SubscribeEvent
		fun command(event: RegisterCommandsEvent) {
			val debugNode = Commands.literal("debug")
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
					}.build()

			event.dispatcher.root.addChild(debugNode)
		}
		@JvmStatic
		fun initialize() {
		}
	}
}