
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.machine.AlloyRecipe;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.util.ItemBuilder;



public class Smeltery extends AbstractCraftingMachine {

	public Smeltery(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(this.tier.getTier() > 0) {
			recipes.add(new AlloyRecipe(this, "iron_dust_to_iron_ingot", new ItemStack(Material.IRON_INGOT), 200, Items.IRON_DUST.get()));
			recipes.add(new AlloyRecipe(this, "gold_dust_to_gold_ingot", new ItemStack(Material.GOLD_INGOT), 200, Items.GOLD_DUST.get()));
			recipes.add(new AlloyRecipe(this, "copper_dust_to_copper_ingot", new ItemStack(Material.COPPER_INGOT), 200, Items.COPPER_DUST.get()));
			recipes.add(new AlloyRecipe(this, "aluminum_dust_to_aluminum_ingot", Items.ALUMINUM_INGOT.get(), 200, Items.ALUMINUM_DUST.get()));
			recipes.add(new AlloyRecipe(this, "tin_dust_to_tin_ingot", Items.TIN_INGOT.get(), 200, Items.TIN_DUST.get()));
			recipes.add(new AlloyRecipe(this, "magnesium_dust_to_magnesium_ingot", Items.MAGNESIUM_INGOT.get(), 200, Items.MAGNESIUM_DUST.get()));
			recipes.add(new AlloyRecipe(this, "nickel_dust_to_nickel_ingot", Items.NICKEL_INGOT.get(), 200, Items.NICKEL_DUST.get()));
			recipes.add(new AlloyRecipe(this, "lead_dust_to_lead_ingot", Items.LEAD_INGOT.get(), 200, Items.LEAD_DUST.get()));
			recipes.add(new AlloyRecipe(this, "silver_dust_to_silver_ingot", Items.SILVER_INGOT.get(), 200, Items.SILVER_DUST.get()));
			recipes.add(new AlloyRecipe(this, "steel_dust_to_steel_ingot", Items.STEEL_INGOT.get(), 200, Items.STEEL_DUST.get()));

			if(this.tier.getTier() > 1) {
				recipes.add(new AlloyRecipe(this, "steel_ingot", Items.STEEL_INGOT.get(), 300, new ItemStack(Material.IRON_INGOT), new ItemBuilder(Items.PULVERIZED_COAL).setAmount(2).build()));
				recipes.add(new AlloyRecipe(this, "plastic_sheet", Items.PLASTIC_SHEET.get(), 200, Items.RUBBER_SHEETS.get()));
				recipes.add(new AlloyRecipe(this, "bronze_ingot", new ItemBuilder(Items.BRONZE_INGOT.get()).setAmount(4).build(), 300, new ItemStack(Material.COPPER_INGOT, 3), Items.TIN_INGOT.get()));
				recipes.add(new AlloyRecipe(this, "electrum_ingot", new ItemBuilder(Items.ELECTRUM_INGOT.get()).setAmount(2).build(), 300, new ItemStack(Material.GOLD_INGOT), Items.SILVER_INGOT.get()));
				recipes.add(new AlloyRecipe(this, "invar_ingot", new ItemBuilder(Items.INVAR_INGOT.get()).setAmount(3).build(), 300, new ItemStack(Material.IRON_INGOT, 2), Items.NICKEL_INGOT.get()));
				if(this.tier.getTier() == 2) {
					recipes.add(new AlloyRecipe(this, "aluminum_steel_ingot", Items.ALUMINUM_STEEL_INGOT.get(), 200, Items.ALUMINUM_INGOT.get(), Items.STEEL_INGOT.get()));
					recipes.add(new AlloyRecipe(this, "hardened_metal_simple", Items.HARDENED_METAL.get(), 300, Items.ALUMINUM_STEEL_INGOT.get(), Items.CARBON.get()));
				}

				if(this.tier.getTier() > 2) {
					recipes.add(new AlloyRecipe(this, "hardened_metal", Items.HARDENED_METAL.get(), 400, Items.STEEL_INGOT.get(), Items.CARBON.get(), Items.ALUMINUM_INGOT.get()));
					recipes.add(new AlloyRecipe(this, "redstone_alloy", Items.REDSTONE_ALLOY.get(), 300, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.REDSTONE), Items.SILICON.get()));
					recipes.add(new AlloyRecipe(this, "netherite", new ItemStack(Material.NETHERITE_INGOT), 400, new ItemStack(Material.NETHERITE_SCRAP, 2), new ItemStack(Material.GOLD_INGOT, 2)));
					recipes.add(new AlloyRecipe(this, "hardened_alloy", Items.HARDENED_ALLOY.get(), 400, Items.HARDENED_METAL.get(), Items.TIN_INGOT.get(), new ItemStack(Material.NETHERITE_INGOT)));
					recipes.add(new AlloyRecipe(this, "nether_steel", Items.NETHER_STEEL.get(), 400, Items.HARDENED_ALLOY.get(), new ItemStack(Material.GOLD_INGOT), new ItemBuilder(Items.SOUL_DUST).setAmount(3).build(), new ItemStack(Material.QUARTZ, 2)));
					recipes.add(new AlloyRecipe(this, "blazing_alloy", Items.BLAZING_ALLOY.get(), 600, Items.NETHER_STEEL.get(), new ItemStack(Material.BLAZE_POWDER, 2), new ItemStack(Material.GLOWSTONE_DUST, 4)));

					if(this.tier.getTier() > 3) {
						recipes.add(new AlloyRecipe(this, "ender_steel", Items.ENDER_STEEL.get(), 600, Items.HARDENED_METAL.get(), new ItemStack(Material.ENDER_PEARL, 2)));
						recipes.add(new AlloyRecipe(this, "enderium", Items.ENDERIUM.get(), 600, Items.ENDER_STEEL.get(), Items.BLAZING_ALLOY.get(), new ItemStack(Material.DIAMOND)));
						recipes.add(new AlloyRecipe(this, "energetic_alloy", Items.ENERGETIC_ALLOY.get(), 600, Items.BLAZING_ALLOY.get(), new ItemStack(Material.ENDER_PEARL)));
						recipes.add(new AlloyRecipe(this, "redstonium", Items.REDSTONIUM.get(), 600, Items.REDSTONE_ALLOY.get(), Items.ENDER_STEEL.get(), new ItemStack(Material.REDSTONE, 3), new ItemStack(Material.GLOWSTONE_DUST, 2)));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	public Material getProgressMaterial(double progress) {
		return Material.FLINT_AND_STEEL;
	}


	@Override
	public String getMachineRegistryName() {
		return "smeltery";
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Smeltery";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.SMELTERY;
	}

}
