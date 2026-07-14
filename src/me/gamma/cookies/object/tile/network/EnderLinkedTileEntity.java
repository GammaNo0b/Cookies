
package me.gamma.cookies.object.tile.network;


import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class EnderLinkedTileEntity<T> extends AbstractCustomTileEntity<EnderLinkedTileEntity<T>, EnderLinkedBlock<T>> implements Ownable, ContainerTile {

	private static final Material[] colors = { Material.WHITE_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE };
	public static final char[] colorcodes = "f7806c6ea23b95dd".toCharArray();

	public static final String KEY_OWNER = "owner";
	public static final String KEY_COLOR = "color";

	private UUID owner = null;
	private int color = 0;

	public EnderLinkedTileEntity(EnderLinkedBlock<T> customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.owner = PersistentDataUtils.getUUID(data, KEY_OWNER);
		this.color = data.getInteger(KEY_COLOR, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		if(this.owner != null)
			PersistentDataUtils.setUUID(data, KEY_OWNER, this.owner);

		data.setInteger(KEY_COLOR, this.color);

		return true;
	}


	@Override
	public UUID getOwner() {
		return this.owner;
	}


	@Override
	public void setOwner(UUID uuid) {
		this.owner = uuid;
	}


	public int getColor() {
		return this.color;
	}


	public T getResource() {
		return this.customBlock.getResource(this.owner, this.color);
	}


	@Override
	public BlockInventoryProvider getInventoryProvider() {
		return this.customBlock;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		int i = event.getSlot() - 3;
		if(0 <= i && i < 3) {
			int shift = i << 2;
			int mask = 0xF << shift;
			int c = (this.color & mask) >> shift;
			int d = 1;
			if(event.getClick().isShiftClick())
				d = 4;
			if(event.getClick().isRightClick())
				d = -d;
			c += d;
			this.color &= ~mask;
			this.color |= (c & 0xF) << shift;
			this.updateColor(gui, this.color);
		}

		return true;
	}


	public void updateColor(Inventory inventory) {
		this.updateColor(inventory, this.color);
	}


	private void updateColor(Inventory inventory, int color) {
		for(int i = 0; i < 3; i++) {
			int c = (color >> (i << 2)) & 0xF;
			inventory.setItem(i + 3, new ItemBuilder(colors[c]).setName(String.format("§%c%d", colorcodes[c], c)).build());
		}
	}


	@Override
	public EnderLinkedTileEntity<T> castTileEntity() {
		return this;
	}

}
