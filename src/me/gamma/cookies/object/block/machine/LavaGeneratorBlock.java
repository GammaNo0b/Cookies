
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.LavaGenerator;
import me.gamma.cookies.util.ItemUtils;



public class LavaGeneratorBlock extends AbstractCustomTileBlock<LavaGeneratorBlock, LavaGenerator> {

	private final MachineTier tier;

	private int frequency;
	private int capacity;
	private int generation;

	public LavaGeneratorBlock(MachineTier tier) {
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


	public int getFrequency() {
		return this.frequency;
	}


	public int getCapacity() {
		return this.capacity;
	}


	public int getGeneration() {
		return this.generation;
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
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		EquipmentSlot hand = event.getHand();

		LavaGenerator generator = this.getTileEntity(block);
		if(generator == null)
			return true;

		if(stack != null && stack.getType() == Material.BUCKET && !ItemUtils.isCustomItem(stack)) {
			if(generator.removeLava(1000)) {
				ItemStack lava = new ItemStack(Material.LAVA_BUCKET);
				if(stack.getAmount() == 1) {
					player.getInventory().setItem(hand, lava);
				} else {
					stack.setAmount(stack.getAmount() - 1);
					ItemUtils.giveItemToPlayer(player, lava);
				}
			}
		} else {
			player.sendMessage("§6Stored Lava: §c" + generator.getLava() + "mb");
		}

		return true;
	}


	@Override
	public LavaGeneratorBlock castCustomBlock() {
		return this;
	}


	@Override
	public LavaGenerator createNewTileEntity(Block block) {
		return new LavaGenerator(this, block);
	}

}
