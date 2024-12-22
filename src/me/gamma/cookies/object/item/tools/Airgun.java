
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.PropertyBuilder;



public class Airgun extends AbstractCustomItem {

	private static final IntegerProperty AMMUNITION = new IntegerProperty("ammunition");

	@Override
	public String getIdentifier() {
		return "airgun";
	}


	@Override
	public String getTitle() {
		return "§eAirgun";
	}


	@Override
	public Material getMaterial() {
		return Material.BREEZE_ROD;
	}


	@Override
	protected PropertyBuilder buildItemProperties(PropertyBuilder builder) {
		return super.buildItemProperties(builder).add(AMMUNITION);
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		player.launchProjectile(WindCharge.class, player.getLocation().getDirection());
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		player.launchProjectile(WindCharge.class, player.getLocation().getDirection());
		return true;
	}

}
