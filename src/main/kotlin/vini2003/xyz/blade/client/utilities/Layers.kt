package com.github.vini2003.blade.client.utilities

import net.minecraft.client.renderer.RenderState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.Texture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.ResourceLocation

class Layers : RenderType {
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
		fun get(texture: ResourceLocation?): RenderType {
			return create(
					"entity_cutout",
					DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, true, true,
					State.builder()
							.setTextureState(TextureState(texture, false, false))
							.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
							.setDiffuseLightingState(NO_DIFFUSE_LIGHTING)
							.setAlphaState(DEFAULT_ALPHA)
							.setLightmapState(NO_LIGHTMAP)
							.setOverlayState(NO_OVERLAY)
							.createCompositeState(true)
			)
		}

		@JvmStatic
		private val INTERRFACE: RenderType = create(
				"blade_flat",
				DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, 7, 256,
				State.builder()
						.setTextureState(NO_TEXTURE)
						.setCullState(CULL)
						.setLightmapState(LIGHTMAP)
						.setShadeModelState(FLAT_SHADE)
						.setDepthTestState(NO_DEPTH_TEST)
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.setAlphaState(DEFAULT_ALPHA)
						.setLayeringState(VIEW_OFFSET_Z_LAYERING)
						.createCompositeState(false)
		)

		@JvmStatic
		fun flat(): RenderType {
			return INTERRFACE
		}
	}
}