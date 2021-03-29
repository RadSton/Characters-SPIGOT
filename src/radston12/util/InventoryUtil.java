package radston12.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class InventoryUtil {
	private FileConfiguration conf;
	private JavaPlugin pl;
	
	public InventoryUtil(JavaPlugin main) {
		this.conf = main.getConfig();
		this.pl = main;
	}


	public void saveInventory(final Inventory inventory, String save) {
	    conf.set(save,inventoryToBase64(inventory));
		pl.saveConfig();
	}
    public void loadInventory(final Inventory playerinventory, String loadFrom){
    	try {
    		String data = conf.getString(loadFrom);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            CraftInventoryCustom inventory = new CraftInventoryCustom(null, dataInput.readInt());
            
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            playerinventory.setContents(inventory.getContents());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String inventoryToBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
