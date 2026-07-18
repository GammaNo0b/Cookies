
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.FarmerBlock;
import me.gamma.cookies.object.block.machine.MachineUpgrade;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.core.MinecraftWorldHelper;



public class Farmer extends AbstractItemProcessingMachine<Farmer, FarmerBlock> {

	private static final Random random = new Random();

	private static final int BONEMEAL_SLOT = 19;

	private static final String KEY_DROPS = "drops";

	private final List<ItemStack> drops = new ArrayList<>();
	private int x, z;

	public Farmer(FarmerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		List<PersistentDataObject> drops = data.getObjectList(KEY_DROPS);
		if(drops != null)
			for(PersistentDataObject drop : drops)
				this.drops.add(PersistentDataUtils.loadItemStack(drop));

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		List<PersistentDataObject> drops = new ArrayList<>();
		for(ItemStack drop : this.drops) {
			PersistentDataObject dobj = new PersistentDataObject(data.getAdapterContext());
			PersistentDataUtils.saveItemStack(dobj, drop);
			drops.add(dobj);
		}
		data.setObjectList(KEY_DROPS, drops);

		return true;
	}


	@Override
	public void destroy() {
		super.destroy();

		for(ItemStack drop : this.drops)
			ItemUtils.dropItem(drop, this.block);
	}


	@Override
	public int[] getInputSlots() {
		return new int[] { BONEMEAL_SLOT };
	}


	@Override
	public int[] getOutputSlots() {
		return new int[] { 14, 15, 16, 23, 24, 25, 32, 33, 34 };
	}


	private void nextXZ() {
		int range = this.getRange();
		if(++this.x > range) {
			this.x = -range;
			if(++this.z > range) {
				this.z = -range;
			}
		}
	}


	private void fertilize() {
		int range = this.getRange();
		Block fertilize = this.block.getRelative(random.nextInt(-range, range + 1), 0, random.nextInt(-range, range + 1));
		Inventory gui = this.getInventory();
		ItemStack bonemeal = gui.getItem(BONEMEAL_SLOT);
		if(!ItemUtils.isEmpty(bonemeal) && ItemUtils.isType(bonemeal, Material.BONE_MEAL)) {
			if(fertilize.applyBoneMeal(CollectionUtils.randomElement(BlockUtils.cartesian))) {
				MinecraftWorldHelper.addGrowthParticles(fertilize);
				ItemUtils.increaseItem(bonemeal, -1);
			}
		}
	}


	private int harvest() {
		this.nextXZ();

		Block relative = this.block.getRelative(this.x, 0, this.z);
		if(x == 0 && z == 0 || relative.isEmpty())
			return 0;

		BlockHarvester harvester = harvesters.get(relative.getType());
		if(harvester == null)
			return 0;

		Collection<ItemStack> drops = harvester.harvest(relative, this.getFortune());
		if(drops == null)
			return 0;

		this.drops.addAll(drops);

		return 200;
	}


	@Override
	protected int createNextProcess() {
		super.createNextProcess();

		this.fertilize();

		return this.harvest();
	}


	@Override
	protected boolean finishProcess() {
		while(!this.storeOutputs(this.drops))
			if(!this.tryPushItems())
				return false;

		this.tryPushItems();

		return true;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return switch (this.customBlock.getTier()) {
			case BASIC -> Material.IRON_HOE;
			case ADVANCED -> Material.GOLDEN_HOE;
			case IMPROVED -> Material.DIAMOND_HOE;
			case PERFECTED -> Material.NETHERITE_HOE;
			default -> null;
		};
	}


	@Override
	public Farmer castTileEntity() {
		return this;
	}


	@Override
	public void getAllowedUpgrades(List<MachineUpgrade> upgrades) {
		super.getAllowedUpgrades(upgrades);
		upgrades.add(MachineUpgrade.LUCK);
		upgrades.add(MachineUpgrade.RANGE);
	}


	public int getFortune() {
		return this.getUpgradeLevel(MachineUpgrade.LUCK);
	}


	public int getRange() {
		return this.customBlock.getTier().getTier() + this.getUpgradeLevel(MachineUpgrade.RANGE);
	}

	private static final Map<Material, BlockHarvester> harvesters = new HashMap<>();

	private static void registerHarvester(Material type, BlockHarvester harvester) {
		harvesters.put(type, harvester);
	}


	private static void registerHarvester(Tag<Material> types, BlockHarvester harvester) {
		types.getValues().forEach(type -> registerHarvester(type, harvester));
	}

