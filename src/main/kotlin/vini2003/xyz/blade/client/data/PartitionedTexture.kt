package com.github.vini2003.blade.client.data

import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.common.miscellaneous.Color
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.ResourceLocation

class PartitionedTexture(
		private val texture: ResourceLocation,
		private val originalWidth: Float,
		private val originalHeight: Float,
		leftPadding: Float,
		rightPadding: Float,
		topPadding: Float,
		bottomPadding: Float
) {
	private val topLeft = Part(0F, 0F, leftPadding, topPadding)
	private val topRight = Part(1F - rightPadding, 0F, 1F, topPadding)
	private val bottomLeft = Part(0F, 1F - bottomPadding, leftPadding, 1F)
	private val bottomRight = Part(1F - rightPadding, 1F - bottomPadding, 1F, 1F)
	private val middleLeft = Part(0F, topPadding, leftPadding, 1F - bottomPadding)
	private val middleRight = Part(1F - rightPadding, topPadding, 1F, 1F - bottomPadding)
	private val middleTop = Part(leftPadding, 0F, 1F - rightPadding, topPadding)
	private val middleBottom = Part(leftPadding, 1F - bottomPadding, 1F - rightPadding, 1F)
	private val center = Part(leftPadding, topPadding, 1F - rightPadding, 1F - bottomPadding)

	fun draw(matrices: MatrixStack, provider: IRenderTypeBuffer, x: Float, y: Float, width: Float, height: Float) {
		val scaleWidth = width / originalWidth
		val scaleHeight = height / originalHeight

		val topLeftWidth = width * (topLeft.uE - topLeft.uS) / scaleWidth
		val topLeftHeight = height * (topLeft.vE - topLeft.vS) / scaleHeight

		val topRightWidth = width * (topRight.uE - topRight.uS) / scaleWidth
		val topRightHeight = height * (topRight.vE - topRight.vS) / scaleHeight


		val bottomLeftWidth = width * (bottomLeft.uE - bottomLeft.uS) / scaleWidth
		val bottomLeftHeight = height * (bottomLeft.vE - bottomLeft.vS) / scaleHeight

		val bottomRightWidth = width * (bottomRight.uE - bottomRight.uS) / scaleWidth
		val bottomRightHeight = height * (bottomRight.vE - bottomRight.vS) / scaleHeight


		val middleTopWidth = width * (middleTop.uE + middleTop.uS) - topLeftWidth - topRightWidth
		val middleTopHeight = height * (middleTop.vE - middleTop.vS) / scaleHeight

		val middleBottomWidth = width * (middleBottom.uE + middleBottom.uS) - bottomLeftWidth - bottomRightWidth
		val middleBottomHeight = height * (middleBottom.vE - middleBottom.vS) / scaleHeight


		val middleLeftWidth = width * (middleLeft.uE - middleLeft.uS) / scaleWidth
		var middleLeftHeight = height * (middleLeft.vE + middleLeft.vS) - topLeftHeight - topRightHeight

		val middleRightWidth = width * (middleRight.uE - middleRight.uS) / scaleWidth
		var middleRightHeight = height * (middleRight.vE + middleRight.vS) - topLeftHeight - topRightHeight


		val centerWidth = width * (center.uE + center.uS) - topLeftWidth - topRightWidth
		var centerHeight = height * (center.vE + center.vS) - topLeftHeight - topRightHeight


		val heightMultiplier = height / (topLeftHeight + middleLeftHeight + bottomLeftHeight)

		middleLeftHeight *= heightMultiplier
		middleRightHeight *= heightMultiplier
		centerHeight *= heightMultiplier


		val layer = Layers.get(texture)

		val color = Color.standard()

		Drawings.drawTexturedQuad(matrices, provider, layer, x, y, 0F, topLeftWidth, topLeftHeight, topLeft.uS, topLeft.vS, topLeft.uE, topLeft.vE, 0x00F000F0, color, texture)

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth, y, 0F, middleTopWidth, middleTopHeight, middleTop.uS, middleTop.vS, middleTop.uE, middleTop.vE, 0x00F000F0, color, texture)

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth + middleTopWidth, y, 0F, topRightWidth, topRightHeight, topRight.uS, topRight.vS, topRight.uE, topRight.vE, 0x00F000F0, color, texture)

		Drawings.drawTexturedQuad(matrices, provider, layer, x, y + topRightHeight, 0F, middleLeftWidth, middleLeftHeight, middleLeft.uS, middleLeft.vS, middleLeft.uE, middleLeft.vE, 0x00F000F0, color, texture)

		Drawings.drawTexturedQuad(matrices, provider, layer, x + middleLeftWidth + middleTopWidth, y + topRightHeight, 0F, middleRightWidth, middleRightHeight, middleRight .uS, middleRight.vS, middleRight.uE, middleRight.vE, 0x00F000F0, color, texture)

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth, y + topLeftHeight, 0F, centerWidth, centerHeight, center.uS, center.vS, center.uE, center.vE, 0x00F000F0, color, texture)

		Drawings.drawTexturedQuad(matrices, provider, layer, x, y + centerHeight + topLeftHeight, 0F, bottomLeftWidth, bottomLeftHeight, bottomLeft.uS, bottomLeft.vS, bottomLeft.uE, bottomLeft.vE, 0x00F000F0, color, texture)

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth, y + centerHeight + topLeftHeight, 0F, middleBottomWidth, middleBottomHeight, middleBottom.uS, middleBottom.vS, middleBottom.uE, middleBottom.vE, 0x00F000F0, color, texture)

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth + middleBottomWidth, y + centerHeight + topLeftHeight, 0F, bottomRightWidth, bottomRightHeight, bottomRight.uS, bottomRight.vS, bottomRight.uE, bottomRight.vE, 0x00F000F0, color, texture)
	}

	data class Part(val uS: Float, val vS: Float, val uE: Float, val vE: Float)
}