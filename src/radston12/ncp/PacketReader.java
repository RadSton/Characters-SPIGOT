package radston12.ncp;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity;


public class PacketReader {

	Channel channel;
	public static Map<UUID, Channel> channels = new HashMap<>();
	public NPCManager mgr;
	
	public PacketReader(NPCManager mgr) {
		this.mgr = mgr;
	}
	
	public void inject(Player p) {
		CraftPlayer player = (CraftPlayer) p;
		channel = player.getHandle().playerConnection.networkManager.channel;
		channels.put(p.getUniqueId(), channel);
		
		if(channel.pipeline().get("PacketInjector") != null) return;
		
		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {

			@Override
			protected void decode(ChannelHandlerContext channel, PacketPlayInUseEntity packet, List<Object> arg) throws Exception {
				arg.add(packet);
				readPacket(player, packet);
			}
			
		});
	}
	
	public void uninject(Player p) {
		channel = channels.get(p.getUniqueId());
		if(channel.pipeline().get("PacketInjector") != null) {
			channel.pipeline().remove("PacketInjector");
		}
	}
	
	public void readPacket(Player p, Packet<?> packet) {
		if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
			
			if(getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")) 
				return;
			
			if(getValue(packet, "d").toString().equalsIgnoreCase("OFF_HAND")) 
				return;
			
			if(getValue(packet, "action").toString().equalsIgnoreCase("INTERACT_AT")) 
				return;
			
			int id = (int) getValue(packet, "a");
			
			if(getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {
				mgr.event(id, p);
			}
		}
	}
	
	private Object getValue(Object instance,String name) {
		Object result = null;
		
		try {
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			result = field.get(instance);
			field.setAccessible(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
