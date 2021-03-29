package radston12.character;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import radston12.ncp.Npc;

public class Character {
	
	private Location loc;
	private CustomInventory inventory;
	private int playerlevel;
	private Player p;
	private Npc n;
	
	public Character(Location loc, Player p, String name) {
		this.loc = loc;
		this.p = p;
		update();
	}
	
	public void update() {
		
	}
	
	public void create() {
		
	}
	
	public boolean isCharacter(int i) {
		return (n.getNpc().getId() == i);
	}
	
	public Npc getCharacterIfCharacter(int i) {
		if(isCharacter(i)) {
			return n;
		} else {
			return null;
		}
	}
	
	public Npc getNpc() {
		return n;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	public void setInventory(CustomInventory inventory) {
		this.inventory = inventory;
	}
	public void setPlayerlevel(int playerlevel) {
		this.playerlevel = playerlevel;
	}
	public int getPlayerlevel() {
		return playerlevel;
	}
	public CustomInventory getInventory() {
		return inventory;
	}
	public Location getLocation() {
		return loc;
	}
	public Player getPlayer() {
		return p;
	}
}
