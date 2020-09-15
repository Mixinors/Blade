package com.github.vini2003.blade.client.handler

import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.common.handler.BaseContainer
import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Positions
import net.minecraft.client.gui.screen.ingame.ContainerScreen
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

open class BaseContainerScreen<T : BaseContainer>(handler: BaseContainer, inventory: PlayerInventory, title: Text) : ContainerScreen<T>(handler as T, inventory, title) {
	override fun init() {
		handler.widgets.clear()
		handler.slots.clear()
		this.backgroundWidth = width
		this.backgroundHeight = height
		super.init()
		handler.initialize(width, height)
		handler.onLayoutChanged()
		handler.allWidgets.forEach { 
			it.onLayoutChanged()
		}
		Networks.toServer(Networks.INITIALIZE, Networks.ofInitialize(handler.syncId, width, height))
	}

	override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {

	}

	override fun isClickOutsideBounds(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean {
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

		val provider: IRenderTypeBuffer.Immediate = Instances.client().bufferBuilders.effectVertexConsumers

		handler.widgets.forEach {
			it.drawWidget(matrices, provider)
		}

		handler.allWidgets.forEach {
			if (!it.hidden && it.focused) {
				renderTooltip(matrices, it.getTooltip(), mouseX, mouseY)
			}
		}

		provider.draw()

		super.render(matrices, mouseX, mouseY, delta)

		super.drawMouseoverTooltip(matrices, mouseX, mouseY)
	}
}