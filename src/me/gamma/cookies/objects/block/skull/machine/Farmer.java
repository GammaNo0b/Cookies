
package me.gamma.cookies.objects.block.skull.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.Utilities;



public class Farmer extends AbstractSkullBlock implements BlockTicker, Switchable {

	public static Set<Location> locations = new HashSet<>();

	
	public Farmer() {
		register();
	}
	
	@Override
	public String getBlockTexture() {
		return HeadTextures.FARMER;
	}


	@Override
	public String getDisplayName() {
		return "§2Farmer";
	}


	@Override
	public String getRegistryName() {
		return "farmer";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Harvests your crops!");
	}


	@Override
	public long getDelay() {
		return ConfigValues.FARMER_DELAY;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isBlockPowered(block) && this.isInstanceOf(block) && block instanceof Skull;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	private int getRange() {
		return ConfigValues.FARMER_RANGE;
	}


	private int getHeight() {
		return ConfigValues.FARMER_HEIGHT;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("IHI", "GCG", "IAI");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('H', Material.DIAMOND_HOE);
		recipe.setIngredient('C', CustomBlockSetup.STORAGE_CONNECTOR.createDefaultItemStack());
		recipe.setIngredient('A', CustomBlockSetup.ITEM_ABSORBER.createDefaultItemStack());
		return recipe;
	}


	@Override
	public void tick(TileState block) {
		int r = this.getRange();
		int h = this.getHeight();
		for(int y = h; y >= 0; y--) {
			List<Block> toBreak = new ArrayList<>();
			for(int z = -r; z <= r; z++) {
				for(int x = -r; x <= r; x++) {
					List<ItemStack> drops = new ArrayList<>();
					Block current = block.getLocation().clone().add(x, y, z).getBlock();
					switch (current.getType()) {
						case WHEAT:
							Ageable wheat = (Ageable) current.getBlockData();
							if(wheat.getAge() == wheat.getMaximumAge()) {
								wheat.setAge(0);
								current.setBlockData(wheat);
								drops.add(new ItemStack(Material.WHEAT, new Random().nextInt(2) + 1));
								if(new Random().nextDouble() < 1 / 3D)
									drops.add(new ItemStack(Material.WHEAT_SEEDS));
							}
							break;
						case POTATOES:
							Ageable potatoes = (Ageable) current.getBlockData();
							if(potatoes.getAge() == potatoes.getMaximumAge()) {
								potatoes.setAge(0);
								current.setBlockData(potatoes);
								drops.add(new ItemStack(Material.POTATO, new Random().nextInt(5) + 1));
							}
							break;
						case CARROTS:
							Ageable carrots = (Ageable) current.getBlockData();
							if(carrots.getAge() == carrots.getMaximumAge()) {
								carrots.setAge(0);
								current.setBlockData(carrots);
								drops.add(new ItemStack(Material.CARROT, new Random().nextInt(5) + 1));
							}
							break;
						case BEETROOTS:
							Ageable beetroots = (Ageable) current.getBlockData();
							if(beetroots.getAge() == beetroots.getMaximumAge()) {
								beetroots.setAge(0);
								current.setBlockData(beetroots);
								drops.add(new ItemStack(Material.BEETROOT, new Random().nextInt(4) + 1));
								if(new Random().nextDouble() < 1 / 3D)
									drops.add(new ItemStack(Material.BEETROOT_SEEDS));
							}
							break;
						case SUGAR_CANE:
							if(current.getRelative(0, -1, 0).getType() == Material.SUGAR_CANE) {
								current.setType(Material.AIR);
								drops.add(new ItemStack(Material.SUGAR_CANE));
							}
							break;
						case CACTUS:
							if(current.getRelative(0, -1, 0).getType() == Material.CACTUS) {
								current.setType(Material.AIR);
								drops.add(new ItemStack(Material.CACTUS));
							}
							break;
						case COCOA:
							Ageable cocoabeans = (Ageable) current.getBlockData();
							if(cocoabeans.getAge() == cocoabeans.getMaximumAge()) {
								cocoabeans.setAge(0);
								current.setBlockData(cocoabeans);
								drops.add(new ItemStack(Material.COCOA_BEANS, new Random().nextInt(3) + 2));
							}
							break;
						case BAMBOO:
							if(current.getRelative(0, -1, 0).getType() == Material.BAMBOO || current.getRelative(0, -1, 0).getType() == Material.BAMBOO_SAPLING) {
								current.setType(Material.AIR);
								drops.add(new ItemStack(Material.BAMBOO));
							}
							break;
						case KELP:
						case KELP_PLANT:
							if(current.getRelative(0, -1, 0).getType() == Material.KELP || current.getRelative(0, -1, 0).getType() == Material.KELP_PLANT) {
								current.setType(Material.WATER);
								drops.add(new ItemStack(Material.KELP));
							}
							break;
						case PUMPKIN:
							current.setType(Material.AIR);
							drops.add(new ItemStack(Material.PUMPKIN));
							break;
						case MELON:
							current.setType(Material.AIR);
							drops.add(new ItemStack(Material.MELON_SLICE, new Random().nextInt(6) + 3));
							break;
						case SWEET_BERRY_BUSH:
							Ageable sweetberrybush = (Ageable) current.getBlockData();
							if(sweetberrybush.getAge() == 2) {
								sweetberrybush.setAge(1);
								drops.add(new ItemStack(Material.SWEET_BERRIES, new Random().nextInt(2) + 1));
							} else if(sweetberrybush.getAge() == 3) {
								sweetberrybush.setAge(1);
								drops.add(new ItemStack(Material.SWEET_BERRIES, new Random().nextInt(3) + 2));
							}
							current.setBlockData(sweetberrybush);
							break;
						case VINE:
							current.setType(Material.AIR);
							drops.add(new ItemStack(Material.VINE));
							break;
						case NETHER_WART:
							Ageable warts = (Ageable) current.getBlockData();
							if(warts.getAge() == warts.getMaximumAge()) {
								warts.setAge(0);
								current.setBlockData(warts);
								drops.add(new ItemStack(Material.NETHER_WART, new Random().nextInt(2) + 1));
							}
							break;
						case CHORUS_FLOWER:
							boolean doBreakFlower = false;
							for(BlockFace face : Utilities.faces) {
								if(face != BlockFace.UP) {
									if(current.getRelative(face).getType() == Material.CHORUS_PLANT) {
										doBreakFlower = true;
										break;
									}
								}
							}
							if(doBreakFlower) {
								current.setType(Material.AIR);
								drops.add(new ItemStack(Material.CHORUS_FLOWER));
							}
							break;
						case CHORUS_PLANT:
							boolean doBreak = false;
							for(BlockFace face : Utilities.faces) {
								if(face != BlockFace.UP) {
									if(current.getRelative(face).getType() == Material.CHORUS_PLANT) {
										doBreak = true;
										break;
									}
								}
							}
							if(doBreak) {
								toBreak.add(current);
								if(new Random().nextDouble() < 0.25)
									drops.add(new ItemStack(Material.CHORUS_FRUIT));
								break;
							} else {
								current.setType(Material.CHORUS_FLOWER);
							}
						default:
							break;
					}
					for(ItemStack drop : drops) {
						ItemStack rest = Utilities.transferItem(drop, block.getBlock(), Utilities.faces);
						if(rest != null) {
							current.getWorld().dropItem(current.getLocation(), rest);
						}
					}
				}
			}
			toBreak.forEach(b -> b.setType(Material.AIR));
		}
	}

}
