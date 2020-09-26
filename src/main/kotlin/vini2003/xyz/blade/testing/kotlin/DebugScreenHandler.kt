package vini2003.xyz.blade.testing.kotlin

import vini2003.xyz.blade.common.handler.BaseContainer
import net.minecraft.entity.player.PlayerEntity

class DebugContainer(syncId: Int, player: PlayerEntity) : BaseContainer(DebugContainers.DEBUG_HANDLER, syncId, player) {
	override fun initialize(width: Int, height: Int) {
		panel {
			val panel = this

			position(48F, 48F)
			size(96F, 96F)

			button {
				size(18F, 18F)

				floatTopLeftIn()
			}

			button {
				size(18F, 18F)

				floatMiddleLeftIn()
			}

			button {
				size(18F, 18F)

				floatBottomLeftIn()
			}

			button {
				size(18F, 18F)

				floatMiddleBottomIn()
			}

			button {
				size(18F, 18F)

				floatBottomRightIn()
			}

			button {
				size(18F, 18F)

				floatMiddleRightIn()
			}

			button {
				size(18F, 18F)

				floatTopRightIn()
			}

			button {
				size(18F, 18F)

				floatMiddleTopIn()
			}

			button {
				size(18F, 18F)

				floatTopLeftOut()
			}

			button {
				size(18F, 18F)

				floatMiddleLeftOut()
			}

			button {
				size(18F, 18F)

				floatBottomLeftOut()
			}

			button {
				size(18F, 18F)

				floatMiddleBottomOut()
			}

			button {
				size(18F, 18F)

				floatBottomRightOut()
			}

			button {
				size(18F, 18F)

				floatMiddleRightOut()
			}

			button {
				size(18F, 18F)

				floatTopRightOut()
			}

			button {
				size(18F, 18F)

				floatMiddleTopOut()
			}

			button {
				size(18F, 18F)

				floatMiddle()

				click {
					println("Oopsie!")
				}
			}

			playerInventory(panel, player.inventory)
		}
	}

	override fun stillValid(player: PlayerEntity?): Boolean = true
}
