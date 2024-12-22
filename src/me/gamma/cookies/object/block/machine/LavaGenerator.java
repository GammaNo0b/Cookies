
package me.gamma.cookies.object.block.machine;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.block.AbstractWorkBlock;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ItemUtils;



public class LavaGenerator extends AbstractWorkBlock implements FluidSupplier, Switchable {

	public static final IntegerProperty LAVA = Properties.LAVA;

	private final MachineTier tier;

	private int frequency;
	private int capacity;
	private int generation;

	public LavaGenerator(MachineTier tier) {
		this.tier = tier;
	}


	@Override
	public ConfigurationSection getConfig() {
		ConfigurationSection section = Config.MACHINES.getConfig().getConfigurationSection("lava_generator");
		return this.tier == null ? section : section.getConfigurationSection(this.tier.name().toLowerCase());
	}


	@Override
	public void configure(ConfigurationSection config) {
		this.frequency = config.getInt("frequency", 20);
		this.capacity = config.getInt("capacity", 1000);
		this.generation = config.getInt("generation", 1);
	}


	@Override
	public String getIdentifier() {
		StringBuilder builder = new StringBuilder("lava_generator");
		if(this.tier != null)
			builder.append("_tier_").append(this.tier.name().toLowerCase());
		return builder.toString();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.LAVA_GENERATOR;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(FLUID_OUTPUT_ACCESS_FLAGS, (byte) 0x3F).add(LAVA);
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(super.onBlockRightClick(player, block, stack, event))
			return true;

		EquipmentSlot hand = event.getHand();

		if(stack != null && stack.getType() == Material.BUCKET && !ItemUtils.isCustomItem(stack)) {
			if(LAVA.remove(block, 1000)) {
				block.update();
				ItemStack lava = new ItemStack(Material.LAVA_BUCKET);
				if(stack.getAmount() == 1) {
					player.getInventory().setItem(hand, lava);
				} else {
					stack.setAmount(stack.getAmount() - 1);
					ItemUtils.giveItemToPlayer(player, lava);
				}
			}
		} else {
			player.sendMessage("§6Stored Lava: §c" + LAVA.fetch(block) + "mb");
		}
		return true;
	}


	@Override
	public List<FluidProvider> getFluidOutputs(TileState block) {
		return Arrays.asList(FluidProvider.fromProperty(FluidType.LAVA, LAVA, block, this.capacity));
	}


	@Override
	public int getWorkPeriod() {
		return this.frequency;
	}


	@Override
	public void tick(TileState block) {
		int lava = LAVA.fetch(block);
		LAVA.store(block, Math.min(this.capacity, lava + this.generation));
		this.tryPushFluid(block);
		block.update();
	}

}
