package radston12.ncp;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.minecraft.server.v1_16_R3.EntityPlayer;

public class RightClickNPC extends Event implements Cancellable {
	
	public final Player p;
	public final EntityPlayer npc;
	public boolean isCancelled;
	private static final HandlerList HANDLERS = new HandlerList();
	
	public RightClickNPC(Player p,EntityPlayer e) {
		this.p =p;
		this.npc = e;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}
	
	@Override
	public void setCancelled(boolean arg0) {
		this.isCancelled = arg0;
	}
	
	public EntityPlayer getNpc() {
		return npc;
	}
	public Player getPlayer() {
		return p;
	}
}
