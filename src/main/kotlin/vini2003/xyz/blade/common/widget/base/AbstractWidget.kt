package vini2003.xyz.blade.common.widget.base

import vini2003.xyz.blade.common.handler.BaseContainer
import vini2003.xyz.blade.common.utilities.Networks
import vini2003.xyz.blade.common.utilities.Positions
import vini2003.xyz.blade.common.utilities.Styles
import vini2003.xyz.blade.common.collection.base.HandledWidgetCollection
import vini2003.xyz.blade.common.collection.base.WidgetCollection
import vini2003.xyz.blade.common.miscellaneous.*
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import java.util.*
import kotlin.properties.Delegates

abstract class AbstractWidget : Positioned, Sized {
	override var position: Position by Delegates.observable(Position.of(0, 0)) { _, _, _ ->
		onLayoutChanged()
	}
	override var size: Size by Delegates.observable(Size.of(0, 0)) { _, _, _ ->
		onLayoutChanged()
	}

	var handled: HandledWidgetCollection? = null
	var immediate: WidgetCollection? = null

	var style = "default"

	val hash: Int
		get() = Objects.hash(javaClass.name + "_" + position.x + "_" + position.y + "_" + size.width + "_" + size.height + (if (parent != null) "_" + parent!!.hash else "") + (if (salt != null) "_" + salt!! else ""))

	var salt: String? = null

	var handler: BaseContainer? = null

	val synchronize: MutableSet<ResourceLocation> = mutableSetOf()

	open var hidden: Boolean = false
		get() {
			return (if (parent == null) field else field || parent!!.hidden)
		}

	open var focused: Boolean = false
		get() {
			return (if (parent == null) field && !hidden else field && !parent!!.hidden)
		}

	open var held: Boolean = false
		get() {
			return (if (parent == null) field else field && !parent!!.hidden)
		}

	open var parent: AbstractWidget? = null

	fun style(): Style {
		return Styles.get(style).also {
			if (it == Style.EMPTY) if (parent == null) Style.EMPTY else parent!!.style() else it
		}
	}

	fun color(property: String): Color {
		return style().asColor(property)
	}

	open fun onAdded(handled: HandledWidgetCollection, immediate: WidgetCollection) {
		this.handled = handled
		this.immediate = immediate
		this.handler = handled.handler
	}

	open fun onRemoved(handled: HandledWidgetCollection, immediate: WidgetCollection) {
		this.handled = null
		this.immediate = null
		this.handler = null
	}

	open fun onMouseMoved(x: Float, y: Float) {
		focus(x, y)

		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onMouseMoved(x, y)
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_MOVE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseMove(handler!!.containerId, hash, x, y))
		}
	}

	open fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused) held = true

		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onMouseClicked(x, y, button)
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_CLICK)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseClick(handler!!.containerId, hash, x, y, button))
		}
	}

	open fun onMouseReleased(x: Float, y: Float, button: Int) {
		held = false

		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onMouseReleased(x, y, button)
			}
		}
		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_RELEASE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseRelease(handler!!.containerId, hash, x, y, button))
		}
	}

	open fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onMouseDragged(x, y, button, deltaX, deltaY)
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_DRAG)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseDrag(handler!!.containerId, hash, x, y, button, deltaX, deltaY))
		}
	}

	open fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onMouseScrolled(x, y, deltaY)
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_SCROLL)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseScroll(handler!!.containerId, hash, x, y, deltaY))
		}
	}

	open fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onKeyPressed(keyCode, scanCode, keyModifiers)
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.KEY_PRESS)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofKeyPress(handler!!.containerId, hash, keyCode, scanCode, keyModifiers))
		}
	}

	open fun onKeyReleased(keyCode: Int, character: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onKeyReleased(keyCode, character, keyModifiers)
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.KEY_RELEASE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofKeyRelease(handler!!.containerId, hash, keyCode, character, keyModifiers))
		}
	}

	open fun onCharTyped(character: Char, keyCode: Int) {
		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onCharTyped(character, keyCode)
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.CHAR_TYPE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofCharType(handler!!.containerId, hash, character, keyCode))
		}
	}

	open fun onFocusGained() {
		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onFocusGained()
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.FOCUS_GAIN)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofFocusGain(handler!!.containerId, hash))
		}
	}

	open fun onFocusReleased() {
		if (this is WidgetCollection && !(handler != null && !handler!!.client)) {
			this.widgets.forEach {
				it.onFocusReleased()
			}
		}

		if (focused && handler != null && handler!!.client && synchronize.contains(Networks.FOCUS_RELEASE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofFocusRelease(handler!!.containerId, hash))
		}
	}

	@OnlyIn(Dist.CLIENT)
	open fun getTooltip(): List<ITextComponent> {
		return emptyList()
	}

	@OnlyIn(Dist.CLIENT)
	open fun drawWidget(matrices: MatrixStack, provider: IRenderTypeBuffer) {
		if (hidden) return
	}

	open fun onLayoutChanged() {
		focus(Positions.mouseX, Positions.mouseY)

		parent?.onLayoutChanged()
		handled?.onLayoutChanged()
	}

	fun focus(x: Float, y: Float) {
		val wasFocused = focused
		focused = isWithin(x, y)

		if (wasFocused && !focused) onFocusReleased()
		if (!wasFocused && focused) onFocusGained()
	}

	fun isWithin(x: Float, y: Float): Boolean {
		return x > position.x && x < position.x + size.width && y > position.y && y < position.y + size.height
	}

	fun irritater() {

	}
}
