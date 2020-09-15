package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.common.collection.base.WidgetCollection
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import vini2003.xyz.blade.Blade

open class PanelWidget : AbstractWidget(), WidgetCollection {
	override val widgets: ArrayList<AbstractWidget> = ArrayList()

	var texture = PartitionedTexture(Blade.resourceLocation("textures/widget/panel.png"), 18F, 18F, 0.25F, 0.25F, 0.25F, 0.25F)

	override fun drawWidget(matrices: MatrixStack, provider: IRenderTypeBuffer) {
		if (hidden) return

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is IRenderTypeBuffer.Impl) provider.endBatch()

		widgets.forEach { it.drawWidget(matrices, provider) }
	}
}