
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.ReflectionUtils;



public class MiniaturizingWand extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "miniaturizing_wand";
	}


	@Override
	public String getTitle() {
		return "§aMiniaturizing Wand";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Turns blocks into small cubes.");
	}


	@Override
	public Material getMaterial() {
		return Material.STICK;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.miniaturizeBlock(player, block);
		return true;
	}


	@Override
	public boolean onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.miniaturizeBlock(player, block);
		return true;
	}


	private void miniaturizeBlock(Player player, Block block) {
		if(Cookies.isInstalled("HeadDrops")) {
			ItemStack blocks = (ItemStack) ReflectionUtils.wrapClass("me.gamma.headdrops.list.BlockHeads").wrapMethod("getBlockHeadFromBlock", ReflectionUtils.wrapClass("me.gamma.headdrops.util.Head").getWrappedClass()).setParameters(block).invoke().getValue();
			if(!ItemUtils.isEmpty(blocks)) {
				block.setType(Material.AIR);
				blocks.setAmount(8);
				ItemUtils.giveItemToPlayer(player, blocks);
			}
		} else {
			player.sendMessage("§cThe HeadDrops Plugin is not installed!");
		}
	}

}
