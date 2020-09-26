package vini2003.xyz.blade.common.miscellaneous

data class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
	companion object {
		@JvmStatic
		fun of(string: String): Color {
			val a = Integer.decode("0x" + string.substring(2, 4)) / 255F
			val r = Integer.decode("0x" + string.substring(4, 6)) / 255F
			val g = Integer.decode("0x" + string.substring(6, 8)) / 255F
			val b = Integer.decode("0x" + string.substring(8, 10)) / 255F

			return Color(r, g, b, a)
		}

		@JvmStatic
		fun of(color: Long): Color {
			val r = (color and -0x1000000 shr 24) / 255F
			val g = (color and 0x00FF0000 shr 16) / 255F
			val b = (color and 0x0000FF00 shr 8) / 255F
			val a = (color and 0x000000FF) / 255F

			return Color(r, g, b, a)
		}

		@JvmStatic
		fun standard(): Color {
			return Color(1F, 1F, 1F, 1F)
		}
	}

	fun toInt(): Int {
		val r: Int = (this.r * 255).toInt() and 0xFF
		val g: Int = (this.g * 255).toInt() and 0xFF
		val b: Int = (this.b * 255).toInt() and 0xFF
		val a: Int = (this.a * 255).toInt() and 0xFF

		return (r shl 24) + (g shl 16) + (b shl 8) + a
	}
}
