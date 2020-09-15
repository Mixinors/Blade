package com.github.vini2003.blade.testing.kotlin

import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedContainerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketBuffer
import net.minecraft.screen.Container
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class DebugCommands {
	companion object {
		@JvmStatic
		fun initialize() {
			CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, dedicated ->
				val debugNode = CommandManager.literal("debug")
						.executes { context: CommandContext<ServerCommandSource> ->
							context.source.player.openContainerScreen(object : ExtendedContainerFactory {
								override fun writeScreenOpeningData(player: ServerPlayerEntity, buffer: PacketBuffer) {
								}

								override fun getDisplayName(): Text {
									return TranslatableText("")
								}

								override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): Container? {
									return DebugContainer(syncId, player)
								}
							})
							1
						}.build()

				dispatcher.root.addChild(debugNode)
			})
		}
	}
}