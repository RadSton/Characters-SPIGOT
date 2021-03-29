package radston12.skin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumGamemode;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_16_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import radston12.CharacterWorld;

public class SkinChanger {

	@SuppressWarnings("deprecation")
	public static void change(Player p, Skin sk, Location loc) {
		EntityPlayer ep = ((CraftPlayer)p).getHandle();
		GameProfile prof = ep.getProfile();
		for(Player toSend: Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer)toSend).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep));
		}
		prof.getProperties().removeAll("textures");
		prof.getProperties().put("textures", new Property("textures",sk.getTexture(), sk.getSignature()));
		Bukkit.getScheduler().runTaskLater(CharacterWorld.INSTANCE, new Runnable() {
			
			@Override
			public void run() {
				p.setHealth(0);
				for(Player toSend: Bukkit.getOnlinePlayers()) {
					PlayerConnection connection = ((CraftPlayer)toSend).getHandle().playerConnection;
					connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep));
				}
			}
		}, 2L);
		
		Bukkit.getScheduler().runTaskLater(CharacterWorld.INSTANCE, new Runnable() {
			
			@Override
			public void run() {
				p.spigot().respawn();
			}
		}, 3L);
	}
	
}
