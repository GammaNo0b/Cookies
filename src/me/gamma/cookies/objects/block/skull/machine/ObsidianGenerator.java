
package me.gamma.cookies.objects.block.skull.machine;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.Fluid;
import me.gamma.cookies.objects.FluidType;
import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.FluidSupplier;
import me.gamma.cookies.objects.block.ItemProvider;
import me.gamma.cookies.objects.block.ItemSupplier;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.FluidProperty;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.Utilities;



public class ObsidianGenerator extends AbstractSkullBlock implements BlockTicker, Switchable, ItemSupplier {

	public static final FluidProperty LAVA = FluidType.LAVA.createProperty();
	public static final IntegerProperty STORED_OBSIDIAN = new IntegerProperty("obsidian");

	private final HashSet<Location> locations = new HashSet<>();

	public ObsidianGenerator() {
		register();
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public void tick(TileState block) {
		Fluid lava = LAVA.fetch(block);
		int millibuckets = this.removeLava(block, 1000 - lava.getMillibuckets()) + lava.getMillibuckets();
		if(millibuckets < 1000) {
			lava.setMilliBuckets(millibuckets);
		} else {
			lava.setMilliBuckets(0);
			STORED_OBSIDIAN.increase(block, 1);
		}
		LAVA.store(block, lava);
		block.update();
	}


	private int removeLava(TileState block, int max) {
		int millibuckets = 0;
		for(BlockFace face : Utilities.faces) {
			Block relative = block.getBlock().getRelative(face);
			BlockState state = relative.getState();
			if(state instanceof TileState) {
				TileState tile = (TileState) state;
				AbstractTileStateBlock custom = CustomBlockSetup.getCustomBlockFromTileState(tile);
				if(!(custom instanceof FluidSupplier))
					continue;
				FluidSupplier supplier = (FluidSupplier) custom;
				Fluid fluid = supplier.removeFluid(tile, FluidType.LAVA, max);
				int amount = fluid.getMillibuckets();
				max -= amount;
				millibuckets += amount;
				if(max <= 0)
					break;
			}
		}
		return millibuckets;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isInstanceOf(block) && this.isBlockPowered(block);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.OBSIDIAN_GENERATOR;
	}


	@Override
	public String getRegistryName() {
		return "obsidian_generator";
	}


	@Override
	public String getDisplayName() {
		return "§5Obsidian Generator";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("SWS", "STS", "SHS");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('W', Material.WATER_BUCKET);
		recipe.setIngredient('T', Material.TINTED_GLASS);
		recipe.setIngredient('H', Material.HOPPER);
		return recipe;
	}


	@Override
	public List<ItemProvider> getOutputStackHolders(TileState block) {
		return Arrays.asList(new ItemProvider() {

			@Override
			public ItemStack get() {
				int obsidian = STORED_OBSIDIAN.fetch(block);
				int amount = Math.min(obsidian, 64);
				STORED_OBSIDIAN.store(block, obsidian - amount);
				block.update();
				return new ItemStack(Material.OBSIDIAN, amount);
			}


			@Override
			public void set(ItemStack value) {
				if(Utilities.isEmpty(value) || value.getType() != Material.OBSIDIAN || AbstractCustomItem.isCustomItem(value))
					return;
				STORED_OBSIDIAN.increase(block, value.getAmount());
				block.update();
			}

		});
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		LAVA.storeEmpty(block);
		STORED_OBSIDIAN.storeEmpty(block);
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		int obsidian = STORED_OBSIDIAN.fetch(block);
		while(obsidian > 0) {
			Utilities.giveItemToPlayer(player, new ItemStack(Material.OBSIDIAN, Math.min(obsidian, 64)));
			obsidian -= 64;
		}
		return super.onBlockBreak(player, block, event);

	}

}
