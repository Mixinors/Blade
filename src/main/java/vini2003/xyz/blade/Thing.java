package vini2003.xyz.blade;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Thing<T extends NetworkEvent> implements Consumer<T> {
    private final Class<T> clazz;
    private final Map<ResourceLocation, BiConsumer<NetworkEvent.Context, PacketBuffer>> map;
    
    public Thing(Class<T> clazz, Map<ResourceLocation, BiConsumer<NetworkEvent.Context, PacketBuffer>> map) {
        this.clazz = clazz;
        this.map = map;
    }
    
    @Override
    public void accept(T event) {
        if (event.getClass() == clazz) {
            NetworkEvent.Context context = event.getSource().get();
            
            if (!context.getPacketHandled()) {
                PacketBuffer buffer = new PacketBuffer(event.getPayload().copy());
                ResourceLocation type = buffer.readResourceLocation();
                BiConsumer<NetworkEvent.Context, PacketBuffer> consumer = map.get(type);
                
                if (consumer != null)
                    consumer.accept(context, buffer);
                
                context.setPacketHandled(true);
            }
        }
    }
}
