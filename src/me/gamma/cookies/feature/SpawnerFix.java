
package me.gamma.cookies.feature;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



public class SpawnerFix implements CookieListener {

	private boolean enabled;

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Override
	public boolean isEnabled() {
		return this.enabled;
	}


	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event) {
		final ItemStack stackUsed = event.getItemInHand().clone();
		if(event.getBlockPlaced().getType() != Material.SPAWNER || !ItemUtils.isType(stackUsed, Material.SPAWNER))
			return;

		Utils.runLater(() -> {
			if(event.isCancelled() || !event.canBuild())
				return;

			Block block = event.getBlockPlaced();
			if(!(block.getState() instanceof CreatureSpawner spawner))
				return;

			if(!ItemUtils.isType(stackUsed, Material.SPAWNER))
				return;

			if(!(stackUsed.getItemMeta() instanceof BlockStateMeta meta))
				return;

			if(!(meta.getBlockState() instanceof CreatureSpawner metaSpawner))
				return;

			spawner.setSpawnedType(metaSpawner.getSpawnedType());
			spawner.setDelay(metaSpawner.getDelay());
			spawner.setRequiredPlayerRange(metaSpawner.getRequiredPlayerRange());
			spawner.setSpawnRange(metaSpawner.getSpawnRange());
			spawner.setSpawnedEntity(metaSpawner.getSpawnedEntity());
			spawner.setPotentialSpawns(metaSpawner.getPotentialSpawns());

			spawner.setMinSpawnDelay(metaSpawner.getMinSpawnDelay());
			spawner.setMaxSpawnDelay(metaSpawner.getMaxSpawnDelay());
			spawner.setSpawnCount(metaSpawner.getSpawnCount());
			spawner.setMaxNearbyEntities(metaSpawner.getMaxNearbyEntities());

			spawner.update();
		});

	}

}
