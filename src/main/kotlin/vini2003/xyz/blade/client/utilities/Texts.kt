package com.github.vini2003.blade.client.utilities

import net.minecraft.util.text.ITextComponent

class Texts {
	companion object {
		@JvmStatic
		fun width(text: ITextComponent): Int {
			return Instances.client().font.width(text)
		}

		@JvmStatic
		fun height(): Int {
			return Instances.client().font.lineHeight
		}
	}
}