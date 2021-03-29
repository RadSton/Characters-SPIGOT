package radston12.character;

import java.util.HashMap;

import org.bukkit.entity.Player;

import radston12.CharacterWorld;

public class CharacterManager {

	private HashMap<String, Character> characters = new HashMap<>();
	
	public void add(String name,Character character) {
		characters.put(name, character);
	}
	
	public void create(Player p, String name) {
		//Save Inventory CHECK
		String saveTo = "inventorys." + p.getName() + "." + name;
		CharacterWorld.INVUTIL.saveInventory(p.getInventory(), saveTo);
		
		//Add to hashmap
		//characters.put(p.getName() +"." +  name, )
		//Spawn NPC
	}
	
}
