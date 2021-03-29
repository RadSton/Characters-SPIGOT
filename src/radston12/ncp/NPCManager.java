package radston12.ncp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import radston12.CharacterWorld;

public class NPCManager {

	private CharacterWorld main;
	private List<Npc> npcs = new ArrayList<>();
	private HashMap<UUID, PacketReader> readers = new HashMap<>();
	
	public NPCManager(CharacterWorld main) {
		this.main = main;
	}
	
	public void create(double x,double y,double z,float yaw,float pitch,String name,String worldname) {
		Npc npc = new Npc();
		npc.createNPC(new Location(Bukkit.getWorld(worldname),x,y,z),name, yaw, pitch);
		npc.showForAll();
		npcs.add(npc);
		hideInTab();
	}
	
	public void create(Player p,String name) {
		this.create(p.getLocation().getX(),
					p.getLocation().getY(),
					p.getLocation().getZ(),
					p.getLocation().getYaw(),
					p.getLocation().getPitch(),
					name.toString(),
					p.getWorld().getName());
	}
	
	public boolean delete(Npc n) {
		for(Player p: Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutEntityDestroy(n.getNpc().getId()));
		}
		return npcs.remove(n);
	}
	public void deleteAll() {
		if(npcs == null) {
			return;
		}
		if(npcs.isEmpty()) 
			return;
		try {
			for (Npc npc : npcs) {
				if(npc == null)
					return;
				delete(npc);
			}
		} catch(Exception e) {
			// Added because the HashMap.next() function is a little glitchey 
			System.out.println("[radston12/npc/NPCManager.java] Error at saving IF you see this radston can not code propally and also not write!");
		}
	}
	
	public List<Npc> getNpcs() {
		return npcs;
	}
	
	public CharacterWorld getMain() {
		return main;
	}
	
	public void inject(Player p) {
		PacketReader pr = new PacketReader(this);
		pr.inject(p);
		readers.put(p.getUniqueId(), pr);
	}
	
	public void uninject(Player p) {
		if(readers.containsKey(p.getUniqueId())) {
			readers.get(p.getUniqueId()).uninject(p);
			readers.remove(p.getUniqueId());
		}
	}
	
	public void injectall() {
		for(Player p: Bukkit.getOnlinePlayers()) {
			if(!readers.containsKey(p.getUniqueId()))
				this.inject(p);
		}
	}
	
	public void uninjectall() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(readers.containsKey(p.getUniqueId()))
				this.uninject(p);
		}
	}
	
	public void event(int id,Player p) {
		for (Npc entity : this.npcs) {
			EntityPlayer npc = entity.getNpc();
			if(npc.getId() == id) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					
					@Override
					public void run() {
						Bukkit.getPluginManager().callEvent(new RightClickNPC(p, npc));
					}
				});
			}
		}
	}
	
	public void hideInTab() {
		for (Npc npc : npcs) {
			if(npc.isHidden()) continue;
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
	         
				@Override
				public void run() {
                 	PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc.getNpcPlayer().getHandle());
                 	for(Player p : Bukkit.getOnlinePlayers())
                 		((CraftPlayer) p).getHandle().playerConnection.sendPacket(info);
            	}
         
        	}, 2);
		}
	}
	
	public Npc getNpcById(int id) {
		if(npcs == null)
			return null;
		Npc n = null;
		for(Npc np : npcs) {
			if(np.getNpc().getId() == id) {
				n = np;
				break;
			}
		}
		return n;
	}
	
	public void saveNPCs() {
		FileConfiguration cfg = main.getConfig();
		cfg.set("npcs", npcs.size());
		int iterator = 0;
		for(Npc npc: npcs) {
			cfg.set("npc." + iterator + ".x", npc.getNpc().locX());
			cfg.set("npc." + iterator + ".y", npc.getNpc().locY());
			cfg.set("npc." + iterator + ".z", npc.getNpc().locZ());
			cfg.set("npc." + iterator + ".pitch", npc.getNpc().pitch);
			cfg.set("npc." + iterator + ".yaw", npc.getNpc().yaw);
			cfg.set("npc." + iterator + ".name", npc.name);
			cfg.set("npc." + iterator + ".world", npc.getNpc().world.getWorld().getName());
			iterator++;
		}
		main.saveConfig();
	}

	
	public void cleanUp() {
		npcs.clear();
		if(readers.isEmpty()) {
			readers.clear();
		} else {
			
		}
	}

}
