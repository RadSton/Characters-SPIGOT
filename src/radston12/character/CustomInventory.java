package radston12.character;

import org.bukkit.entity.Player;

import radston12.CharacterWorld;

public class CustomInventory {
	
	//INVUTIL v0.1 WRAPPER
	
	private String name;
	public CustomInventory(String name) {
		this.name = name;
	}
	
	public void create(Player p) {
		CharacterWorld.INVUTIL.saveInventory(p.getInventory(),
						"inventorys." + p.getName() + "." + name);
	}
	
	public void loadToPlayer(Player p) {
		CharacterWorld.INVUTIL.loadInventory(p.getInventory(),
				"inventorys." + p.getName() + "." + name);
	}
	
	
	public String getName() {
		return name;
	}
}
