package com.github.vini2003.blade.client.utilities

import com.github.vini2003.blade.common.miscellaneous.Color
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.util.ResourceLocation

class Drawings {
	companion object {
		@JvmStatic
		fun drawQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, sX: Float, sY: Float, color: Color) {
			drawQuad(matrices, provider, layer, x, y, 0F, sX, sY, 0x00f000f0, color)
		}

		@JvmStatic
		fun drawQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, color: Color) {
			drawQuad(matrices, provider, layer, x, y, z, sX, sY, 0x00f000f0, color)
		}

		@JvmStatic
		fun drawQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, light: Int, color: Color) {
			val consumer = provider.getBuffer(layer)

			consumer.vertex(matrices.last().pose(), x, y, z).color(color.r, color.g, color.b, color.a).uv2(light).endVertex()
			consumer.vertex(matrices.last().pose(), x, y + sY, z).color(color.r, color.g, color.b, color.a).uv2(light).endVertex()
			consumer.vertex(matrices.last().pose(), x + sX, y + sY, z).color(color.r, color.g, color.b, color.a).uv2(light).endVertex()
			consumer.vertex(matrices.last().pose(), x + sX, y, z).color(color.r, color.g, color.b, color.a).uv2(light).endVertex()
		}


		@JvmStatic
		fun drawGradientQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, startX: Float, startY: Float, endX: Float, endY: Float, colorStart: Color, colorEnd: Color) {
			drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, 0F, 0f, 0f, 1f, 1f, 0x00f000f0, colorStart, colorEnd, false)
		}

		@JvmStatic
		fun drawGradientQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, startX: Float, startY: Float, endX: Float, endY: Float, z: Float, colorStart: Color, colorEnd: Color) {
			drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, z, 0f, 0f, 1f, 1f, 0x00f000f0, colorStart, colorEnd, false)
		}

		@JvmStatic
		fun drawGradientQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, startX: Float, startY: Float, endX: Float, endY: Float, z: Float, light: Int, colorStart: Color, colorEnd: Color) {
			drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, z, 0f, 0f, 1f, 1f, light, colorStart, colorEnd, false)
		}

		@JvmStatic
		fun drawGradientQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, startX: Float, startY: Float, endX: Float, endY: Float, z: Float, uS: Float, vS: Float, uE: Float, vE: Float, light: Int, colorStart: Color, colorEnd: Color, textured: Boolean) {
			val consumer = provider.getBuffer(layer)

			consumer.vertex(matrices.last().pose(), endX, startY, z + 201).color(colorStart.r, colorStart.g, colorStart.b, colorStart.a).uv(uS, vS).uv2(light).normal(matrices.last().normal(), 0f, 1f, 0f).endVertex()
			consumer.vertex(matrices.last().pose(), startX, startY, z + 201).color(colorStart.r, colorStart.g, colorStart.b, colorStart.a).uv(uS, vE).uv2(light).normal(matrices.last().normal(), 0f, 1f, 0f).endVertex()
			consumer.vertex(matrices.last().pose(), startX, endY, z + 201).color(colorEnd.r, colorEnd.g, colorEnd.b, colorEnd.a).uv(uE, vS).uv2(light).normal(matrices.last().normal(), 0f, 1f, 0f).endVertex()
			consumer.vertex(matrices.last().pose(), endX, endY, z + 201).color(colorEnd.r, colorEnd.g, colorEnd.b, colorEnd.a).uv(uE, vE).uv2(light).normal(matrices.last().normal(), 0f, 1f, 0f).endVertex()
		}

		@JvmStatic
		fun drawPanel(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, sX: Float, sY: Float, shadow: Color, panel: Color, highlight: Color, outline: Color) {
			drawPanel(matrices, provider, layer, x, y, 0F, sX, sY, shadow, panel, highlight, outline)
		}

		@JvmStatic
		fun drawPanel(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, shadow: Color, panel: Color, hilight: Color, outline: Color) {
			drawQuad(matrices, provider, layer, x + 3, y + 3, z, sX - 6, sY - 6, 0x00F000F0, panel)
			drawQuad(matrices, provider, layer, x + 2, y + 1, z, sX - 4, 2F, 0x00F000F0, hilight)
			drawQuad(matrices, provider, layer, x + 2, y + sY - 3, z, sX - 4, 2F, 0x00F000F0, shadow)
			drawQuad(matrices, provider, layer, x + 1, y + 2, z, 2F, sY - 4, 0x00F000F0, hilight)
			drawQuad(matrices, provider, layer, x + sX - 3, y + 2, z, 2F, sY - 4, 0x00F000F0, shadow)
			drawQuad(matrices, provider, layer, x + sX - 3, y + 2, z, 1F, 1F, 0x00F000F0, panel)
			drawQuad(matrices, provider, layer, x + 2, y + sY - 3, z, 1F, 1F, 0x00F000F0, panel)
			drawQuad(matrices, provider, layer, x + 3, y + 3, z, 1F, 1F, 0x00F000F0, hilight)
			drawQuad(matrices, provider, layer, x + sX - 4, y + sY - 4, z, 1F, 1F, 0x00F000F0, shadow)
			drawQuad(matrices, provider, layer, x + 2, y, z, sX - 4, 1F, 0x00F000F0, outline)
			drawQuad(matrices, provider, layer, x, y + 2, z, 1F, sY - 4, 0x00F000F0, outline)
			drawQuad(matrices, provider, layer, x + sX - 1, y + 2, z, 1F, sY - 4, 0x00F000F0, outline)
			drawQuad(matrices, provider, layer, x + 2, y + sY - 1, z, sX - 4, 1F, 0x00F000F0, outline)
			drawQuad(matrices, provider, layer, x + 1, y + 1, z, 1F, 1F, 0x00F000F0, outline)
			drawQuad(matrices, provider, layer, x + 1, y + sY - 2, z, 1F, 1F, 0x00F000F0, outline)
			drawQuad(matrices, provider, layer, x + sX - 2, y + 1, z, 1F, 1F, 0x00F000F0, outline)
			drawQuad(matrices, provider, layer, x + sX - 2, y + sY - 2, z, 1F, 1F, 0x00F000F0, outline)
		}

		@JvmStatic
		fun drawBeveledPanel(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, sX: Float, sY: Float, topLeft: Color, panel: Color, bottomRight: Color) {
			drawBeveledPanel(matrices, provider, layer, x, y, 0F, sX, sY, 0x00F000F0, topLeft, panel, bottomRight)
		}

		@JvmStatic
		fun drawBeveledPanel(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, topLeft: Color, panel: Color, bottomRight: Color) {
			drawBeveledPanel(matrices, provider, layer, x, y, z, sX, sY, 0x00F000F0, topLeft, panel, bottomRight)
		}

		@JvmStatic
		fun drawBeveledPanel(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, light: Int, topLeft: Color, panel: Color, bottomRight: Color) {
			drawQuad(matrices, provider, layer, x, y, z, sX, sY, light, panel)
			drawQuad(matrices, provider, layer, x, y, z, sX, 1F, light, topLeft)
			drawQuad(matrices, provider, layer, x, y + 1, z, 1F, sY - 1, light, topLeft)
			drawQuad(matrices, provider, layer, x + sX - 1, y + 1, z, 1F, sY - 1, light, bottomRight)
			drawQuad(matrices, provider, layer, x, y + sY - 1, z, sX - 1, 1F, light, bottomRight)
		}

		@JvmStatic
		fun drawTexturedQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, sX: Float, sY: Float, texture: ResourceLocation?) {
			drawTexturedQuad(matrices, provider, layer, x, y, 0F, sX, sY, 0F, 0F, 1F, 1F, 0x00F000F0, Color.standard(), texture)
		}

		@JvmStatic
		fun drawTexturedQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, texture: ResourceLocation?) {
			drawTexturedQuad(matrices, provider, layer, x, y, z, sX, sY, 0F, 0F, 1F, 1F, 0x00F000F0, Color.standard(), texture)
		}

		@JvmStatic
		fun drawTexturedQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, color: Color, texture: ResourceLocation?) {
			drawTexturedQuad(matrices, provider, layer, x, y, z, sX, sY, 0F, 0F, 1F, 1F, 0x00F000F0, color, texture)
		}

		@JvmStatic
		fun drawTexturedQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, light: Int, color: Color, texture: ResourceLocation?) {
			drawTexturedQuad(matrices, provider, layer, x, y, z, sX, sY, 0F, 0F, 1F, 1F, light, color, texture)
		}

		@JvmStatic
		fun drawTexturedQuad(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, sX: Float, sY: Float, u0: Float, v0: Float, u1: Float, v1: Float, light: Int, color: Color, texture: ResourceLocation?) {
			getTextureManager()?.bind(texture)

			val consumer = provider.getBuffer(layer)

			consumer.vertex(matrices.last().pose(), x, y + sY, z).color(color.r, color.g, color.b, color.a).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrices.last().normal(), 0F, 0F, 0F).endVertex()
			consumer.vertex(matrices.last().pose(), x + sX, y + sY, z).color(color.r, color.g, color.b, color.a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrices.last().normal(), 0F, 0F, 0F).endVertex()
			consumer.vertex(matrices.last().pose(), x + sX, y, z).color(color.r, color.g, color.b, color.a).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrices.last().normal(), 0F, 0F, 0F).endVertex()
			consumer.vertex(matrices.last().pose(), x, y, z).color(color.r, color.g, color.b, color.a).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrices.last().normal(), 0F, 0F, 0F).endVertex()
		}

		@JvmStatic
		fun drawTooltip(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, width: Float, height: Float, shadowStart: Color, shadowEnd: Color, backgroundStart: Color, backgroundEnd: Color, outlineStart: Color, outlineEnd: Color) {
			drawTooltip(matrices, provider, layer, x, y, 256F, width, height, shadowStart, shadowEnd, backgroundStart, backgroundEnd, outlineStart, outlineEnd)
		}

		@JvmStatic
		fun drawTooltip(matrices: MatrixStack, provider: IRenderTypeBuffer, layer: RenderType, x: Float, y: Float, z: Float, width: Float, height: Float, shadowStart: Color, shadowEnd: Color, backgroundStart: Color, backgroundEnd: Color, outlineStart: Color, outlineEnd: Color) {
			drawGradientQuad(matrices, provider, layer, x - 3, y - 4, x + width + 3, y - 3, z, shadowStart, shadowStart) // Border - top
			drawGradientQuad(matrices, provider, layer, x - 3, y + height + 3, x + width + 3, y + height + 4, z, shadowEnd, shadowEnd) // Border - bottom
			drawGradientQuad(matrices, provider, layer, x - 3, y - 3, x + width + 3, y + height + 3, z, backgroundStart, backgroundEnd) // Body
			drawGradientQuad(matrices, provider, layer, x - 4, y - 3, x - 3, y + height + 3, z, shadowStart, shadowEnd) // Border - left
			drawGradientQuad(matrices, provider, layer, x + width + 3, y - 3, x + width + 4, y + height + 3, z, shadowStart, shadowEnd) // Border - right

			drawGradientQuad(matrices, provider, layer, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, z, outlineStart, outlineEnd) // Outline - left
			drawGradientQuad(matrices, provider, layer, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, z, outlineStart, outlineEnd) // Outline - right
			drawGradientQuad(matrices, provider, layer, x - 3, y - 3, x + width + 3, y - 3 + 1, z, outlineStart, outlineStart) // Outline - top
			drawGradientQuad(matrices, provider, layer, x - 3, y + height + 2, x + width + 3, y + height + 3, z, outlineEnd, outlineEnd) // Outline - bottom
		}

		@JvmStatic
		fun getTextureManager(): TextureManager? {
			return Instances.client().getTextureManager()
		}

		@JvmStatic
		fun getItemRenderer(): ItemRenderer? {
			return Instances.client().itemRenderer
		}

		@JvmStatic
		fun getTextRenderer(): FontRenderer? {
			return Instances.client().font
		}
	}
}