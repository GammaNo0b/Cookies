package me.gamma.cookies.objects.block.skull;

import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.Utilities;

public interface TextureChanger {
	
	default void changeBlockTexture(Skull skull, String texture) {
		Utilities.setSkullTexture(skull, texture);
	}
	
	default void changeBlockTexture(ItemStack skull, String texture) {
		Utilities.setSkullTexture(skull, texture);
	}

}
