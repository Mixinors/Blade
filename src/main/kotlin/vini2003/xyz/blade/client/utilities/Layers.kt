package com.github.vini2003.blade.client.utilities

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.ResourceLocation

class Layers : RenderLayer {
	constructor(
			name: String?,
			vertexFormat: VertexFormat?,
			drawMode: Int,
			expectedBufferSize: Int,
			hasCrumbling: Boolean,
			translucent: Boolean,
			startAction: Runnable?,
			endAction: Runnable?
	) : super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction)

	companion object {
		@JvmStatic
		fun get(texture: ResourceLocation?): RenderLayer {
			return of(
					"entity_cutout",
					VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, 7, 256, true, true,
					MultiPhaseParameters.builder()
							.texture(Texture(texture, false, false))
							.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
							.diffuseLighting(RenderPhase.DISABLE_DIFFUSE_LIGHTING)
							.alpha(RenderPhase.ONE_TENTH_ALPHA)
							.lightmap(RenderPhase.DISABLE_LIGHTMAP)
							.overlay(RenderPhase.DISABLE_OVERLAY_COLOR).build(true)
			)
		}

		@JvmStatic
		private val INTERRFACE: RenderLayer = of(
				"blade_flat",
				VertexFormats.POSITION_COLOR_LIGHT, 7, 256,
				MultiPhaseParameters.builder()
						.texture(NO_TEXTURE)
						.cull(ENABLE_CULLING)
						.lightmap(ENABLE_LIGHTMAP)
						.shadeModel(SHADE_MODEL)
						.depthTest(ALWAYS_DEPTH_TEST)
						.transparency(TRANSLUCENT_TRANSPARENCY)
						.alpha(ONE_TENTH_ALPHA)
						.layering(VIEW_OFFSET_Z_LAYERING)
						.build(false)
		)

		@JvmStatic
		private val TOOLTIP: RenderLayer = of(
				"blade_tooltip",
				VertexFormats.POSITION_COLOR_LIGHT, 7, 256,
				MultiPhaseParameters.builder()
						.texture(NO_TEXTURE)
						.cull(ENABLE_CULLING)
						.lightmap(ENABLE_LIGHTMAP)
						.shadeModel(SHADE_MODEL)
						.depthTest(ALWAYS_DEPTH_TEST)
						.transparency(NO_TRANSPARENCY)
						.alpha(ONE_TENTH_ALPHA)
						.layering(VIEW_OFFSET_Z_LAYERING)
						.build(false)
		)

		@JvmStatic
		fun flat(): RenderLayer {
			return INTERRFACE
		}

		@JvmStatic
		fun tooltip(): RenderLayer {
			return TOOLTIP
		}
	}
}