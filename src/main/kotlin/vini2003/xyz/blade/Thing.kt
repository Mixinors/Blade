package vini2003.xyz.blade

import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.BiConsumer
import java.util.function.Consumer
import kotlin.reflect.KClass

class Thing<T : NetworkEvent>(private val clazz: KClass<T>, private val map: Map<ResourceLocation, (NetworkEvent.Context, PacketBuffer) -> Unit>) : Consumer<T> {
    constructor(clazz: Class<T>, map: Map<ResourceLocation, BiConsumer<NetworkEvent.Context, PacketBuffer>>) : this(clazz.kotlin, map.convert())

    companion object {
        private fun Map<ResourceLocation, BiConsumer<NetworkEvent.Context, PacketBuffer>>.convert() =
            this.entries.map { it.key to { ctx: NetworkEvent.Context, buf: PacketBuffer -> it.value.accept(ctx, buf) } }.toMap()
    }

    override fun accept(event: T) {
        if (event::class != this.clazz) return

        val context = event.source.get()
        if (context.packetHandled) return

        val buffer = PacketBuffer(event.payload.copy())
        val type = buffer.readResourceLocation()

        this.map[type]?.let { it(context, buffer) }

        context.packetHandled = true
    }
}
