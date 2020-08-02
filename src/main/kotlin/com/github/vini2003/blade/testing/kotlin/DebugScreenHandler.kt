package com.github.vini2003.blade.testing.kotlin

import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.base.ButtonWidget
import com.github.vini2003.blade.common.widget.base.ItemWidget
import com.github.vini2003.blade.common.widget.base.SlotListWidget
import com.github.vini2003.blade.common.widget.base.SlotWidget
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.util.registry.Registry

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(DebugContainers.DEBUG_HANDLER, syncId, player) {
    init {
        addInventory(0, player.inventory)

        val slot = SlotWidget(0, player.inventory)

        slot.position = (Position({16F}, {16F}))
        slot.size = (Size({36F}, {36F}))

        val topButton = ButtonWidget {
        }

        topButton.position = (Position({slot.position.x + slot.size.width + 18F}, {slot.position.y}))
        topButton.size = (Size({120F}, {16F}))
        topButton.label = LiteralText("I am NinePatch,")

        val bottomButton = ButtonWidget {
        }

        bottomButton.position = (Position({slot.position.x + slot.size.width + 18F}, {topButton.position.y + topButton.size.height + 1}))
        bottomButton.size = (Size({160F}, {16F}))
        bottomButton.label = LiteralText("or something.")

        val item = ItemWidget()

        item.position = (Position({256F}, {16F}))
        item.size = (Size({16F}, {16F}))
        item.stack = ItemStack(Items.RED_WOOL)

        val inventory = SimpleInventory(4096)
        for (i in 0 until inventory.size()) inventory.setStack(i, ItemStack(Registry.ITEM.getRandom(player.world.random), player.world.random.nextInt(64)))

        val slots = SlotListWidget(inventory)

        slots.position = (Position({slot.position.x}, {slot.position.y + slot.size.width + 2}))
        slots.size = (Size({17 * 18F}, {11 * 18F}))

        addWidget(slot)

        addWidget(topButton)
        addWidget(bottomButton)

        addWidget(item)

        addWidget(slots)
    }
}