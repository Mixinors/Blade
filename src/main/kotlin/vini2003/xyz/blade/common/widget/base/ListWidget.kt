package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.client.utilities.Scissors
import com.github.vini2003.blade.common.miscellaneous.Position
import com.github.vini2003.blade.common.miscellaneous.Size
import com.github.vini2003.blade.common.miscellaneous.Rectangle
import com.github.vini2003.blade.common.utilities.Positions
import com.github.vini2003.blade.common.collection.base.WidgetCollection
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import vini2003.xyz.blade.Blade
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

open class ListWidget() : AbstractWidget(), WidgetCollection {
    override val widgets: MutableList<AbstractWidget> = mutableListOf()

    var scrollbarTexture = PartitionedTexture(Blade.resourceLocation("textures/widget/scrollbar.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.16666666666666666667F)
    var scrollerTexture = PartitionedTexture(Blade.resourceLocation("textures/widget/scroller.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F)
    var scrollerFocusTexture = PartitionedTexture(Blade.resourceLocation("textures/widget/scroller_focus.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F)

    private var scrollerHeld = false

    private var updateScrollerRectangle: Boolean = false
    private var updateScrollbarRectangle: Boolean = false

    private var scrollerRectangleCached: Rectangle = Rectangle.empty()
    private var scrollbarRectangleCached: Rectangle = Rectangle.empty()

    private val scrollerHeight: Float
        get() = min(size.height - 2, height / widgets.sumBy { it.height.toInt() } * height)

    private val scrollerY: Float
        get() {
            val maxY = widgets.asSequence().map { it.y + it.height }.maxOrNull() ?: 0F
            val minY = widgets.asSequence().map { it.y }.minOrNull() ?: 0F

            return max(y + 2, min(y + size.height - scrollerHeight, (abs(y - minY) / (maxY - minY) * (size.height + scrollerHeight) + y + 1)))
        }

    private val scrollerRectangle: Rectangle
        get() {
            return if (updateScrollerRectangle) {
                scrollerRectangleCached = Rectangle(Position.of(position.x + size.width - 1 - 16F, scrollerY - 1), Size.of(16F, scrollerHeight))
                updateScrollerRectangle = false
                return scrollerRectangleCached
            } else {
                scrollerRectangleCached
            }
        }

    private val scrollbarRectangle: Rectangle
        get() {
            return if (updateScrollbarRectangle) {
                scrollbarRectangleCached = Rectangle(Position.of(position.x + size.width - 1 - 16F, position.y + 1), Size.of(16F, size.height - 2))
                updateScrollbarRectangle = false
                return scrollbarRectangleCached
            } else {
                scrollbarRectangleCached
            }
        }

    override fun addWidget(widget: AbstractWidget) {
        widgets.add(widget)
        handled?.also { widget.onAdded(it, this) }

        widgets.forEach { _ ->
            widget.onLayoutChanged()
        }

        super.addWidget(widget)
    }

    override fun removeWidget(widget: AbstractWidget) {
        widgets.remove(widget)
        handled?.also { widget.onRemoved(it, this) }

        widgets.forEach { _ ->
            widget.onLayoutChanged()
        }

        super.removeWidget(widget)
    }

    override fun onLayoutChanged() {
        super.onLayoutChanged()

        updateScrollerRectangle = true
        updateScrollbarRectangle = true
    }

    override fun onMouseClicked(x: Float, y: Float, button: Int) {
        if (handler!!.client) {
            super.onMouseClicked(x, y, button)
        }

        if (scrollerRectangle.isWithin(x, y)) {
            scrollerHeld = true
        } else if (scrollbarRectangle.isWithin(x, y)) {
            if (y > scrollerY) {
                onMouseScrolled(x, y, -1.0)
            } else if (y < scrollerY) {
                onMouseScrolled(x, y, 1.0)
            }
        }
    }

    override fun onMouseReleased(x: Float, y: Float, button: Int) {
        if (handler!!.client) {
            super.onMouseReleased(x, y, button)
        }

        scrollerHeld = false
    }

	override fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (handler!!.client) {
			super.onMouseDragged(x, y, button, deltaX, deltaY)
		}

		if (scrollerHeld) {
			if (deltaY > 0) {
				onMouseScrolled(x, y, -deltaY)
			} else if (deltaY < 0) {
				onMouseScrolled(x, y, -deltaY)
			}
		}
	}

    override fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
        if (handler!!.client) {
            if (focused || scrollerHeld) {
                super.onMouseScrolled(x, y, deltaY)
            } else {
                return
            }
        }

		if (widgets.isNotEmpty()) {
			if (deltaY > 0 && widgets.minByOrNull { it.y }!!.y < this.y + 2) {
				allWidgets.forEach {
					it.y += deltaY.toFloat() * 2.5F

					it.onLayoutChanged()
					this.onLayoutChanged()

					if (it.y >= this.y + height) it.hidden = true
					else if (it.y >= this.y) it.hidden = false
				}
			} else if (deltaY <= 0 && widgets.asSequence().map { it.y + it.height }.maxOrNull()!! >= this.y + height - 2) {
				allWidgets.forEach {
					it.y += deltaY.toFloat() * 2.5F

					it.onLayoutChanged()
					this.onLayoutChanged()

					if (it.y >= this.y + height) it.hidden = true
					else if (it.y >= this.y) it.hidden = false
				}
			}
		}
    }

    override fun drawWidget(matrices: MatrixStack, provider: IRenderTypeBuffer) {
        if (hidden) return

        scrollbarTexture.draw(matrices, provider, position.x + size.width - 18F, position.y, 18F, size.height)

        val scrollerFocus = scrollerRectangle.isWithin(Positions.mouseX, Positions.mouseY)

        if (scrollerFocus || scrollerHeld) {
            scrollerFocusTexture.draw(matrices, provider, position.x + size.width - 18F + 1F, scrollerY - 1, 16F, scrollerHeight)
        } else {
            scrollerTexture.draw(matrices, provider, position.x + size.width - 18F + 1F, scrollerY - 1, 16F, scrollerHeight)
        }

        val rawHeight = Instances.client().window.height.toFloat()
        val scale = Instances.client().window.guiScale.toFloat()

        val area = Scissors(provider, (x * scale).toInt(), (rawHeight - (y + height) * scale).toInt(), (width * scale).toInt(), (height * scale).toInt())

        widgets.forEach {
            it.drawWidget(matrices, provider)
        }

        area.destroy(provider)
    }
}
