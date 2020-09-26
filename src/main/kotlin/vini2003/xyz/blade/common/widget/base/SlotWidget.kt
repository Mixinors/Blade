package vini2003.xyz.blade.common.widget.base

import vini2003.xyz.blade.client.data.PartitionedTexture
import vini2003.xyz.blade.client.utilities.Instances
import vini2003.xyz.blade.common.handler.BaseContainer
import vini2003.xyz.blade.common.collection.base.HandledWidgetCollection
import vini2003.xyz.blade.common.collection.base.WidgetCollection
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.Slot
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import vini2003.xyz.blade.Blade
import kotlin.properties.Delegates

open class SlotWidget(
	var slot: Int,
	var inventory: IInventory,
	var slotProvider: (IInventory, Int, Int, Int) -> Slot
) : AbstractWidget() {
	constructor(slot: Int, inventory: IInventory) : this(slot, inventory, { inv, id, x, y -> Slot(inv, id, x, y) })

	var backendSlot: Slot? = null

	var texture = PartitionedTexture(Blade.resourceLocation("textures/widget/slot.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F)

	override var hidden: Boolean by Delegates.observable(false) { _, _, _ ->
		updateSlotPosition()
	}

	fun updateSlotPosition() {
		if (hidden) {
			backendSlot?.x = Int.MAX_VALUE
			backendSlot?.y = Int.MAX_VALUE
		} else {
			backendSlot?.x = slotX
			backendSlot?.y = slotY
		}

		if (handler?.client == true) {
			updateSlotPositionDelegate()
		}
	}

	@OnlyIn(Dist.CLIENT)
	fun updateSlotPositionDelegate() {
		val screen = Instances.client().screen as? ContainerScreen<*> ?: return

		backendSlot?.x = backendSlot?.x?.minus(screen.guiLeft)
		backendSlot?.y = backendSlot?.y?.minus(screen.guiTop)
	}

	private val slotX: Int
		get() = (x + (if (size.width <= 18) 1F else size.width / 2F - 9F)).toInt()

	private val slotY: Int
		get() = (y + (if (size.height <= 18) 1F else size.height / 2F - 9F)).toInt()

	override fun onAdded(handled: HandledWidgetCollection, immediate: WidgetCollection) {
		super.onAdded(handled, immediate)
		backendSlot = slotProvider(inventory, slot, slotX, slotY)
		backendSlot!!.index = slot

		if (handled is BaseContainer) {
			handled.addSlot(backendSlot)
		}
	}

	override fun onRemoved(handled: HandledWidgetCollection, immediate: WidgetCollection) {
		super.onRemoved(handled, immediate)

		if (handled is BaseContainer) {
			handled.removeSlot(backendSlot)
		}
	}

	override fun onLayoutChanged() {
		super.onLayoutChanged()
		updateSlotPosition()
	}

	override fun drawWidget(matrices: MatrixStack, provider: IRenderTypeBuffer) {
		if (hidden) return

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		super.drawWidget(matrices, provider)
	}
}
