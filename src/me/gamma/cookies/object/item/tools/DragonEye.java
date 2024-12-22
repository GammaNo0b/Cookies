
package me.gamma.cookies.object.item.tools;


import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.StructureSearchResult;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.list.HeadTextures;



public class DragonEye extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "dragon_eye";
	}


	@Override
	public String getTitle() {
		return "§bDragon Eye";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Multi-use eye of ender.");
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.DRAGON_EYE;
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		StructureSearchResult result = player.getWorld().locateNearestStructure(player.getLocation(), StructureType.STRONGHOLD, 100, false);
		if(result == null) {
			player.sendMessage("§cUnable to find the nearest Stronghold.");
		} else {
			EnderSignal signal = (EnderSignal) player.getWorld().spawnEntity(player.getEyeLocation().add(player.getLocation().getDirection()), EntityType.EYE_OF_ENDER);
			signal.setTargetLocation(result.getLocation());
			signal.setDropItem(false);
		}
		return true;
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onEntitySpawn(EntitySpawnEvent event) {
				if(event.getEntity() instanceof EnderSignal signal)
					signal.setDropItem(true);
			}

		};
	}

}