	static {
		registerHarvester(Material.WHEAT, BlockHarvester.CROP_HARVESTER);
		registerHarvester(Material.POTATOES, BlockHarvester.CROP_HARVESTER);
		registerHarvester(Material.CARROTS, BlockHarvester.CROP_HARVESTER);
		registerHarvester(Material.BEETROOTS, BlockHarvester.CROP_HARVESTER);
		registerHarvester(Material.NETHER_WART, BlockHarvester.CROP_HARVESTER);
		registerHarvester(Material.SUGAR_CANE, BlockHarvester.SUGAR_CANE_HARVESTER);
		registerHarvester(Material.CACTUS, BlockHarvester.SUGAR_CANE_HARVESTER);
		registerHarvester(Material.CACTUS_FLOWER, BlockHarvester.SIMPLE_BLOCK_HARVESTER);
		registerHarvester(Material.BAMBOO, BlockHarvester.SUGAR_CANE_HARVESTER);
		registerHarvester(Material.KELP, BlockHarvester.SUGAR_CANE_HARVESTER);
		registerHarvester(Material.PUMPKIN, BlockHarvester.SIMPLE_BLOCK_HARVESTER);
		registerHarvester(Material.MELON, BlockHarvester.SIMPLE_BLOCK_HARVESTER);
		registerHarvester(Material.SWEET_BERRY_BUSH, BlockHarvester.SWEET_BERRY_HARVESTER);
		registerHarvester(Material.COCOA, BlockHarvester.COCOA_HARVESTER);
		registerHarvester(Material.CAVE_VINES, BlockHarvester.GLOW_BERRIES_HARVESTER);
		registerHarvester(Material.CAVE_VINES_PLANT, BlockHarvester.GLOW_BERRIES_HARVESTER);
		registerHarvester(Material.CHORUS_PLANT, BlockHarvester.CHORUS_HARVESTER);
		registerHarvester(Material.CHORUS_FLOWER, BlockHarvester.CHORUS_HARVESTER);
		registerHarvester(Tag.SMALL_FLOWERS, BlockHarvester.SIMPLE_BLOCK_HARVESTER);
	}

	private static Collection<ItemStack> getDrops(Block block, Material tool, int fortune) {
		return block.getDrops(tool == null ? null : new ItemBuilder(tool).addEnchantment(Enchantment.FORTUNE, fortune).build());
	}


	private static List<ItemStack> removeSeed(Collection<ItemStack> drops, Predicate<Material> isSeed) {
		List<ItemStack> list = new ArrayList<>();
		Iterator<ItemStack> iterator = drops.iterator();
		while(iterator.hasNext()) {
			ItemStack drop = iterator.next();
			list.add(drop);
			if(isSeed.test(drop.getType())) {
				ItemUtils.increaseItem(drop, -1);
				iterator.forEachRemaining(list::add);
				return list;
			}
		}
		return null;
	}

	@FunctionalInterface
	private static interface BlockHarvester {

		Set<Material> seeds = Set.of(Material.WHEAT_SEEDS, Material.POTATO, Material.CARROT, Material.BEETROOT_SEEDS, Material.NETHER_WART);

