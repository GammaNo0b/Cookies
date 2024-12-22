
package me.gamma.cookies.object.item.resources;


import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.Vector;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.block.network.WireHolder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.VectorProperty;
import me.gamma.cookies.util.ItemUtils;



public class InsulatedCopperWire extends AbstractCustomItem {

	public static final VectorProperty HOOK_POS = Properties.POS;

	@Override
	public String getIdentifier() {
		return "insulated_copper_wire";
	}


	@Override
	public String getTitle() {
		return "§fInsulated Copper Wire";
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.INSULATED_COPPER_WIRE;
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection("", false).add("§7Shift-Right click to set first position.").add("§7Right click to connect wire.");
	}


	@Override
	protected PropertyBuilder buildItemProperties(PropertyBuilder builder) {
		return super.buildItemProperties(builder).add(HOOK_POS);
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(block.getState() instanceof TileState state && Blocks.getCustomBlockFromBlock(state) instanceof WireHolder holder) {
			ItemMeta meta = stack.getItemMeta();
			if(player.isSneaking()) {
				HOOK_POS.store(meta, block.getLocation().toVector());
			} else {
				Vector pos = HOOK_POS.fetch(meta);
				if(pos != null && !block.getLocation().toVector().equals(pos) && holder.createWire(state, pos.toLocation(block.getWorld())))
					if(player.getGameMode() == GameMode.SURVIVAL)
						stack.setAmount(stack.getAmount() - 1);
			}
			stack.setItemMeta(meta);
		}

		return super.onBlockRightClick(player, stack, block, event);
	}


	@Override
	public boolean onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		ItemMeta meta = stack.getItemMeta();
		HOOK_POS.storeEmpty(meta);
		stack.setItemMeta(meta);
		return super.onAirLeftClick(player, stack, event);
	}


	@Override
	public boolean onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(block.getState() instanceof TileState state && Blocks.getCustomBlockFromBlock(state) instanceof WireHolder holder)
			if(holder.removeWire(state))
				ItemUtils.dropItem(Items.INSULATED_COPPER_WIRE.get(), block);

		return super.onBlockLeftClick(player, stack, block, event);
	}

}