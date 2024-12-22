
package me.gamma.cookies.object.item.tools;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;



public class LumberAxe extends VeinMinerTool {

	private static final Material[] logs = { Material.OAK_LOG, Material.OAK_WOOD, Material.SPRUCE_LOG, Material.SPRUCE_WOOD, Material.BIRCH_LOG, Material.BIRCH_WOOD, Material.DARK_OAK_LOG, Material.DARK_OAK_WOOD, Material.ACACIA_LOG, Material.ACACIA_WOOD, Material.JUNGLE_LOG, Material.JUNGLE_WOOD, Material.MANGROVE_LOG, Material.MANGROVE_WOOD, Material.CRIMSON_STEM, Material.CRIMSON_HYPHAE, Material.WARPED_STEM, Material.WARPED_HYPHAE };

	@Override
	public String getIdentifier() {
		return "lumber_axe";
	}


	@Override
	public String getTitle() {
		return "§9Lumberaxe";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Fells a whole tree.");
	}


	@Override
	public Material getMaterial() {
		return Material.DIAMOND_AXE;
	}


	@Override
	protected boolean isValidMaterial(Material material) {
		for(Material m : logs)
			if(m == material)
				return true;
		return false;
	}


	@Override
	protected Set<Block> getNextGeneration(Block block) {
		Set<Block> blocks = new HashSet<>();
		for(int y = 0; y <= 1; y++)
			for(int z = -1; z <= 1; z++)
				for(int x = -1; x <= 1; x++)
					blocks.add(block.getRelative(x, y, z));
		return blocks;
	}

}
