package radston12.skin;

import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;


public class SkinGrabber {

	public static Skin get(String playername) {
		try {
			URL uuidURL = new URL("https://api.mojang.com/users/profiles/minecraft/" + playername);
			InputStreamReader reader = new InputStreamReader(uuidURL.openStream());
			String playeruuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
			reader.close();
			URL skinURL = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + playeruuid + "?unsigned=false");
			InputStreamReader reader2 = new InputStreamReader(skinURL.openStream());
			JsonObject skindata = new JsonParser().parse(reader2).getAsJsonObject()
					.get("properties").getAsJsonArray().get(0).getAsJsonObject();
			reader2.close();
			String sig = skindata.get("signature").getAsString();
			String texture =skindata.get("value").getAsString();
			
			return new Skin(texture,sig);
		} catch(Exception e) {
			System.out.println("Error at loading skin");
			return null;
		}
	}
	
	public static Skin getFromUUID(String playeruuid) {
		try {
			URL skinURL = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + playeruuid + "?unsigned=false");
			InputStreamReader reader2 = new InputStreamReader(skinURL.openStream());
			JsonObject skindata = new JsonParser().parse(reader2).getAsJsonObject()
					.get("properties").getAsJsonObject().get("0").getAsJsonObject();
			reader2.close();
			String sig = skindata.get("signature").getAsString();
			String texture =skindata.get("value").getAsString();
			
			return new Skin(texture,sig);
		} catch(Exception e) {
			System.out.println("Wrong UUID!");
			return null;
		}
	}
	
	public static Skin getFromPlayer(Player p) {
		Property prof = ((CraftPlayer)p).getHandle().getProfile().getProperties().get("textures").iterator().next();
		return new Skin(prof.getValue(),prof.getSignature());
	}
}
