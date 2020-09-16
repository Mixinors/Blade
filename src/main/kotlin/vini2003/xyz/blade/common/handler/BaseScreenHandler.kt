package com.github.vini2003.blade.common.handler

import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.common.utilities.Stacks
import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.collection.base.HandledWidgetCollection
import com.github.vini2003.blade.common.widget.base.AbstractWidget
import me.shedaniel.math.Rectangle
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ClickType
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

abstract class BaseContainer(type: ContainerType<out Container>, syncId: Int, val player: PlayerEntity) : Container(type, syncId), HandledWidgetCollection {
	override val widgets: ArrayList<AbstractWidget> = ArrayList()

	override val handler: BaseContainer
		get() = this

	val client = player.level.isClientSide

	var rectangle: Rectangle = Rectangle()

	abstract fun initialize(width: Int, height: Int)

	override fun onLayoutChanged() {
		var minimumX = Float.MAX_VALUE
		var minimumY = Float.MAX_VALUE
		var maximumX = 0F
		var maximumY = 0F

		widgets.forEach {
			if (it.x < minimumX) {
				minimumX = it.x
			}
			if (it.x + it.width > maximumX) {
				maximumX = it.x + it.width
			}
			if (it.y < minimumY) {
				minimumY = it.y
			}
			if (it.y + it.height > maximumY) {
				maximumY = it.y + it.height
			}
		}

		rectangle = Rectangle(minimumX.toInt(), minimumY.toInt(), (maximumX - minimumX).toInt(), (maximumY - minimumY).toInt())

		if (client) {
			onLayoutChangedDelegate()
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	fun onLayoutChangedDelegate() {
		val screen = Instances.client().screen as? ContainerScreen<*> ?: return

		screen.leftPos = rectangle.minX
		screen.topPos = rectangle.minY

		screen.imageWidth = rectangle.width
		screen.imageHeight = rectangle.height
	}

	override fun addWidget(widget: AbstractWidget) {
		widgets.add(widget)
		widget.onAdded(this, this)

		widgets.forEach { _ ->
			widget.onLayoutChanged()
		}
	}

	override fun removeWidget(widget: AbstractWidget) {
		widgets.remove(widget)
		widget.onRemoved(this, this)

		widgets.forEach { _ ->
			widget.onLayoutChanged()
		}
	}

	open fun handlePacket(id: ResourceLocation, buf: PacketBuffer) {
		val hash = buf.readInt()

		allWidgets.forEach {
			val buffer = PacketBuffer(buf.copy())
			if (it.hash == hash) {
				when (id) {
					Networks.MOUSE_MOVE -> it.onMouseMoved(buffer.readFloat(), buffer.readFloat())
					Networks.MOUSE_CLICK -> it.onMouseClicked(buffer.readFloat(), buffer.readFloat(), buffer.readInt())
					Networks.MOUSE_RELEASE -> it.onMouseReleased(buffer.readFloat(), buffer.readFloat(), buffer.readInt())
					Networks.MOUSE_DRAG -> it.onMouseDragged(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readDouble(), buffer.readDouble())
					Networks.MOUSE_SCROLL -> it.onMouseScrolled(buffer.readFloat(), buffer.readFloat(), buffer.readDouble())
					Networks.KEY_PRESS -> it.onKeyPressed(buffer.readInt(), buffer.readInt(), buffer.readInt())
					Networks.KEY_RELEASE -> it.onKeyReleased(buffer.readInt(), buffer.readInt(), buffer.readInt())
					Networks.CHAR_TYPE -> it.onCharTyped(buffer.readChar(), buffer.readInt())
					Networks.FOCUS_GAIN -> it.onFocusGained()
					Networks.FOCUS_RELEASE -> it.onFocusReleased()
				}

				return
			}
		}
	}

	public override fun addSlot(slot: Slot?): Slot {
		return super.addSlot(slot)
	}

	fun removeSlot(slot: Slot?) {
		val index = slot!!.index
		slots.remove(slot)
		slots.forEach {
			if (it.index >= index) {
				--it.index
			}
		}
	}

	override fun doClick(slotNumber: Int, button: Int, actionType: ClickType, playerEntity: PlayerEntity): ItemStack {
		return when (actionType) {
			ClickType.QUICK_MOVE -> {
				if (slotNumber >= 0 && slotNumber < slots.size) {
					val slot = slots[slotNumber]

					if (slot != null && !slot.item.isEmpty && slot.mayPickup(playerEntity)) {
						for (newSlotNumber in 0 until slots.size) {
							val newSlot = slots[newSlotNumber]

							if (!newSlot.item.isEmpty) {
								if (newSlot.mayPlace(slot.item)) {
									if (newSlot.container !is PlayerInventory && newSlot != slot && newSlot.container != slot.container) {
										Stacks.merge(slot.item, newSlot.item, slot.item.maxStackSize, newSlot.item.maxStackSize) { stackA, stackB ->
											slot.set(stackA)
											newSlot.set(stackB)
										}
									}

									if (newSlot.container is PlayerInventory && newSlot != slot && newSlot.container != slot.container) {
										Stacks.merge(slot.item, newSlot.item, slot.item.maxStackSize, newSlot.item.maxStackSize) { stackA, stackB ->
											slot.set(stackA)
											newSlot.set(stackB)
										}
									}

									if (slot.item.isEmpty) break
								}
							}
						}
						for (newSlotNumber in 0 until slots.size) {
							val newSlot = slots[newSlotNumber]

							if (newSlot.mayPlace(slot.item)) {
								if (newSlot.container !is PlayerInventory && newSlot != slot && newSlot.container != slot.container) {
									Stacks.merge(slot.item, newSlot.item, slot.item.maxStackSize, newSlot.item.maxStackSize) { stackA, stackB ->
										slot.set(stackA)
										newSlot.set(stackB)
									}
								}

								if (newSlot.container is PlayerInventory && newSlot != slot && newSlot.container != slot.container) {
									Stacks.merge(slot.item, newSlot.item, slot.item.maxStackSize, newSlot.item.maxStackSize) { stackA, stackB ->
										slot.set(stackA)
										newSlot.set(stackB)
									}
								}

								if (slot.item.isEmpty) break
							}
						}
					}
				}

				return ItemStack.EMPTY
			}
			else -> super.doClick(slotNumber, button, actionType, playerEntity)
		}
	}
}