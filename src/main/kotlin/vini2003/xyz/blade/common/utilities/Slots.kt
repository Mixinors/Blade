package vini2003.xyz.blade.common.utilities

import vini2003.xyz.blade.common.miscellaneous.Position
import vini2003.xyz.blade.common.miscellaneous.PositionHolder
import vini2003.xyz.blade.common.miscellaneous.Size
import vini2003.xyz.blade.common.collection.base.WidgetCollection
import vini2003.xyz.blade.common.widget.base.SlotWidget
import net.minecraft.inventory.IInventory
import java.util.*

class Slots {
	companion object {
		@JvmStatic
		fun addPlayerInventory(position: PositionHolder, size: Size, parent: WidgetCollection, inventory: IInventory): Collection<SlotWidget>? {
			val set: MutableCollection<SlotWidget> = addArray(position, size, parent, 9, 9, 3, inventory)
			set.addAll(addArray(Position.of(position, 0, size.height * 3 + 4), size, parent, 0, 9, 1, inventory))
			return set
		}

		@JvmStatic
		fun addArray(position: PositionHolder, size: Size, parent: WidgetCollection, slotNumber: Int, arrayWidth: Int, arrayHeight: Int, inventory: IInventory): MutableCollection<SlotWidget> {
			val set: MutableCollection<SlotWidget> = HashSet()
			for (y in 0 until arrayHeight) {
				for (x in 0 until arrayWidth) {
					val slot = SlotWidget(slotNumber + y * arrayWidth + x, inventory)
					slot.position = Position.of(position, size.width * x, size.height * y)
					slot.size = Size.of(size)

					parent.addWidget(slot)
				}
			}
			return set
		}
	}
}
