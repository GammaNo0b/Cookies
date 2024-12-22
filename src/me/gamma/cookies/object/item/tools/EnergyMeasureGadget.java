
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.energy.EnergyConsumer;
import me.gamma.cookies.object.energy.EnergySupplier;
import me.gamma.cookies.object.item.AbstractCustomItem;



public class EnergyMeasureGadget extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "energy_measure_gadget";
	}


	@Override
	public String getTitle() {
		return "§eEnergy Measure Gadget";
	}
	
	
	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Displays the stored energy from energy suppliers and consumers.");
	}


	@Override
	public Material getMaterial() {
		return Material.CLOCK;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		BlockState state = block.getState();
		if(state instanceof TileState) {
			TileState tile = (TileState) state;
			AbstractCustomBlock custom = Blocks.getCustomBlockFromBlock(tile);
			if(custom != null) {
				if(custom instanceof EnergySupplier) {
					EnergySupplier supplier = (EnergySupplier) custom;
					player.sendMessage("§cStored Energy: §6" + supplier.getEnergyOutput(tile).amount());
				} else if(custom instanceof EnergyConsumer) {
					EnergyConsumer consumer = (EnergyConsumer) custom;
					player.sendMessage("§cStored Energy: §6" + consumer.getEnergyInput(tile).amount());
				}
			}
		}
		return true;
	}

}
