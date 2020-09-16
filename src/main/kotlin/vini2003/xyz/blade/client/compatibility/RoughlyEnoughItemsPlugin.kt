package com.github.vini2003.blade.client.compatibility
//
//import com.github.vini2003.blade.Blade
//import com.github.vini2003.blade.client.handler.BaseContainerScreen
//import com.github.vini2003.blade.common.handler.BaseContainer
//import me.shedaniel.math.Rectangle
//import me.shedaniel.rei.api.DisplayHelper
//import me.shedaniel.rei.api.DisplayHelper.DisplayBoundsProvider
//import me.shedaniel.rei.api.plugins.REIPluginV0
//import net.minecraft.util.ResourceLocation
//import vini2003.xyz.blade.Blade
//
//class RoughlyEnoughItemsPlugin : REIPluginV0 {
//	companion object {
//		val IDENTIFIER = Blade.resourceLocation("roughly_enough_items_plugin")
//	}
//
//	override fun getPluginResourceLocation(): ResourceLocation {
//		return IDENTIFIER
//	}
//
//	override fun registerBounds(displayHelper: DisplayHelper?) {
//		displayHelper!!.registerHandler(object : DisplayBoundsProvider<BaseContainerScreen<BaseContainer>> {
//			override fun getBaseSupportedClass(): Class<*>? {
//				return BaseContainerScreen::class.java
//			}
//
//			override fun getScreenBounds(screen: BaseContainerScreen<BaseContainer>): Rectangle {
//				return screen.screenHandler.rectangle
//			}
//
//			override fun getPriority(): Float {
//				return 16F
//			}
//		})
//	}
//}