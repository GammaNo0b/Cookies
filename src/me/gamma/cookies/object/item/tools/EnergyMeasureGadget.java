
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.energy.EnergyConsumer;
import me.gamma.cookies.object.energy.EnergySupplier;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.collection.PersistentDataObject;



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
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		builder.createSection(null, true).add("§7Displays the stored energy from energy suppliers and consumers.");
	}


	@Override
	public Material getMaterial() {
		return Material.CLOCK;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		AbstractCustomTileEntity<?, ?> custom = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		if(custom != null) {
			if(custom instanceof EnergySupplier) {
				EnergySupplier supplier = (EnergySupplier) custom;
				player.sendMessage("§cStored Energy: §6" + supplier.getEnergyOutput().amount());
			} else if(custom instanceof EnergyConsumer) {
				EnergyConsumer consumer = (EnergyConsumer) custom;
				player.sendMessage("§cStored Energy: §6" + consumer.getEnergyInput().amount());
			}
		}

		return true;
	}

}
