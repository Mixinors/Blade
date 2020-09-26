package vini2003.xyz.blade.common.utilities

import vini2003.xyz.blade.common.handler.BaseContainer
import vini2003.xyz.blade.Blade
import vini2003.xyz.blade.common.registry.NetworkRegistry
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation

class Networks {
	companion object {
		@JvmStatic
		val INITIALIZE = Blade.resourceLocation("initialize")

		@JvmStatic
		val WIDGET_UPDATE = Blade.resourceLocation("update")

		@JvmStatic
		val MOUSE_MOVE = Blade.resourceLocation("mouse_move")

		@JvmStatic
		val MOUSE_CLICK = Blade.resourceLocation("mouse_click")

		@JvmStatic
		val MOUSE_RELEASE = Blade.resourceLocation("mouse_release")

		@JvmStatic
		val MOUSE_DRAG = Blade.resourceLocation("mouse_drag")

		@JvmStatic
		val MOUSE_SCROLL = Blade.resourceLocation("mouse_scroll")

		@JvmStatic
		val KEY_PRESS = Blade.resourceLocation("key_press")

		@JvmStatic
		val KEY_RELEASE = Blade.resourceLocation("key_release")

		@JvmStatic
		val CHAR_TYPE = Blade.resourceLocation("char_type")

		@JvmStatic
		val FOCUS_GAIN = Blade.resourceLocation("focus_gain")

		@JvmStatic
		val FOCUS_RELEASE = Blade.resourceLocation("focus_release")

		init {
			NetworkRegistry.registerC2SHandler(WIDGET_UPDATE) { context, buf ->
				val syncId = buf.readInt()
				val id = buf.readResourceLocation()

				buf.retain()

				context.enqueueWork {
					context.sender!!.server!!.playerList.players.forEach {
						if (it.containerMenu.containerId == syncId && it.containerMenu is BaseContainer) {
							(it.containerMenu as BaseContainer).handlePacket(id, PacketBuffer(buf.copy()))
						}
					}
				}
			}

			NetworkRegistry.registerC2SHandler(INITIALIZE) { context, buf ->
				val syncId = buf.readInt()
				val width = buf.readInt()
				val height = buf.readInt()

				buf.retain()

				context.enqueueWork {
					context.sender!!.server!!.playerList.players.forEach { it ->
						if (it.containerMenu.containerId == syncId && it.containerMenu is BaseContainer) {
							(it.containerMenu as BaseContainer).also {
								it.slots.clear()
								it.widgets.clear()
								it.initialize(width, height)
							}
						}
					}
				}
			}
		}

		@JvmStatic
		fun initialize() {
		}

		@JvmStatic
		fun toServer(id: ResourceLocation, buf: PacketBuffer) {
			NetworkRegistry.sendToServer(id, buf)
		}

		@JvmStatic
		fun ofInitialize(syncId: Int, width: Int, height: Int): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeInt(width)
			buf.writeInt(height)
			return buf
		}

		@JvmStatic
		fun ofMouseMove(syncId: Int, hash: Int, x: Float, y: Float): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(MOUSE_MOVE)
			buf.writeInt(hash)
			buf.writeFloat(x)
			buf.writeFloat(y)
			return buf
		}

		@JvmStatic
		fun ofMouseClick(syncId: Int, hash: Int, x: Float, y: Float, button: Int): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(MOUSE_CLICK)
			buf.writeInt(hash)
			buf.writeFloat(x)
			buf.writeFloat(y)
			buf.writeInt(button)
			return buf
		}

		@JvmStatic
		fun ofMouseRelease(syncId: Int, hash: Int, x: Float, y: Float, button: Int): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(MOUSE_RELEASE)
			buf.writeInt(hash)
			buf.writeFloat(x)
			buf.writeFloat(y)
			buf.writeInt(button)
			return buf
		}

		@JvmStatic
		fun ofMouseDrag(syncId: Int, hash: Int, x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(MOUSE_DRAG)
			buf.writeInt(hash)
			buf.writeFloat(x)
			buf.writeFloat(y)
			buf.writeInt(button)
			buf.writeDouble(deltaX)
			buf.writeDouble(deltaY)
			return buf
		}

		@JvmStatic
		fun ofMouseScroll(syncId: Int, hash: Int, x: Float, y: Float, deltaY: Double): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(MOUSE_SCROLL)
			buf.writeInt(hash)
			buf.writeFloat(x)
			buf.writeFloat(y)
			buf.writeDouble(deltaY)
			return buf
		}

		@JvmStatic
		fun ofKeyPress(syncId: Int, hash: Int, keyCode: Int, scanCode: Int, keyModifiers: Int): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(KEY_PRESS)
			buf.writeInt(hash)
			buf.writeInt(keyCode)
			buf.writeInt(scanCode)
			buf.writeInt(keyModifiers)
			return buf
		}

		@JvmStatic
		fun ofKeyRelease(syncId: Int, hash: Int, keyCode: Int, scanCode: Int, keyModifiers: Int): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(KEY_RELEASE)
			buf.writeInt(hash)
			buf.writeInt(keyCode)
			buf.writeInt(scanCode)
			buf.writeInt(keyModifiers)
			return buf
		}

		@JvmStatic
		fun ofCharType(syncId: Int, hash: Int, character: Char, keyCode: Int): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(CHAR_TYPE)
			buf.writeInt(hash)
			buf.writeChar(character.toInt())
			buf.writeInt(keyCode)
			return buf
		}

		@JvmStatic
		fun ofFocusGain(syncId: Int, hash: Int): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(FOCUS_GAIN)
			buf.writeInt(hash)
			return buf
		}

		@JvmStatic
		fun ofFocusRelease(syncId: Int, hash: Int): PacketBuffer {
			val buf = PacketBuffer(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeResourceLocation(FOCUS_RELEASE)
			buf.writeInt(hash)
			return buf
		}
	}
}
