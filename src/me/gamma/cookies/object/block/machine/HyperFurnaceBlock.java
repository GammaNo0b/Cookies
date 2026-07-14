
package me.gamma.cookies.object.block.machine;


import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.FurnaceRecipe;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;
import me.gamma.cookies.object.tile.machine.HyperFurnace;
import me.gamma.cookies.util.Utils;



public class HyperFurnaceBlock extends AbstractCraftingMachineBlock<HyperFurnaceBlock, HyperFurnace> {

	private final int level;
	private final List<MachineRecipe> recipes;

	public HyperFurnaceBlock(MachineTier tier, int level) {
		super(tier);
		this.level = level;

		this.recipes = StreamSupport.stream(Spliterators.spliteratorUnknownSize(Bukkit.recipeIterator(), 0), false).filter(r -> r instanceof FurnaceRecipe).map(r -> (FurnaceRecipe) r).map(r -> new SimpleMachineRecipe(this, r.getKey().getKey(), r.getResult(), r.getInputChoice(), r.getCookingTime())).collect(Collectors.toList());
	}


	@Override
	public ConfigurationSection getConfig() {
		return Config.MACHINES.getConfig().getConfigurationSection("hyper_furnace").getConfigurationSection("level" + this.level);
	}


	@Override
	public List<MachineRecipe> getAllMachineRecipes() {
		return this.recipes;
	}


	@Override
	public String getMachineRegistryName() {
		return "hyper_furnace_" + this.level;
	}


	@Override
	public String getTitle() {
		return String.format("§%cHyper Furnace %s", this.tier.getColorcode(), Utils.romanNumber(this.level));
	}


	@Override
	public Material getMaterial() {
		return Material.FURNACE;
	}


	@Override
	public HyperFurnaceBlock castCustomBlock() {
		return this;
	}


	@Override
	public HyperFurnace createNewTileEntity(Block block) {
		return new HyperFurnace(this, block);
	}

}
