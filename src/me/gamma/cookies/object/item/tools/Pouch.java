
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
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.NBTProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.NBTUtils;
import me.gamma.cookies.util.Utils;
import net.minecraft.nbt.NBTTagCompound;



public class Pouch extends AbstractCustomItem {

	private static final Set<Material> invalid = Set.of(Material.BEDROCK, Material.COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID, Material.BARRIER);

	public static final NBTProperty BLOCK = new NBTProperty("block");

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
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("").add("§7Empty");
	}


	@Override
	protected PropertyBuilder buildItemProperties(PropertyBuilder builder) {
		return super.buildItemProperties(builder).add(BLOCK);
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		ItemMeta meta = stack.getItemMeta();
		NBTTagCompound nbt = BLOCK.fetch(meta);
		if(nbt.g()) {
			if(invalid.contains(block.getType()))
				return false;

			if(block.getState() instanceof TileState tile && Blocks.getCustomBlockFromBlock(tile) != null)
				return false;

			NBTTagCompound tag = this.store(block);
			meta.setLore(Arrays.asList("", "§dMaterial: " + Utils.toCapitalWords(block.getType()), "§bBlockstates: " + tag.p("data").e(), "§eTags: " + tag.p("tile").e()));
			BLOCK.store(meta, tag);
			// remove block after event
			//Utils.runLater(() -> block.setType(Material.AIR));
			player.playSound(block.getLocation(), Sound.ITEM_BUNDLE_INSERT, 1.0F, 1.0F);
		} else {
			Block target = block.getRelative(event.getBlockFace());
			if(target.getType() == Material.AIR) {
				LoreBuilder builder = new LoreBuilder();
				this.getDescription(builder, null);
				meta.setLore(builder.build());
				BLOCK.storeEmpty(meta);
				// place block after event
				Utils.runLater(() -> this.place(target.getLocation(), nbt));
				player.playSound(block.getLocation(), Sound.ITEM_BUNDLE_REMOVE_ONE, 1.0F, 1.0F);
			}
		}
		// run later so that the item won't change during the event causing another event to be fired
		Utils.runLater(() -> stack.setItemMeta(meta));
		return true;
	}


	private NBTTagCompound store(Block block) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.a("type", block.getType().name());
		BlockState state = block.getState();
		nbt.a("data", NBTUtils.saveBlockStateToNBT(state));
		if(state instanceof TileState tile)
			nbt.a("tile", NBTUtils.saveTileEntityToNBT(tile));
		return nbt;
	}


	private void place(Location location, NBTTagCompound nbt) {
		location.getBlock().setType(Material.valueOf(nbt.l("type")));
		BlockState state = location.getBlock().getState();
		NBTUtils.storeBlockDataFromNBT(nbt.p("data"), state);
		if(state instanceof TileState tile)
			NBTUtils.storeTileEntityFromNBT(nbt.p("tile"), tile);
		state.update();
	}

}
