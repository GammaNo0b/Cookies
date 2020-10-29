package me.gamma.cookies.objects.block.skull;

import org.bukkit.block.Skull;

import me.gamma.cookies.util.Utilities;

public interface TextureChanger {
	
	default void changeBlockTexture(Skull skull, String texture) {
		Utilities.setSkullTexture(skull, texture);
	}

}