		BlockHarvester SIMPLE_BLOCK_HARVESTER = (Block block, int fortune) -> {
			Material type = block.getType();
			block.setType(Material.AIR);
			return List.of(new ItemStack(type, 1 + random.nextInt(1 + fortune)));
		};
		BlockHarvester CROP_HARVESTER = (Block block, int fortune) -> {
			BlockData data = block.getBlockData();
			if(!(data instanceof Ageable crop))
				return null;

			if(crop.getAge() < crop.getMaximumAge())
				return null;

			Collection<ItemStack> drops = getDrops(block, Material.DIAMOND_HOE, fortune);
			List<ItemStack> list = removeSeed(drops, seeds::contains);
			if(list == null) {
				block.setType(Material.AIR);
				return new ArrayList<>(drops);
			} else {
				crop.setAge(0);
				block.setBlockData(crop);
				return list;
			}
		};
		BlockHarvester SUGAR_CANE_HARVESTER = (Block block, int fortune) -> {
			Material type = block.getType();
			int amount = 0;
			Block b;
			for(b = block.getRelative(BlockFace.UP); b.getType() == type; b = b.getRelative(BlockFace.UP)) {
				b.setType(Material.AIR);
				++amount;
			}
			boolean cactusFlower = false;
			if(b.getType() == Material.CACTUS_FLOWER) {
				cactusFlower = true;
				b.setType(Material.AIR);
			}

			if(block.getRelative(BlockFace.DOWN).getType() == type) {
				block.setType(Material.AIR);
				++amount;
			}

			if(amount <= 0)
				return null;

			amount += (int) Math.round(random.nextDouble() * amount * fortune);

			List<ItemStack> drops = ItemUtils.getManyItems(new ItemStack(type), amount);
			if(cactusFlower)
				drops.add(new ItemStack(Material.CACTUS_FLOWER));
			return drops;

		};
		BlockHarvester COCOA_HARVESTER = (Block block, int fortune) -> {
			BlockData data = block.getBlockData();
			if(!(data instanceof Ageable ageable))
				return null;

			if(ageable.getAge() < ageable.getMaximumAge())
				return null;

			ageable.setAge(0);
			block.setBlockData(ageable);

			int beans = 3 + random.nextInt(1 + fortune);
			return List.of(new ItemStack(Material.COCOA_BEANS, beans));
		};
		BlockHarvester SWEET_BERRY_HARVESTER = (Block block, int fortune) -> {
			BlockData data = block.getBlockData();
			if(!(data instanceof Ageable ageable))
				return null;

			if(ageable.getAge() < ageable.getMaximumAge())
				return null;

			ageable.setAge(1);
			block.setBlockData(ageable);

			int berries = random.nextInt(2, 5) + random.nextInt(1 + fortune);
			return List.of(new ItemStack(Material.SWEET_BERRIES, berries));
		};
		BlockHarvester GLOW_BERRIES_HARVESTER = (Block block, int fortune) -> {
			int vines = 0;
			int berries = 0;
			for(Block b = block.getRelative(BlockFace.DOWN); b.getBlockData() instanceof CaveVinesPlant plant; b = b.getRelative(BlockFace.DOWN)) {
				if(plant.isBerries())
					berries += 1 + random.nextInt(1 + fortune);

				b.setType(Material.AIR);
				++vines;
			}

			if(block.getBlockData() instanceof CaveVinesPlant plant && plant.isBerries()) {
				berries += 1 + random.nextInt(1 + fortune);
				plant.setBerries(false);
				block.setBlockData(plant);
			}

			if(block.getRelative(BlockFace.UP).getType() == Material.CAVE_VINES_PLANT) {
				block.setType(Material.AIR);
				++vines;
			}

			if(vines <= 0 && berries <= 0)
				return null;

			return ItemUtils.getManyItems(new ItemStack(Material.GLOW_BERRIES), berries);
		};
		BlockHarvester CHORUS_HARVESTER = new BlockHarvester() {

			private static final BlockFace[] directions = { BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

			private boolean breakChorusBreadthFirst(Block block, Set<Block> visited, int[] chorus) {
				boolean destroy = true;
				for(BlockFace direction : directions) {
					Block next = block.getRelative(direction);
					if(visited.add(next)) {
						if(next.getType() == Material.CHORUS_PLANT) {
							if(!this.breakChorusBreadthFirst(next, visited, chorus))
								destroy = false;
						} else if(next.getType() == Material.CHORUS_FLOWER) {
							if(next.getBlockData() instanceof Ageable flower && flower.getAge() < flower.getMaximumAge()) {
								destroy = false;
							} else {
								++chorus[1];
								next.setType(Material.AIR);
							}
						}
					}
				}

				if(!destroy)
					return false;

				block.setType(Material.AIR);
				++chorus[0];

				return true;
			}


			@Override
			public Collection<ItemStack> harvest(Block block, int fortune) {
				if(block.getType() == Material.CHORUS_FLOWER) {
					if(block.getBlockData() instanceof Ageable flower && flower.getAge() == flower.getMaximumAge()) {
						flower.setAge(0);
						block.setBlockData(flower);
						return Collections.emptyList();
					}
					return null;
				}

				if(block.getType() != Material.CHORUS_PLANT)
					return null;

				int[] chorus = { 0, 0 };
				if(this.breakChorusBreadthFirst(block, new HashSet<>(List.of(block)), chorus)) {
					if(chorus[1] > 0 && block.getRelative(BlockFace.DOWN).getType() == Material.END_STONE) {
						--chorus[1];
						block.setType(Material.CHORUS_FLOWER);
					}
				}

				if(chorus[0] <= 0 && chorus[1] <= 0)
					return null;

				int chorusFruits = (int) Math.round(random.nextDouble() * 0.5D * (1.0D + fortune) * chorus[0]);
				List<ItemStack> drops = ItemUtils.getManyItems(new ItemStack(Material.CHORUS_FRUIT), chorusFruits);
				drops.addAll(ItemUtils.getManyItems(new ItemStack(Material.CHORUS_FLOWER), chorus[1]));
				return drops;
			}

		};

		// TODO: BlockHarvester TREE_HARVESTER = (Block block, int fortune) -> { return null; };

		Collection<ItemStack> harvest(Block block, int fortune);

	}

}
