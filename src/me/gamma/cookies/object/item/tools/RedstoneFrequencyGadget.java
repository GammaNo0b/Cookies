
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.object.property.Properties;



public class RedstoneFrequencyGadget extends AbstractCustomItem {

	public static final ByteProperty REDSTONE_FREQUENCY = Properties.REDSTONE_FREQUENCY;

	@Override
	public String getIdentifier() {
		return "redstone_frequency_gadget";
	}


	@Override
	public String getTitle() {
		return "§eRedstone Frequency Gadget";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Click a redstone transmitter or receiver").add("§7to change it's frequency or sneak").add("§7to display it's current value.");
	}


	@Override
	public Material getMaterial() {
		return Material.CLOCK;
	}


	@Override
	public boolean onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.handleClick(player, block, player.isSneaking(), false);
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.handleClick(player, block, player.isSneaking(), true);
		return true;
	}


	private void handleClick(Player player, Block block, boolean display, boolean increase) {
		if(block.getState() instanceof Skull) {
			Skull skull = (Skull) block.getState();
			if(Blocks.WIRELESS_REDSTONE_TRANSMITTER.isInstanceOf(skull) || Blocks.WIRELESS_REDSTONE_RECEIVER.isInstanceOf(skull)) {
				if(display) {
					player.sendMessage("§aFrequency: §2" + REDSTONE_FREQUENCY.fetch(skull));
				} else {
					int frequency = REDSTONE_FREQUENCY.fetch(skull) & 0xFF;
					frequency = (frequency + (increase ? 1 : -1) + 16) % 16;
					REDSTONE_FREQUENCY.store(skull, (byte) frequency);
					skull.update();
					player.sendMessage("§aNew Frequency: §2" + frequency);
				}
			}
		}
	}

}
