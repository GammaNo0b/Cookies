
package me.gamma.cookies.objects.block.skull.machine;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.Fluid;
import me.gamma.cookies.objects.FluidType;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.FluidProvider;
import me.gamma.cookies.objects.block.FluidSupplier;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.machine.MachineTier;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.list.TieredMaterials;
import me.gamma.cookies.objects.property.FluidProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;



public class CompactLavaGenerator extends AbstractSkullBlock implements BlockTicker, Switchable, FluidSupplier {

	public static final FluidProperty LAVA = new FluidProperty("lava", FluidType.LAVA);

	private final HashSet<Location> locations = new HashSet<>();
	private final MachineTier tier;
	private final int max_lava_millibuckets;
	private final int speed;

	public CompactLavaGenerator(MachineTier tier) {
		this.tier = tier;
		this.speed = (int) Math.pow(4, tier.getTier());
		this.max_lava_millibuckets = 1000 * this.speed;

		register();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COMPACT_LAVA_GENERATOR;
	}


	@Override
	public String getRegistryName() {
		return "compact_lava_generator_tier_" + this.tier.getTier();
	}


	@Override
	public String getDisplayName() {
		return this.tier.getName() + " §6Compact Lava Generator";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("ILI", "LCL", "ILI");
		ItemStack generator = null;
		switch (this.tier) {
			case BASIC:
				generator = CustomBlockSetup.LAVA_GENERATOR.createDefaultItemStack();
				break;
			case ADVANCED:
				generator = CustomBlockSetup.COMPACT_LAVA_GENERATOR.createDefaultItemStack();
				break;
			case IMPROVED:
				generator = CustomBlockSetup.ADVANCED_COMPACT_LAVA_GENERATOR.createDefaultItemStack();
				break;
			case PERFECTED:
				generator = CustomBlockSetup.IMPROVED_COMPACT_LAVA_GENERATOR.createDefaultItemStack();
				break;
		}
		recipe.setIngredient('I', TieredMaterials.getIngot(this.tier));
		recipe.setIngredient('L', generator);
		recipe.setIngredient('C', MachineCasing.getByTier(this.tier).createDefaultItemStack());
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		LAVA.storeEmpty(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		LAVA.transfer(usedItem.getItemMeta(), block);
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemStack stack = super.onBlockBreak(player, block, event);
		ItemMeta meta = stack.getItemMeta();
		LAVA.transfer(block, meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		if(event.getHand() == EquipmentSlot.OFF_HAND)
			return true;

		ItemStack stack = player.getInventory().getItemInMainHand();
		boolean bucket = stack != null && stack.getType() == Material.BUCKET && !AbstractCustomItem.isCustomItem(stack);
		if(bucket) {
			if(LavaGenerator.removeBucket(block)) {
				player.getInventory().setItemInMainHand(new ItemStack(Material.LAVA_BUCKET));
			}
		} else {
			player.sendMessage("§6Stored Lava: §c" + LAVA.fetch(block).getMillibuckets() + "mb");
		}
		return bucket;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public List<FluidProvider> getOutputFluidHolders(TileState block) {
		return Arrays.asList(FluidProvider.fromProperty(LAVA, block));
	}


	@Override
	public void tick(TileState block) {
		Fluid lava = LAVA.fetch(block);
		if(lava.getMillibuckets() < this.max_lava_millibuckets) {
			lava.grow(this.speed);
			LAVA.store(block, lava);
			block.update();
		}
	}


	@Override
	public long getDelay() {
		return 20;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isInstanceOf(block) && this.isBlockPowered(block);
	}

}
