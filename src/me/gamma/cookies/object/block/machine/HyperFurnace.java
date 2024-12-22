
package me.gamma.cookies.object.block.machine;


import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



public class HyperFurnace extends AbstractCraftingMachine {

	private final int level;
	private final List<MachineRecipe> recipes;

	public HyperFurnace(MachineTier tier, int level) {
		super(tier);
		this.level = level;

		this.recipes = StreamSupport.stream(Spliterators.spliteratorUnknownSize(Bukkit.recipeIterator(), 0), false).filter(r -> r instanceof FurnaceRecipe).map(r -> (FurnaceRecipe) r).map(r -> new SimpleMachineRecipe(this, r.getKey().getKey(), r.getResult(), r.getInputChoice(), r.getCookingTime())).collect(Collectors.toList());
	}


	@Override
	public ConfigurationSection getConfig() {
		return Config.MACHINES.getConfig().getConfigurationSection("hyper_furnace").getConfigurationSection("level" + this.level);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		return this.recipes;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.FLINT_AND_STEEL;
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
	protected int createNextProcess(TileState block) {
		int i = super.createNextProcess(block);
		Furnace furnace = (Furnace) block.getBlockData();
		furnace.setLit(i > 0);
		block.setBlockData(furnace);
		return i;
	}


	@Override
	public void tick(TileState block) {
		org.bukkit.block.Furnace furnace = (org.bukkit.block.Furnace) block;
		FurnaceInventory inventory = furnace.getSnapshotInventory();
		ItemStack stack = inventory.getSmelting();
		if(!ItemUtils.isEmpty(stack)) {
			inventory.setSmelting(this.addStack(block, stack));
			furnace.update();
		}
		super.tick(block);
	}


	@Override
	public double getHalfWidth() {
		// negative to put the wire on the opposite of the block
		return -0.5D;
	}


	@Override
	public Vector getCenter() {
		return new Vector(0.5D, 0.5D, 0.5D);
	}

}
