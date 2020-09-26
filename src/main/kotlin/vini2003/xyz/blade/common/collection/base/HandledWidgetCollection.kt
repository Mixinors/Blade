package vini2003.xyz.blade.common.collection.base

import vini2003.xyz.blade.common.handler.BaseContainer

interface HandledWidgetCollection : WidgetCollection {
	val handler: BaseContainer

	fun onLayoutChanged()
}
