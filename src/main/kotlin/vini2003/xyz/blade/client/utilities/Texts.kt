package com.github.vini2003.blade.client.utilities

import net.minecraft.text.Text

class Texts {
	companion object {
		@JvmStatic
		fun width(text: Text): Int {
			return Instances.client().textRenderer.getWidth(text)
		}

		@JvmStatic
		fun height(): Int {
			return Instances.client().textRenderer.fontHeight
		}
	}
}