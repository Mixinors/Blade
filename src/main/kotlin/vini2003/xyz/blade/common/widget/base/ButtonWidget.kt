package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.client.utilities.Texts
import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.collection.base.HandledWidgetCollection
import com.github.vini2003.blade.common.collection.base.WidgetCollection
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.audio.SimpleSound
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.util.SoundEvents
import net.minecraft.util.text.ITextComponent
import vini2003.xyz.blade.Blade

open class ButtonWidget(var clickAction: () -> Unit = {}) : AbstractWidget() {
	var textureOn = PartitionedTexture(Blade.resourceLocation("textures/widget/button_on.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.16666666666666666667F)
	var textureOff = PartitionedTexture(Blade.resourceLocation("textures/widget/button_off.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F)
	var textureFocus = PartitionedTexture(Blade.resourceLocation("textures/widget/button_on_focus.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F)

	var disabled: () -> Boolean = { false }

	var label: ITextComponent? = null

	override fun onAdded(handled: HandledWidgetCollection, immediate: WidgetCollection) {
		super.onAdded(handled, immediate)

		synchronize.add(Networks.MOUSE_CLICK)
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused || (!focused && handler != null && !handler!!.client)) {
			clickAction()

			if (handler!!.client) {
				playSound()
			}
		}

		super.onMouseClicked(x, y, button)
	}

	private fun playSound() {
		Instances.client().soundManager.play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F))
	}

	override fun drawWidget(matrices: MatrixStack, provider: IRenderTypeBuffer) {
		if (hidden) return

		val texture = if (disabled()) textureOff else if (focused) textureFocus else textureOn

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is IRenderTypeBuffer.Impl) provider.endBatch()

		label?.also {
			Drawings.getTextRenderer()?.drawWithShadow(matrices, label, position.x + (size.width / 2 - Texts.width(label!!) / 2), position.y + (size.height / 2 - Texts.height() / 2), color("button.label").toInt()) // 0xFCFCFC
		}
	}
}