
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.block.AbstractWorkBlock;
import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.EnumProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ItemUtils;



public class BlockGenerator extends AbstractWorkBlock implements Switchable {

	public static final EnumProperty<RedstoneMode> REDSTONE_MODE = Properties.REDSTONE_MODE;

	private final String identifier;
	private final IItemSupplier generator;
	private final int frequency;

	public BlockGenerator(String identifier, IItemSupplier generator, int frequency) {
		this.identifier = identifier;
		this.generator = generator;
		this.frequency = frequency;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	protected int getWorkPeriod() {
		return this.frequency;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COBBLESTONE_GENERATOR;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(REDSTONE_MODE);
	}


	@Override
	public void tick(TileState block) {
		if(REDSTONE_MODE.fetch(block).isActive(this.isBlockPowered(block))) {
			ItemStack rest = ItemUtils.transferItem(this.generator.get(), block.getBlock());
			ItemUtils.dropItem(rest, block.getLocation().add(0.5D, 0.5D, 0.5D));
		}
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(!player.isSneaking() && ItemUtils.isType(stack, Material.REDSTONE_TORCH)) {
			player.sendMessage("§cRedstone mode set to " + REDSTONE_MODE.cycle(block).getTitle());
			block.update();
			return true;
		}
		return false;
	}

}
