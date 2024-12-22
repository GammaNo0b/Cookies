
package me.gamma.cookies.object.block.network.item;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.PropertyBuilder;



public class EnderAccessor extends AbstractCustomBlock implements ItemStorage, Ownable {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ENDER_ACCESSOR;
	}


	@Override
	public String getIdentifier() {
		return "ender_accessor";
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(ITEM_INPUT_ACCESS_FLAGS, (byte) 0x3F).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public List<Provider<ItemStack>> getItemProviders(TileState block) {
		Player player = this.getOwningPlayer(block);
		return player == null ? new ArrayList<>() : ItemProvider.fromInventory(player.getEnderChest());
	}


	@Override
	public boolean canPlace(Player player, Block block) {
		return player != null && super.canPlace(player, block);
	}

}
