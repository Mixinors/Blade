package vini2003.xyz.blade.common.registry

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import io.netty.buffer.Unpooled
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.IPacket
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.DistExecutor.SafeRunnable
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.fml.network.NetworkEvent.ClientCustomPayloadEvent
import net.minecraftforge.fml.network.NetworkEvent.ServerCustomPayloadEvent
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.PacketDistributor
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget
import org.apache.commons.lang3.tuple.Pair
import vini2003.xyz.blade.Blade
import vini2003.xyz.blade.Thing
import java.util.function.BiConsumer
import java.util.function.Consumer

object NetworkRegistry {
    private val CHANNEL_ID = Blade.resourceLocation("blade")

    private val CHANNEL = NetworkRegistry.newEventChannel(CHANNEL_ID, { "1" }, { true }, { true })

    private val serverToClient: MutableMap<ResourceLocation, BiConsumer<NetworkEvent.Context, PacketBuffer>> = Maps.newHashMap()

    private val clientToServer: MutableMap<ResourceLocation, BiConsumer<NetworkEvent.Context, PacketBuffer>> = Maps.newHashMap()


    fun initialize() {
        CHANNEL.addListener(
            createPacketHandler(
                ClientCustomPayloadEvent::class.java, clientToServer
            )
        )

        DistExecutor.safeRunWhenOn(Dist.CLIENT) {
            SafeRunnable {
                Client.initClient()
            }
        }
    }

    private fun <T : NetworkEvent> createPacketHandler(clazz: Class<T>, map: Map<ResourceLocation, BiConsumer<NetworkEvent.Context, PacketBuffer>>): Consumer<T> {
        return Thing(clazz, map)
    }

    @OnlyIn(Dist.CLIENT)
    fun registerS2CHandler(id: ResourceLocation, consumer: BiConsumer<NetworkEvent.Context, PacketBuffer>) {
        serverToClient[id] = consumer
    }

    fun registerC2SHandler(id: ResourceLocation, consumer: BiConsumer<NetworkEvent.Context, PacketBuffer>) {
        clientToServer[id] = consumer
    }

    @OnlyIn(Dist.CLIENT)
    fun sendToServer(id: ResourceLocation?, buffer: PacketBuffer?) {
        val connection = Minecraft.getInstance().connection

        if (connection != null) {
            val packetBuffer = PacketBuffer(Unpooled.buffer())

            packetBuffer.writeResourceLocation(id)
            packetBuffer.writeBytes(buffer)

            connection.send(
                NetworkDirection.PLAY_TO_SERVER.buildPacket<IPacket<*>>(
                    Pair.of(packetBuffer, 0),
                    CHANNEL_ID
                ).getThis()
            )
        }
    }

    fun sendToClient(id: ResourceLocation?, target: PacketTarget, buffer: PacketBuffer?) {
        val packetBuffer = PacketBuffer(Unpooled.buffer())

        packetBuffer.writeResourceLocation(id)
        packetBuffer.writeBytes(buffer)

        target.send(
            NetworkDirection.PLAY_TO_CLIENT.buildPacket<IPacket<*>>(Pair.of(packetBuffer, 0), CHANNEL_ID).getThis()
        )
    }

    fun sendToPlayer(player: ServerPlayerEntity?, id: ResourceLocation?, buffer: PacketBuffer?) {
        sendToClient(id, PacketDistributor.PLAYER.with { player }, buffer)
    }

    private fun sendSyncPacket(map: Map<ResourceLocation, BiConsumer<NetworkEvent.Context, PacketBuffer>>): PacketBuffer {
        val availableIds: List<ResourceLocation> = Lists.newArrayList(map.keys)
        val packetBuffer = PacketBuffer(Unpooled.buffer())

        packetBuffer.writeInt(availableIds.size)

        for (availableId in availableIds) {
            packetBuffer.writeResourceLocation(availableId)
        }

        return packetBuffer
    }

    private class Client {
        companion object {
            @OnlyIn(Dist.CLIENT)
            fun initClient() {
                CHANNEL.addListener(
                    createPacketHandler(
                        ServerCustomPayloadEvent::class.java, serverToClient
                    )
                )
            }
        }
    }
}
