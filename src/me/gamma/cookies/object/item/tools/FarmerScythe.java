
package me.gamma.cookies.object.item.tools;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.ItemUtils;



public class FarmerScythe extends AbstractCustomItem {

	static {
		Harvester.CROPS.register(Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.BEETROOTS, Material.NETHER_WART, Material.COCOA);
		Harvester.BLOCK.register(Material.PUMPKIN, Material.MELON);
		Harvester.TOWER.register(Material.CACTUS, Material.SUGAR_CANE, Material.BAMBOO, Material.KELP_PLANT);
		Harvester.SWEET_BERRIES.register(Material.SWEET_BERRY_BUSH, Material.GLOW_BERRIES);
	}

	@Override
	public String getIdentifier() {
		return "farmers_scythe";
	}


	@Override
	public String getTitle() {
		return "§6Farmer's Scythe";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Harvests only full grown crops in a 3x3 area.");
	}


	@Override
	public Material getMaterial() {
		return Material.IRON_HOE;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.harvest(player, block);
		return true;
	}


	@Override
	public boolean onBlockBreak(Player player, ItemStack stack, Block block, BlockBreakEvent event) {
		this.harvest(player, event.getBlock());
		return true;
	}


	/**
	 * Harvests the blocks around the given block
	 * 
	 * @param player the player who is harvesting
	 * @param block  the central block
	 */
	private void harvest(Player player, Block block) {
		List<ItemStack> drops = new ArrayList<>();
		for(int i = -1; i <= 1; i++)
			for(int j = -1; j <= 1; j++)
				this.harvestCrop(player, block.getRelative(i, 0, j), drops);
		ItemUtils.giveItemsToPlayer(player, drops);
	}


	/**
	 * Harvests the given block and stores the drops in the given list.
	 * 
	 * @param player the player who is harvesting
	 * @param block  the block to be harvested
	 * @param drops  a list to store the drops in
	 */
	private void harvestCrop(Player player, Block block, List<ItemStack> drops) {
		Harvester<?> harvester = Harvester.harvesters.get(block.getType());
		if(harvester != null)
			harvester.harvest(block, player, player.getInventory().getItemInMainHand(), drops);
	}

	/**
	 * Class to define methods to harvest similar behaving blocks like wheat, potatoes, carrots and beetroots.
	 * 
	 * @author gamma
	 *
	 * @param <T> the type of {@link BlockData} this harvester supports
	 */
	public static abstract class Harvester<T extends BlockData> {

		/**
		 * Map with all registered harvesters.
		 */
		static final HashMap<Material, Harvester<?>> harvesters = new HashMap<>();

		/**
		 * Harvester to harvest crops that simply grow till a specific stage at which they can be harvested.
		 */
		static final Harvester<Ageable> CROPS = new Harvester<Ageable>(Ageable.class) {

			@Override
			void harvest(Block block, Ageable data, Player player, ItemStack tool, List<ItemStack> drops) {
				if(data.getAge() < data.getMaximumAge())
					return;

				drops.addAll(block.getDrops(tool, player));
				data.setAge(0);
				block.setBlockData(data);
			}

		};
		/**
		 * Harvester to harvest sweet berry bush which when right-clicked does not reset it's age to zero but to one.
		 */
		static final Harvester<Ageable> SWEET_BERRIES = new Harvester<Ageable>(Ageable.class) {

			@Override
			void harvest(Block block, Ageable data, Player player, ItemStack tool, List<ItemStack> drops) {
				if(data.getAge() < 2)
					return;

				drops.addAll(block.getDrops(tool, player));
				data.setAge(1);
				block.setBlockData(data);
			}

		};
		/**
		 * Harvester to harvest glow berries from the glow berry vines.
		 */
		static final Harvester<CaveVinesPlant> GLOW_BERRIES = new Harvester<CaveVinesPlant>(CaveVinesPlant.class) {

			void harvest(Block block, CaveVinesPlant data, Player player, ItemStack tool, List<ItemStack> drops) {
				if(!data.isBerries())
					return;

				drops.addAll(block.getDrops(tool, player));
				data.setBerries(false);
				block.setBlockData(data);
			};

		};
		/**
		 * Harvester to harvest block platns that grow on stems like pumpkins and melons.
		 */
		static final Harvester<BlockData> BLOCK = new Harvester<BlockData>(BlockData.class) {

			@Override
			void harvest(Block block, BlockData data, Player player, ItemStack tool, List<ItemStack> drops) {
				drops.addAll(block.getDrops(tool, player));
				block.setType(Material.AIR);
			}

		};
		/**
		 * Harvester to harvest plants that grow directly upwards like sugarcane, kelp, bamboo and cactus.
		 */
		static final Harvester<BlockData> TOWER = new Harvester<BlockData>(BlockData.class) {

			@Override
			void harvest(Block block, BlockData data, Player player, ItemStack tool, List<ItemStack> drops) {
				Material type = block.getType();
				int x = block.getX();
				int y = block.getY();
				int z = block.getZ();
				if(block.getRelative(0, -1, 0).getType() != type)
					y++;
				int maxy = y;
				while(maxy < block.getWorld().getMaxHeight()) {
					if(block.getWorld().getBlockAt(x, maxy, z).getType() != type)
						break;
					maxy++;
				}
				for(; maxy >= y; maxy--) {
					Block plant = block.getWorld().getBlockAt(x, maxy, z);
					drops.addAll(plant.getDrops(tool, player));
					plant.setType(Material.AIR);
				}
			}

		};

		final Class<T> blockDataClass;

		Harvester(Class<T> blockDataClass) {
			this.blockDataClass = blockDataClass;
		}


		/**
		 * Registers this harvester for the given materials.
		 * 
		 * @param materials the materials that should be harvested using this harvester
		 */
		void register(Material... materials) {
			for(Material material : materials)
				harvesters.put(material, this);
		}


		/**
		 * Harvests the given block with the given tool and stores the drops in the given list.
		 * 
		 * @param block  the harvested block
		 * @param player the harvesting player
		 * @param tool   the tool that was used
		 * @param drops  the list to store the drops
		 */
		void harvest(Block block, Player player, ItemStack tool, List<ItemStack> drops) {
			T data = this.blockDataClass.cast(block.getBlockData());
			this.harvest(block, data, player, tool, drops);
		}


		/**
		 * Same method as {@link Harvester#harvest(Block, Player, ItemStack, List)} with the specific data as additional argument. This method has to be
		 * implemented in order for the harvester to work.
		 * 
		 * @param block  the harvested block
		 * @param data   the data of the block
		 * @param player the harvesting player
		 * @param tool   the tool that was used
		 * @param drops  the list to store the drops
		 */
		abstract void harvest(Block block, T data, Player player, ItemStack tool, List<ItemStack> drops);

	}

}
