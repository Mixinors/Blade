package vini2003.xyz.blade.common.widget.base

import vini2003.xyz.blade.client.utilities.Drawings
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.util.text.ITextComponent

open class TextWidget(var text: ITextComponent? = null) : AbstractWidget() {
	var shadow = false
	var color = 4210752

	override fun drawWidget(matrices: MatrixStack, provider: IRenderTypeBuffer) {
		if (hidden) return

		super.drawWidget(matrices, provider)

		text?.also {
			if (shadow) {
				Drawings.getTextRenderer()?.drawShadow(matrices, text, position.x, position.y, color)
			} else {
				Drawings.getTextRenderer()?.draw(matrices, text, position.x, position.y, color)
			}
		}
	}
}
