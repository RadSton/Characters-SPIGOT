package radston12.ncp;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;
import radston12.CharacterWorld;
import radston12.skin.Skin;
import radston12.skin.SkinGrabber;

public class Npc {

	private EntityPlayer npc;
	private GameProfile gameProfile;
	private boolean isHidden;
	public String name;
	public Skin s;
	public void createNPC(Location location, String npcName,float yaw,float pitch) {
        name = npcName;
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "" + npcName);
        Skin s = SkinGrabber.get(npcName);
        gameProfile.getProperties().put("textures", new Property("textures",s.getTexture(),s.getSignature()));
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
        npc.setLocation(location.getX(), location.getY(), location.getZ(), yaw, pitch);
        this.npc = npc;
        this.gameProfile = gameProfile;
    }

    public void sendPacket(Player p) {
    	if(npc == null) return;
    	PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
    }
    
    public void sendSecondPacket(Player p) {
    	if(npc == null) return;
    	PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
    	connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
    }
    
    public void showForAll() {
    	for(Player p: Bukkit.getOnlinePlayers()) {
    		sendPacket(p);
    	}
    	
    	Bukkit.getScheduler().runTaskLater(CharacterWorld.INSTANCE, new Runnable() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					sendSecondPacket(p);
				}
			}
		}, 20L);
    }
    
    public EntityPlayer getNpc() {
		return npc;
	}
    
    public CraftPlayer getNpcPlayer() {
		return npc.getBukkitEntity();
	}
    
    public GameProfile getGameProfile() {
		return gameProfile;
	}
    
    public void setIsHidden(boolean b) {
    	this.isHidden = b;
    }
    
    public boolean isHidden() {
    	return this.isHidden;
    }
}
