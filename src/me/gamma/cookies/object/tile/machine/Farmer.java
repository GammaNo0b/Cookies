
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.block.machine.FarmerBlock;
import me.gamma.cookies.object.block.machine.MachineUpgrade;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class Farmer extends AbstractItemProcessingMachine<Farmer, FarmerBlock> {

	private static final Random random = new Random();

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
	protected int[] getInputSlots() {
		return new int[] { 19 };
	}


	@Override
	protected int[] getOutputSlots() {
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
		if(fertilize.getBlockData() instanceof Ageable ageable) {
			int age = ageable.getAge();
			int max = ageable.getMaximumAge();
			if(age < max) {
				if(Supplier.supply(new ItemStack(Material.BONE_MEAL), 1, this.getItemInputs()) > 0) {
					ageable.setAge(Math.min(age + random.nextInt(2, 5), max));
					fertilize.setBlockData(ageable);
					fertilize.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, fertilize.getLocation(), random.nextInt(5, 13), 0.5D, 0.5D, 0.5D);
				}
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

		List<ItemStack> drops = harvester.harvest(relative, this.getFortune());
		drops.removeIf(ItemUtils::isEmpty);
		if(drops.isEmpty())
			return 0;

		this.drops.clear();
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
		while(!this.storeOutputs(this.drops)) {
			if(!this.tryPushItems()) {
				return false;
			}
		}

		while(this.tryPushItems());

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

	static {
		harvesters.put(Material.WHEAT, BlockHarvester.CROP_HARVESTER);
		harvesters.put(Material.POTATOES, BlockHarvester.CROP_HARVESTER);
		harvesters.put(Material.CARROTS, BlockHarvester.CROP_HARVESTER);
		harvesters.put(Material.BEETROOTS, BlockHarvester.CROP_HARVESTER);
		harvesters.put(Material.NETHER_WART, BlockHarvester.CROP_HARVESTER);
		harvesters.put(Material.SUGAR_CANE, BlockHarvester.SUGAR_CANE_HARVESTER);
		harvesters.put(Material.CACTUS, BlockHarvester.SUGAR_CANE_HARVESTER);
		harvesters.put(Material.BAMBOO, BlockHarvester.SUGAR_CANE_HARVESTER);
		harvesters.put(Material.KELP, BlockHarvester.SUGAR_CANE_HARVESTER);
		harvesters.put(Material.PUMPKIN, BlockHarvester.PUMPKIN_HARVESTER);
		harvesters.put(Material.MELON, BlockHarvester.PUMPKIN_HARVESTER);
		harvesters.put(Material.SWEET_BERRY_BUSH, BlockHarvester.SWEET_BERRY_HARVESTER);
		harvesters.put(Material.COCOA, BlockHarvester.COCOA_HARVESTER);
		harvesters.put(Material.CAVE_VINES, BlockHarvester.GLOW_BERRIES_HARVESTER);
		harvesters.put(Material.CAVE_VINES_PLANT, BlockHarvester.GLOW_BERRIES_HARVESTER);
	}

	@FunctionalInterface
	private static interface BlockHarvester {

		Set<Material> seeds = Set.of(Material.WHEAT_SEEDS, Material.POTATO, Material.CARROT, Material.BEETROOT_SEEDS, Material.NETHER_WART);

		BlockHarvester CROP_HARVESTER = (Block block, int fortune) -> {
			BlockData data = block.getBlockData();
			if(!(data instanceof Ageable crop))
				return Collections.emptyList();

			if(crop.getAge() < crop.getMaximumAge())
				return Collections.emptyList();

			ItemStack tool = new ItemBuilder(Material.DIAMOND_HOE).addEnchantment(Enchantment.FORTUNE, fortune).build();
			Collection<ItemStack> drops = block.getDrops(tool);
			List<ItemStack> list = removeSeed(drops, seeds::contains);
			if(list == null) {
				block.setType(Material.AIR);
				return new ArrayList<>(drops);
			} else {
				crop.setAge(0);
				block.setBlockData(data);
				return list;
			}
		};
		BlockHarvester SUGAR_CANE_HARVESTER = (Block block, int fortune) -> {
			int y = block.getY();
			int i;

			Material type = block.getType();
			Block top;
			for(i = 1; i < block.getWorld().getMaxHeight() - y; i++) {
				top = block.getRelative(0, i, 0);
				if(top.getType() == type) {
					top.setType(top.getBlockData() instanceof Waterlogged water && water.isWaterlogged() ? Material.WATER : Material.AIR);
				} else {
					break;
				}
			}

			if(block.getRelative(0, -1, 0).getType() == type) {
				block.setType(Material.AIR);
			} else {
				i--;
			}

			i = i + random.nextInt(1 + i * fortune);

			List<ItemStack> drops = new ArrayList<>();
			int max = type.getMaxStackSize();
			while(i >= max) {
				drops.add(new ItemStack(type, max));
				i -= max;
			}
			drops.add(new ItemStack(type, i));
			return drops;

		};
		BlockHarvester PUMPKIN_HARVESTER = (Block block, int fortune) -> {
			List<ItemStack> drops = new ArrayList<>(block.getDrops(new ItemBuilder(Material.DIAMOND_HOE).addEnchantment(Enchantment.FORTUNE, fortune).build()));
			block.setType(Material.AIR);
			return drops;
		};
		BlockHarvester COCOA_HARVESTER = (Block block, int fortune) -> {
			BlockData data = block.getBlockData();
			if(!(data instanceof Ageable ageable))
				return new ArrayList<>();

			if(ageable.getAge() < ageable.getMaximumAge())
				return new ArrayList<>();

			ageable.setAge(0);
			block.setBlockData(data);
			int beans = 2 + fortune + random.nextInt(1 + fortune);
			return Arrays.asList(new ItemStack(Material.COCOA_BEANS, beans));
		};
		BlockHarvester SWEET_BERRY_HARVESTER = (Block block, int fortune) -> {
			BlockData data = block.getBlockData();
			if(!(data instanceof Ageable ageable))
				return new ArrayList<>();

			int age = ageable.getAge();
			if(age < 2)
				return new ArrayList<>();

			ageable.setAge(1);
			block.setBlockData(data);

			int berries = age - 1;
			if(random.nextBoolean())
				berries++;

			berries += fortune + random.nextInt(1 + fortune);
			return Arrays.asList(new ItemStack(Material.SWEET_BERRIES, berries));
		};
		BlockHarvester GLOW_BERRIES_HARVESTER = (Block block, int fortune) -> {
			BlockData data = block.getBlockData();
			if(!(data instanceof CaveVinesPlant vines))
				return new ArrayList<>();

			if(!vines.isBerries())
				return new ArrayList<>();

			vines.setBerries(false);
			block.setBlockData(data);
			int berries = 1 + random.nextInt(1 + fortune);
			return Arrays.asList(new ItemStack(Material.GLOW_BERRIES, berries));
		};

		// TODO: BlockHarvester CHORUS_HARVESTER = (Block block, int fortune) -> { return null; };

		// TODO: BlockHarvester TREE_HARVESTER = (Block block, int fortune) -> { return null; };

		List<ItemStack> harvest(Block block, int fortune);


		static List<ItemStack> removeSeed(Collection<ItemStack> drops, Predicate<Material> isSeed) {
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

	}

}
