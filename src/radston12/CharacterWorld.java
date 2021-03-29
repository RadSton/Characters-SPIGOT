	package radston12;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import radston12.character.ChangeCharacter;
import radston12.event.Join;
import radston12.ncp.NPCManager;
import radston12.util.InventoryUtil;

public class CharacterWorld extends JavaPlugin{
	public static CharacterWorld INSTANCE;
	public static InventoryUtil INVUTIL;
	public static NPCManager NPCs;
	@Override
	public void onEnable() {
		// Set instance to JavaPlugin object
		INSTANCE = this;
		
		// Register Events
		Bukkit.getPluginManager().registerEvents(new ChangeCharacter(), this);
		Bukkit.getPluginManager().registerEvents(new Join(), this);
		
		// Register commands
		
		// Init Config
		saveDefaultConfig();
		
		// Init Utils
		NPCs = new NPCManager(INSTANCE);
		INVUTIL = new InventoryUtil(INSTANCE);
		
		// Load up
		loadNPCs();
		for (Player p : Bukkit.getOnlinePlayers()) {
			NPCs.inject(p);
		}
		
		
	}
	@Override
	public void onDisable() {
		
		// Save
		NPCs.saveNPCs();
		
		// Unload due reload
		for (Player p : Bukkit.getOnlinePlayers()) {
			NPCs.uninject(p);
		}
		
		// CleanUP
		NPCs.deleteAll();
	}
	
	public void loadNPCs() {
		FileConfiguration cfg = INSTANCE.getConfig();
		int npcs = cfg.getInt("npcs");
		for (int i = 0; i < npcs; i++) {
			double x = cfg.getDouble("npc." + i + ".x");
			double y = cfg.getDouble("npc." + i + ".y");
			double z = cfg.getDouble("npc." + i + ".z");
			double yaw = cfg.getDouble("npc." + i + ".yaw");
			double pitch = cfg.getDouble("npc." + i + ".pitch");
			String name = cfg.getString("npc." + i + ".name");
			String world = cfg.getString("npc." + i + ".world");
			
			NPCs.create(x, y, z,(float) yaw,(float) pitch, name, world);
		}
		
	}
	
}
