
package me.gamma.cookies.object.item.tools;


import org.bukkit.entity.Player;

import me.gamma.cookies.object.list.HeadTextures;



public class PortableEnderChest extends PortableInventoryOpener {

	@Override
	protected void openInventory(Player player) {
		player.openInventory(player.getEnderChest());
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.ENDER_CHEST;
	}


	@Override
	public String getIdentifier() {
		return "portable_ender_chest";
	}


	@Override
	public String getTitle() {
		return "§3Portable Ender Chest";
	}

}
