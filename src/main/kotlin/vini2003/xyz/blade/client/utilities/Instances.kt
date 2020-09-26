package vini2003.xyz.blade.client.utilities

import net.minecraft.client.Minecraft

class Instances {
	companion object {
		@JvmStatic
		fun client(): Minecraft {
			return Minecraft.getInstance()
		}
	}
}
