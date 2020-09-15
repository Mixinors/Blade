package com.github.vini2003.blade.common.collection.base

import com.github.vini2003.blade.common.handler.BaseContainer

interface HandledWidgetCollection : WidgetCollection {
	val handler: BaseContainer

	fun onLayoutChanged()
}