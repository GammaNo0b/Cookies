
package me.gamma.cookies.object.block.organic;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.Pair;



public class FruitTreeSaplingBlock extends AbstractCustomBlock {

	private static final Map<TreeType, Pair<Material, Material>> treeMaterials = new HashMap<>();

	private static void registerTreeMaterial(TreeType type, Material stem, Material leaves) {
		treeMaterials.put(type, new Pair<>(stem, leaves));
	}

	static {
		registerTreeMaterial(TreeType.TREE, Material.OAK_LOG, Material.OAK_LEAVES);
		registerTreeMaterial(TreeType.BIG_TREE, Material.OAK_LOG, Material.OAK_LEAVES);
		registerTreeMaterial(TreeType.REDWOOD, Material.SPRUCE_LOG, Material.SPRUCE_LEAVES);
		registerTreeMaterial(TreeType.TALL_REDWOOD, Material.SPRUCE_LOG, Material.SPRUCE_LEAVES);
		registerTreeMaterial(TreeType.BIRCH, Material.BIRCH_LOG, Material.BIRCH_LEAVES);
		registerTreeMaterial(TreeType.JUNGLE, Material.JUNGLE_LOG, Material.JUNGLE_LEAVES);
		registerTreeMaterial(TreeType.SMALL_JUNGLE, Material.JUNGLE_LOG, Material.JUNGLE_LEAVES);
		registerTreeMaterial(TreeType.COCOA_TREE, Material.JUNGLE_LOG, Material.JUNGLE_LEAVES);
		registerTreeMaterial(TreeType.JUNGLE_BUSH, Material.OAK_LOG, Material.JUNGLE_LEAVES);
		registerTreeMaterial(TreeType.RED_MUSHROOM, Material.MUSHROOM_STEM, Material.RED_MUSHROOM_BLOCK);
		registerTreeMaterial(TreeType.BROWN_MUSHROOM, Material.MUSHROOM_STEM, Material.BROWN_MUSHROOM_BLOCK);
		registerTreeMaterial(TreeType.SWAMP, Material.OAK_LOG, Material.OAK_LEAVES);
		registerTreeMaterial(TreeType.ACACIA, Material.ACACIA_LOG, Material.ACACIA_LEAVES);
		registerTreeMaterial(TreeType.DARK_OAK, Material.DARK_OAK_LOG, Material.DARK_OAK_LEAVES);
		registerTreeMaterial(TreeType.MEGA_REDWOOD, Material.SPRUCE_LOG, Material.SPRUCE_LEAVES);
		registerTreeMaterial(TreeType.MEGA_PINE, Material.SPRUCE_LOG, Material.SPRUCE_LEAVES);
		registerTreeMaterial(TreeType.TALL_BIRCH, Material.BIRCH_LOG, Material.BIRCH_LEAVES);
		registerTreeMaterial(TreeType.CHORUS_PLANT, Material.CHORUS_PLANT, Material.CHORUS_FLOWER);
		registerTreeMaterial(TreeType.CRIMSON_FUNGUS, Material.CRIMSON_STEM, Material.NETHER_WART_BLOCK);
		registerTreeMaterial(TreeType.WARPED_FUNGUS, Material.WARPED_STEM, Material.WARPED_WART_BLOCK);
		registerTreeMaterial(TreeType.AZALEA, Material.OAK_LOG, Material.AZALEA_LEAVES);
		registerTreeMaterial(TreeType.MANGROVE, Material.MANGROVE_LOG, Material.MANGROVE_LEAVES);
		registerTreeMaterial(TreeType.TALL_MANGROVE, Material.MANGROVE_LOG, Material.MANGROVE_LEAVES);
		registerTreeMaterial(TreeType.CHERRY, Material.CHERRY_LOG, Material.CHERRY_LEAVES);
		registerTreeMaterial(TreeType.PALE_OAK, Material.PALE_OAK_LOG, Material.PALE_OAK_LEAVES);
		registerTreeMaterial(TreeType.PALE_OAK_CREAKING, Material.PALE_OAK_LOG, Material.PALE_OAK_LEAVES);
	}

	private final String identifier;
	private final Material type;
	private final TreeType treeType;
	private final Material stem;
	private final AbstractCustomBlock leaveBlock;

	public FruitTreeSaplingBlock(String identifier, Material type, TreeType treeType, Material stem, AbstractCustomBlock leaveBlock) {
		this.identifier = identifier;
		this.type = type;
		this.treeType = treeType;
		this.stem = stem;
		this.leaveBlock = leaveBlock;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public Material getMaterial() {
		return this.type;
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	@Override
	public boolean onBlockStructureGrow(Block block, StructureGrowEvent event) {
		if(!this.breakBlock(block))
			return false;

		Pair<Material, Material> pair = treeMaterials.get(this.treeType);
		if(pair == null)
			return false;

		block.getWorld().generateTree(block.getLocation(), new Random(), this.treeType, state -> {
			Material oldType = state.getType();
			if(oldType == pair.left) {
				state.setType(this.stem);
			} else if(oldType == pair.right) {
				Location l = state.getLocation();
				Utils.runLater(() -> this.leaveBlock.place(l.getBlock(), null));
			}
		});

		return false;
	}

}
