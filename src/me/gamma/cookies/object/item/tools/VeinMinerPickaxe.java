
package me.gamma.cookies.object.item.tools;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;



public class VeinMinerPickaxe extends VeinMinerTool {

	private static final Material[] ores = new Material[] { Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON_BLOCK, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.RAW_COPPER_BLOCK, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.RAW_GOLD_BLOCK, Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS };

	@Override
	public String getIdentifier() {
		return "vein_miner_pickaxe";
	}


	@Override
	public String getTitle() {
		return "§bVein Miner Pickaxe";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Mines a whole vine of the same ore.");
	}


	@Override
	public Material getMaterial() {
		return Material.DIAMOND_PICKAXE;
	}


	@Override
	protected boolean isValidMaterial(Material material) {
		for(Material m : ores)
			if(m == material)
				return true;
		return false;
	}


	@Override
	protected Set<Block> getNextGeneration(Block block) {
		Set<Block> blocks = new HashSet<>();
		for(int y = -1; y <= 1; y++)
			for(int z = -1; z <= 1; z++)
				for(int x = -1; x <= 1; x++)
					blocks.add(block.getRelative(x, y, z));
		return blocks;
	}

}
