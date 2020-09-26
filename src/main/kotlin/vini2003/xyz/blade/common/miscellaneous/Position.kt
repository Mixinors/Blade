package vini2003.xyz.blade.common.miscellaneous

interface Position : PositionHolder {
	fun offset(x: Number, y: Number): Position = of(this, x.toFloat(), y.toFloat())

	companion object {
		@JvmStatic
		fun of(x: Number, y: Number): Position = SimplePosition(x.toFloat(), y.toFloat())

		@JvmStatic
		fun of(anchor: PositionHolder, x: Number, y: Number): Position = of(x.toFloat() + anchor.x, y.toFloat() + anchor.y)

		@JvmStatic
		fun of(anchor: PositionHolder): Position = of(anchor, 0, 0)
	}

	data class SimplePosition(override val x: Float, override val y: Float) : Position
}

interface Positioned : PositionHolder {
	var position: Position

	override var x: Float
		get() = position.x
		set(value) {
			position = Position.of(value, position.y)
		}

	override var y: Float
		get() = position.y
		set(value) {
			position = Position.of(position.x, value)
		}
}

interface PositionHolder {
	val x: Float
	val y: Float
}
