
package me.gamma.cookies.object.item.tools;


import java.util.Arrays;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.NBTUtils;
import me.gamma.cookies.util.NBTWrapper;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.PersistentDataObject;
import net.minecraft.nbt.CompoundTag;



public class Pouch extends AbstractCustomItem {

	private static final Set<Material> invalid = Set.of(Material.BEDROCK, Material.COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID, Material.BARRIER);

	public static final String KEY_BLOCK = "block";

	@Override
	public String getIdentifier() {
		return "pouch";
	}


	@Override
	public String getTitle() {
		return "§6Pouch";
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.POUCH;
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		builder.createSection(null, true).add("").add("§7Empty");
	}


	@Override
	protected void createData(PersistentDataObject customData) {
		super.createData(customData);

		customData.setObject(KEY_BLOCK, new PersistentDataObject(customData.getAdapterContext()));
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		ItemMeta meta = stack.getItemMeta();
		CustomItemData data = getCustomData(stack);
		if(data == null)
			return true;

		NBTWrapper wrapper = new NBTWrapper(NBTUtils.convertPersistentDataToNBT(data.getData().getObject(KEY_BLOCK).getContainer()));
		if(wrapper.isEmpty()) {
			if(invalid.contains(block.getType()))
				return false;

			if(Blocks.getCustomBlockFromBlock(block) != null)
				return false;

			NBTWrapper tagWrapper = new NBTWrapper(this.store(block));
			meta.setLore(Arrays.asList("", "§dMaterial: " + Utils.toCapitalWords(block.getType()), "§bBlockstates: " + tagWrapper.getNBTTagList("data").size(), "§eTags: " + tagWrapper.getNBTTagList("tile").size()));
			data.getData().setObject(KEY_BLOCK, new PersistentDataObject(NBTUtils.convertNBTToPersistentData(tagWrapper.getTag(), data.getData().getAdapterContext())));
			data.save(meta);
			// remove block after event
			// Utils.runLater(() -> block.setType(Material.AIR));
			player.playSound(block.getLocation(), Sound.ITEM_BUNDLE_INSERT, 1.0F, 1.0F);
		} else {
			Block target = block.getRelative(event.getBlockFace());
			if(target.getType() == Material.AIR) {
				LoreBuilder builder = new LoreBuilder();
				this.buildDescription(builder, null, null);
				meta.setLore(builder.build());
				data.getData().setObject(KEY_BLOCK, new PersistentDataObject(data.getData().getAdapterContext()));
				data.save(meta);
				// place block after event
				Utils.runLater(() -> this.place(target.getLocation(), wrapper.getTag()));
				player.playSound(block.getLocation(), Sound.ITEM_BUNDLE_REMOVE_ONE, 1.0F, 1.0F);
			}
		}
		// run later so that the item won't change during the event causing another event to be fired
		Utils.runLater(() -> stack.setItemMeta(meta));
		return true;
	}


	private CompoundTag store(Block block) {
		NBTWrapper wrapper = new NBTWrapper();
		wrapper.putString("type", block.getType().name());
		BlockState state = block.getState();
		wrapper.putNBT("data", NBTUtils.saveBlockStateToNBT(state));
		if(state instanceof TileState tile)
			wrapper.putNBT("tile", NBTUtils.saveTileEntityToNBT(tile));
		return wrapper.getTag();
	}


	private void place(Location location, CompoundTag nbt) {
		NBTWrapper wrapper = new NBTWrapper(nbt);
		location.getBlock().setType(Material.valueOf(wrapper.getString("type", null)));
		BlockState state = location.getBlock().getState();
		NBTUtils.storeBlockDataFromNBT(wrapper.getNBTTagCompound("data"), state);
		if(state instanceof TileState tile)
			NBTUtils.storeTileEntityFromNBT(wrapper.getNBTTagCompound("tile"), tile);
		state.update();
	}

}
