package vini2003.xyz.blade.client.handler

import vini2003.xyz.blade.client.utilities.Instances
import vini2003.xyz.blade.common.handler.BaseContainer
import vini2003.xyz.blade.common.utilities.Networks
import vini2003.xyz.blade.common.utilities.Positions
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

open class BaseContainerScreen<T : BaseContainer>(val handler: BaseContainer, inventory: PlayerInventory, title: ITextComponent) : ContainerScreen<T>(handler as T, inventory, title) {
	override fun init() {
		handler.widgets.clear()
		handler.slots.clear()
		this.imageWidth = width
		this.imageHeight = height
		super.init()
		handler.initialize(width, height)
		handler.onLayoutChanged()
		handler.allWidgets.forEach {
			it.onLayoutChanged()
		}
		Networks.toServer(Networks.INITIALIZE, Networks.ofInitialize(handler.containerId, width, height))
	}

	override fun hasClickedOutside(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean {
		return handler.allWidgets.none { widget -> widget.isWithin(mouseX.toFloat(), mouseY.toFloat()) }
	}

	override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
		handler.widgets.forEach {
			it.onMouseClicked(mouseX.toFloat(), mouseY.toFloat(), button)
		}

		return super.mouseClicked(mouseX, mouseY, button)
	}

	override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
		handler.widgets.forEach {
			it.onMouseReleased(mouseX.toFloat(), mouseY.toFloat(), button)
		}

		return super.mouseReleased(mouseX, mouseY, button)
	}

	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		handler.widgets.forEach {
			it.onMouseDragged(mouseX.toFloat(), mouseY.toFloat(), button, deltaX, deltaY)
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
	}

	override fun mouseMoved(mouseX: Double, mouseY: Double) {
		handler.widgets.forEach {
			it.onMouseMoved(mouseX.toFloat(), mouseY.toFloat())
		}

		Positions.mouseX = mouseX.toFloat()
		Positions.mouseY = mouseY.toFloat()

		super.mouseMoved(mouseX, mouseY)
	}

	override fun mouseScrolled(mouseX: Double, mouseY: Double, deltaY: Double): Boolean {
		handler.widgets.forEach {
			it.onMouseScrolled(mouseX.toFloat(), mouseY.toFloat(), deltaY)
		}

		return super.mouseScrolled(mouseX, mouseY, deltaY)
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		handler.widgets.forEach {
			it.onKeyPressed(keyCode, scanCode, keyModifiers)
		}

		return super.keyPressed(keyCode, scanCode, keyModifiers)
	}

	override fun keyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		handler.widgets.forEach {
			it.onKeyReleased(keyCode, scanCode, keyModifiers)
		}

		return super.keyReleased(keyCode, scanCode, keyModifiers)
	}

	override fun charTyped(character: Char, keyCode: Int): Boolean {
		handler.widgets.forEach {
			it.onCharTyped(character, keyCode)
		}

		return super.charTyped(character, keyCode)
	}

	override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
		super.renderBackground(matrices)

		val provider: IRenderTypeBuffer.Impl = Instances.client().renderBuffers().bufferSource()

		handler.widgets.forEach {
			it.drawWidget(matrices, provider)
		}

		handler.allWidgets.forEach {
			if (!it.hidden && it.focused) {
				renderComponentTooltip(matrices, it.getTooltip(), mouseX, mouseY)
			}
		}

		provider.endBatch()

		super.render(matrices, mouseX, mouseY, delta)

		super.renderTooltip(matrices, mouseX, mouseY)
	}

	override fun renderBg(p_230450_1_: MatrixStack, p_230450_2_: Float, p_230450_3_: Int, p_230450_4_: Int) {
		TODO("Not yet implemented")
	}
}
