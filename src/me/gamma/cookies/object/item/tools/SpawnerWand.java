
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



public class SpawnerWand extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "spawner_wand";
	}


	@Override
	public String getTitle() {
		return "§aSpawner Wand";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection("§7Powerfull wand to move spawners.", true).add("  §7Right-Click: Print spawner info").add("  §7Left-Click: Pick up spawner");
	}


	@Override
	public Material getMaterial() {
		return Material.BLAZE_ROD;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.printSpawnerStats(player, block);
		return true;
	}


	@Override
	public boolean onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.breakSpawner(player, block);
		return true;
	}


	private void printSpawnerStats(Player player, Block block) {
		if(!(block.getState() instanceof CreatureSpawner spawner)) {
			player.sendMessage("§cThis Block is not moveable.");
			return;
		}

		player.sendMessage("§d==========  §5Spawner Stats  §d==========");
		player.sendMessage("§7Entity Type: §e" + (spawner.getSpawnedType() == null ? "Empty" : Utils.toCapitalWords(spawner.getSpawnedType())));
		player.sendMessage("§7Delay: §b" + spawner.getDelay());
		player.sendMessage("§7Min-Delay: §b" + spawner.getMinSpawnDelay());
		player.sendMessage("§7Max-Delay: §b" + spawner.getMaxSpawnDelay());
		player.sendMessage("§7Spawn Count: §b" + spawner.getSpawnCount());
		player.sendMessage("§7Max nearby Entities: §b" + spawner.getMaxNearbyEntities());
		player.sendMessage("§7Required Player Range: §b" + spawner.getRequiredPlayerRange());
		player.sendMessage("§7Spawning Range: §b" + spawner.getSpawnRange());
	}


	private void breakSpawner(Player player, Block block) {
		BlockState state = block.getState();
		if(!player.isSneaking() && block.getType() == Material.SPAWNER && state instanceof CreatureSpawner) {
			ItemStack spawner = this.getSpawnerItemFromBlockState(state);
			block.setType(Material.AIR);
			ItemUtils.giveItemToPlayer(player, spawner);
		}
	}


	private ItemStack getSpawnerItemFromBlockState(BlockState state) {
		ItemStack stack = new ItemStack(Material.SPAWNER);
		BlockStateMeta meta = (BlockStateMeta) stack.getItemMeta();
		meta.setBlockState(state);
		stack.setItemMeta(meta);
		return stack;
	}

}
