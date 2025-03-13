
package me.gamma.cookies.object.block.network.item;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.AdvancedFilterBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.gui.util.ItemFilterGui;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.network.NetworkInterface;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.PropertyCompound;
import me.gamma.cookies.util.BlockUtils;



public class StorageImporter extends AbstractCustomBlock implements NetworkInterface<ItemStack>, AdvancedFilterBlock<ItemStack, ItemFilter>, Cartesian {

	public static final PropertyCompound<ItemFilter> ITEM_FILTER = Properties.createItemFilterProperty();
	public static final IntegerProperty PRIORITY = Properties.PRIORITY;
	public static final ByteProperty CHANNEL = Properties.CHANNEL;
	public static final ByteProperty BLOCK_FACE_ACCESS_FLAGS = new ByteProperty("blockfaceaccessflags");

	@Override
	public int getPriority(TileState block) {
		return PRIORITY.fetch(block);
	}


	@Override
	public void setPriority(TileState block, int priority) {
		PRIORITY.store(block, priority);
		block.update();
	}


	@Override
	public int getChannel(TileState block) {
		return CHANNEL.fetch(block);
	}


	@Override
	public void setChannel(TileState block, int channel) {
		CHANNEL.store(block, (byte) (channel & 0xFF));
		block.update();
	}


	@Override
	public List<Provider<ItemStack>> getInputs(TileState block) {
		List<Provider<ItemStack>> inputs = new ArrayList<>();
		byte flags = BLOCK_FACE_ACCESS_FLAGS.fetch(block);
		for(BlockFace face : BlockUtils.cartesian) {
			if(!BlockFaceConfigurable.isFaceEnabled(flags, block, face))
				continue;

			BlockState state = block.getBlock().getRelative(face).getState();

			if(state instanceof TileState tile) {
				if(Blocks.getCustomBlockFromBlock(tile) instanceof ItemSupplier supplier) {
					inputs.addAll(supplier.getItemOutputs(tile, face.getOppositeFace()));
					continue;
				}
			}

			if(state instanceof BlockInventoryHolder holder)
				inputs.addAll(ItemProvider.fromInventory(holder.getInventory()));
		}
		return inputs;
	}


	@Override
	public Filter<ItemStack> getInputFiler(TileState block) {
		return this.getFilter(block);
	}


	@Override
	public List<Provider<ItemStack>> getOutputs(TileState block) {
		return new ArrayList<>();
	}


	@Override
	public Filter<ItemStack> getOutputFilter(TileState block) {
		return (_, _) -> 0;
	}


	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}


	@Override
	public String getFilterTitle() {
		return "§2-> §8Storage Importer §2<-";
	}


	@Override
	public ItemFilter getFilter(TileState block) {
		return ITEM_FILTER.fetch(block);
	}


	@Override
	public void setFilter(TileState block, ItemFilter filter) {
		ITEM_FILTER.store(block, filter);
		block.update();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_IMPORTER;
	}


	@Override
	public String getIdentifier() {
		return "storage_importer";
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(ITEM_FILTER, new ItemFilter()).add(PRIORITY).add(CHANNEL).add(BLOCK_FACE_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public boolean canPlace(Player player, Block block) {
		if(player == null)
			return false;

		if(this.checkForAdjacentNotOwnedNetworks(player.getUniqueId(), block.getLocation()))
			return false;

		return super.canPlace(player, block);
	}


	@Override
	public boolean onBlockPlace(Player player, PersistentDataHolder holder, TileState block) {
		if(super.onBlockPlace(player, holder, block))
			return true;

		if(player == null)
			return true;

		return !this.setup(block, player.getUniqueId());
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(!player.isSneaking()) {
			ItemFilterGui.open(player, block, this, BLOCK_FACE_ACCESS_FLAGS, Material.GREEN_STAINED_GLASS_PANE);
			return true;
		}
		return false;
	}

}
