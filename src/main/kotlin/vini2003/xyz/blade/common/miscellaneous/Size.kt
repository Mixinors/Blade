package vini2003.xyz.blade.common.miscellaneous

interface Size : SizeHolder {
	companion object {
		@JvmStatic
		fun of(width: Number, height: Number): Size = SimpleSize(width.toFloat(), height.toFloat())

		@JvmStatic
		fun of(anchor: SizeHolder, width: Number, height: Number): Size = of(width.toFloat() + anchor.width, height.toFloat() + anchor.height)

		@JvmStatic
		fun of(anchor: SizeHolder): Size = of(anchor, 0, 0)

		@JvmStatic
		fun absolute(anchor: SizeHolder): Size = of(anchor.width, anchor.height)
	}

	data class SimpleSize(override val width: Float = 0F, override val height: Float = 0F) : Size
}

interface Sized : SizeHolder {
	var size: Size
	override var width: Float
		get() = size.width
		set(value) {
			size = Size.of(value, size.height)
		}
	override var height: Float
		get() = size.height
		set(value) {
			size = Size.of(size.width, value)
		}
}

interface SizeHolder {
	val width: Float
	val height: Float
}
