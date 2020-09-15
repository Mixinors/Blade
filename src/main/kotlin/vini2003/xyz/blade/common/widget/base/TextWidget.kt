package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.utilities.Drawings
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

open class TextWidget(var text: Text? = null) : AbstractWidget() {
	var shadow = false
	var color = 4210752

	override fun drawWidget(matrices: MatrixStack, provider: IRenderTypeBuffer) {
		if (hidden) return
		
		super.drawWidget(matrices, provider)

		text?.also {
			if (shadow) {
				Drawings.getTextRenderer()?.drawWithShadow(matrices, text, position.x, position.y, color)
			} else {
				Drawings.getTextRenderer()?.draw(matrices, text, position.x, position.y, color)
			}
		}
	}
}