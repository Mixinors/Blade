package com.github.vini2003.blade.common.utilities

import net.minecraft.item.ItemStack
import kotlin.math.max
import kotlin.math.min

class Stacks {
	companion object {
		@JvmStatic
		fun merge(originalStackA: ItemStack, originalStackB: ItemStack, maxA: Int, maxB: Int, consumer: (ItemStack, ItemStack) -> Unit) {
			var stackA: ItemStack = originalStackA
			var stackB: ItemStack = originalStackB

			if (equal(stackA, stackB)) {
				val countA = stackA.count
				val countB = stackB.count

				val availableB = max(0, maxB - countB)

				stackB.count = stackB.count + (min(countA, availableB))
				stackA.count = max(countA - availableB, 0)
			} else {
				if (stackA.isEmpty && !stackB.isEmpty) {
					val countA = stackA.count
					val availableA = maxA - countA

					val countB = stackB.count

					stackA = stackB.copy()
					stackA.count = min(countB, availableA)
					stackB.count = stackB.count - min(countB, availableA)
				} else if (!stackA.isEmpty && stackB.isEmpty) {
					val countB = stackB.count
					val availableB = maxB - countB

					val countA = stackA.count

					stackB = stackA.copy()
					stackB.count = min(countA, availableB)
					stackA.count = stackA.count - min(countA, availableB)
				}
			}

			consumer.invoke(stackA, stackB)
		}

		@JvmStatic
		fun equal(stackA: ItemStack, stackB: ItemStack): Boolean {
			return ItemStack.isSame(stackA, stackB)
		}
	}
}