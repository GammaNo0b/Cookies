
package me.gamma.cookies.object.block.machine;


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

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class Farmer extends AbstractItemProcessingMachine {

	private static final Random random = new Random();

	private static final ByteProperty RELATIVE_X = new ByteProperty("x");
	private static final ByteProperty RELATIVE_Z = new ByteProperty("z");
	private static final ListProperty<ItemStack, ItemStackProperty> DROPS = new ListProperty<>("drops", ItemStackProperty::new);

	public Farmer(MachineTier tier) {
		super(tier);
	}


	@Override
	public String getTitle() {
		return this.tier.getDescription() + " Farmer";
	}


	@Override
	public String getMachineRegistryName() {
		return "farmer";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.FARMER;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return switch (this.tier) {
			case BASIC -> Material.IRON_HOE;
			case ADVANCED -> Material.GOLDEN_HOE;
			case IMPROVED -> Material.DIAMOND_HOE;
			case PERFECTED -> Material.NETHERITE_HOE;
			default -> null;
		};
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(RELATIVE_X).add(RELATIVE_Z).add(DROPS);
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		for(ItemStack stack : DROPS.fetch(block))
			drops.add(stack);

		return drops;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		for(int i : new int[] { 9, 10, 11, 18, 20, 27, 28, 29 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);

		for(int i : new int[] { 13, 17, 22, 26, 31, 35 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);

		return gui;
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 3;
	}


	@Override
	protected int getUpgradeSlot() {
		return 12;
	}


	@Override
	protected int getProgressSlot() {
		return 21;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 30;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 39;
	}


	@Override
	protected int getOutputModeSlot() {
		return 42;
	}


	@Override
	public void getAllowedUpgrades(ArrayList<MachineUpgrade> upgrades) {
		super.getAllowedUpgrades(upgrades);
		upgrades.add(MachineUpgrade.FORTUNE);
		upgrades.add(MachineUpgrade.RANGE);
	}


	public int getFortune(TileState block) {
		return this.getUpgradeLevel(block, MachineUpgrade.FORTUNE);
	}


	public int getRange(TileState block) {
		return this.getTier().getTier() + this.getUpgradeLevel(block, MachineUpgrade.RANGE);
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { 19 };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 14, 15, 16, 23, 24, 25, 32, 33, 34 };
	}


	private int[] nextXZ(int range, int x, int z) {
		if(++x > range) {
			x = -range;
			if(++z > range) {
				z = -range;
			}
		}
		return new int[] { x, z };
	}


	@Override
	protected int createNextProcess(TileState block) {
		super.createNextProcess(block);

		int range = this.getRange(block);
		Block fertilize = block.getBlock().getRelative(random.nextInt(-range, range + 1), 0, random.nextInt(-range, range + 1));
		if(fertilize.getBlockData() instanceof Ageable ageable) {
			int age = ageable.getAge();
			int max = ageable.getMaximumAge();
			if(age < max) {
				if(Supplier.supply(new ItemStack(Material.BONE_MEAL), 1, this.getItemInputs(block)) > 0) {
					ageable.setAge(Math.min(age + random.nextInt(2, 5), max));
					fertilize.setBlockData(ageable);
					fertilize.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, fertilize.getLocation(), random.nextInt(5, 13), 0.5D, 0.5D, 0.5D);
				}
			}
		}

		int x = RELATIVE_X.fetch(block);
		int z = RELATIVE_Z.fetch(block);

		int[] next = this.nextXZ(range, x, z);
		RELATIVE_X.store(block, (byte) next[0]);
		RELATIVE_Z.store(block, (byte) next[1]);
		block.update();

		Block relative = block.getBlock().getRelative(x, 0, z);
		if(x == 0 && z == 0 || relative.isEmpty())
			return 0;

		BlockHarvester harvester = harvesters.get(relative.getType());
		if(harvester == null)
			return 0;

		List<ItemStack> drops = harvester.harvest(relative, this.getFortune(block));
		drops.removeIf(ItemUtils::isEmpty);
		if(drops.isEmpty())
			return 0;

		DROPS.store(block, drops);
		block.update();

		return 200;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		List<ItemStack> drops = DROPS.fetch(block);
		while(!this.storeOutputs(block, drops)) {
			if(!this.tryPushItems(block)) {
				DROPS.store(block, drops);
				return false;
			}
		}

		DROPS.store(block, drops);
		while(this.tryPushItems(block));

		return true;
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
