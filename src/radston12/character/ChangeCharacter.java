package radston12.character;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import radston12.CharacterWorld;
import radston12.ncp.Npc;
import radston12.ncp.RightClickNPC;

public class ChangeCharacter implements Listener{

	@EventHandler
	public void on(RightClickNPC event) {
		Npc n = CharacterWorld.NPCs.getNpcById(event.getNpc().getId());
		if(n == null) {
			System.out.println("Wait, somthing went wrong");
			return;
		}
		CharacterWorld.NPCs.delete(n);
	}
	
}
