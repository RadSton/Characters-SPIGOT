package radston12.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import radston12.CharacterWorld;
import radston12.skin.SkinChanger;
import radston12.skin.SkinGrabber;

public class Join implements Listener{

	@EventHandler
	public void on(PlayerJoinEvent e ) {
		CharacterWorld.NPCs.inject(e.getPlayer());
		SkinChanger.change(e.getPlayer(), SkinGrabber.get("awesomeelina"),null);
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e) {
		CharacterWorld.NPCs.uninject(e.getPlayer());
	}
	
	@EventHandler 
	public void on(AsyncPlayerChatEvent e) {
		SkinChanger.change(e.getPlayer(), SkinGrabber.get(e.getMessage()),null);
	}
}
