
package me.gamma.cookies.object.item.armor;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;



public class FarmerBoots extends AbstractCustomArmorItem {

	@Override
	public String getIdentifier() {
		return "farmer_boots";
	}


	@Override
	public String getTitle() {
		return "§eFarmer's Boots";
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.BOOTS;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	protected void editItemMeta(ItemMeta meta) {
		((LeatherArmorMeta) meta).setColor(Color.YELLOW);
	}
	
	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection("§7Prevents the trampelling of crops.", false);
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onFarmlandTrample(PlayerInteractEvent event) {
				if(event.getAction() == Action.PHYSICAL) {
					Block block = event.getClickedBlock();
					if(block != null && block.getType() == Material.FARMLAND)
						if(!block.getRelative(BlockFace.UP).isEmpty())
							if(FarmerBoots.this.isEquipped(event.getPlayer()))
								event.setCancelled(true);
				}
			}

		};
	}

}
