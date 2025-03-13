
package me.gamma.cookies.object.item.tools;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.init.MultiBlockInit;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.ItemUtils;



public class PortableCraftingTable extends PortableInventoryOpener {

	@Override
	protected void openInventory(Player player) {
		player.openInventory(MenuType.CRAFTING.builder().title(this.getTitle()).build(player));
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.CRAFTING_TABLE;
	}


	@Override
	public String getIdentifier() {
		return "portable_crafting_table";
	}


	@Override
	public String getTitle() {
		return "§6Portable Crafting Table";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Right click custom multiblock structures to turn them into their portable version.");
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(MultiBlockInit.CUSTOM_CRAFTING_TABLE.isMultiBlock(block)) {
			stack.setAmount(stack.getAmount() - 1);
			ItemUtils.giveItemToPlayer(player, Items.PORTABLE_CUSTOM_CRAFTING_TABLE.get());
			return true;
		} else if(MultiBlockInit.ENGINEERING_STATION.isMultiBlock(block)) {
			stack.setAmount(stack.getAmount() - 1);
			ItemUtils.giveItemToPlayer(player, Items.PORTABLE_ENGINEERING_STATION.get());
			return true;
		} else if(MultiBlockInit.MAGIC_ALTAR.isMultiBlock(block)) {
			stack.setAmount(stack.getAmount() - 1);
			ItemUtils.giveItemToPlayer(player, Items.PORTABLE_MAGIC_ALTAR.get());
			return true;
		} else if(MultiBlockInit.KITCHEN.isMultiBlock(block)) {
			stack.setAmount(stack.getAmount() - 1);
			ItemUtils.giveItemToPlayer(player, Items.PORTABLE_KITCHEN.get());
			return true;
		}
		return super.onBlockRightClick(player, stack, block, event);
	}

}
