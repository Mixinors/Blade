package com.github.vini2003.blade.client.utilities

import com.github.vini2003.blade.common.widget.base.AbstractWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.IRenderTypeBuffer.Impl
import org.lwjgl.opengl.GL11

class Scissors(provider: IRenderTypeBuffer?, x: Int, y: Int, width: Int, height: Int) {
	private var index = 0
	private var left = 0
	private var right = 0
	private var top = 0
	private var bottom = 0

	constructor(provider: IRenderTypeBuffer?, element: AbstractWidget) : this(provider, (element.position.x * Minecraft.getInstance().window.guiScale).toInt(),
			(Minecraft.getInstance().window.height - (element.position.y + element.size.height) * Minecraft.getInstance().window.guiScale).toInt(),
			(element.size.width * Minecraft.getInstance().window.guiScale).toInt(),
			(element.size.height * Minecraft.getInstance().window.guiScale).toInt())

	private fun resume() {
		GL11.glEnable(GL11.GL_SCISSOR_TEST)
		glScissor(left, top, right - left + 1, bottom - top + 1)
	}

	fun destroy(provider: IRenderTypeBuffer?) {
		if (provider is Impl) {
			provider.endBatch()
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST)
		objects[index] = null
		lastObject--
		if (lastObject > -1) objects[lastObject]!!.resume()
	}

	companion object {
		private const val max = 512
		private val objects = arrayOfNulls<Scissors>(max)
		private var lastObject = -1
		private fun glScissor(x: Int, y: Int, width: Int, height: Int) {
			var width = width
			var height = height
			if (width < 0) width = 0
			if (height < 0) height = 0
			GL11.glScissor(x, y, width, height)
		}
	}

	init {
		if (provider is Impl) {
			provider.endBatch()
		}
		lastObject++
		if (lastObject < max) {
			index = lastObject
			objects[index] = this
			left = x
			right = x + width - 1
			top = y
			bottom = y + height - 1
			if (index > 0) {
				val parent = objects[index - 1]
				if (left < parent!!.left) left = parent.left
				if (right > parent.right) right = parent.right
				if (top < parent.top) top = parent.top
				if (bottom > parent.bottom) bottom = parent.bottom
			}
			resume()
		}
	}
}