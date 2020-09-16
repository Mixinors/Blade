package com.github.vini2003.blade.common.utilities

import com.github.vini2003.blade.common.miscellaneous.Style
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.resources.IResourceManager
import java.nio.charset.Charset

class Styles {
	companion object {
		private val entries: MutableMap<String, Style> = mutableMapOf()

		private val jankson: Gson = Gson()

		fun get(name: String): Style {
			return entries.getOrDefault(name, Style.EMPTY)
		}

		fun load(manager: IResourceManager) {
			manager.listResources("style") { string -> string.endsWith(".style.json") }.forEach {
				try {
					val stream = manager.getResource(it).inputStream

					val reader = stream.bufferedReader(Charset.defaultCharset())

					val jsonObject = JsonObject()

					load(it.path.replaceFirst("style/", "").replaceFirst(".style.json", ""), jankson.fromJson(reader, jsonObject.javaClass))

					stream.close()
				} catch (exception: Exception) {
					System.err.println(exception.message)
				}
			}
		}

		fun load(name: String, data: JsonObject) {
			val templates: JsonObject? = data.getAsJsonObject("templates")
			val entries: JsonObject? = data.getAsJsonObject("entries")

			if (templates == null || entries == null) {
				throw RuntimeException("Invalid JSON parsed for Blade theme!")
			}

			val mapped: MutableMap<String, JsonElement> = mutableMapOf()

			entries.entrySet().forEach {
				mapped[it.key] = if (it.key.startsWith("$")) (templates[it.key.replace("$", "")]).asJsonPrimitive as JsonElement else it.value
			}

			Styles.entries[name] = Style(mapped)
		}

		fun clear() {
			entries.clear()
		}
	}
}