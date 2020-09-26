package vini2003.xyz.blade.common.collection

import vini2003.xyz.blade.common.collection.base.WidgetCollection
import vini2003.xyz.blade.common.widget.base.AbstractWidget
import vini2003.xyz.blade.common.widget.base.TabWidget

class TabWidgetCollection(private val number: Int) : AbstractWidget(), WidgetCollection {
	override val widgets: MutableList<AbstractWidget> = mutableListOf()

	override fun addWidget(widget: AbstractWidget) {
		super.addWidget(widget)
		widget.hidden = widget.hidden || parent != null && (parent as TabWidget).selected != number
	}
}
