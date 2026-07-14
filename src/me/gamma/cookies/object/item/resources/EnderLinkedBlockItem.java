
package me.gamma.cookies.object.item.resources;


import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.tile.network.EnderLinkedTileEntity;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class EnderLinkedBlockItem<T, E extends EnderLinkedBlock<T>> extends AbstractBlockItem<E> {

	private final String title;

	public EnderLinkedBlockItem(E block, String title) {
		super(block);
		this.title = title;
	}


	@Override
	public String getTitle() {
		return this.title;
	}


	@Override
	protected void createData(PersistentDataObject customData) {
		super.createData(customData);

		customData.setInteger(EnderLinkedTileEntity.KEY_COLOR, 0);
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		int color = data.getInteger(EnderLinkedTileEntity.KEY_COLOR, 0);
		Section section = builder.createSection("§7Ender Color:", false);
		for(int i = 0; i < 3; i++) {
			int c = (color >> (i << 2)) & 0xF;
			section.add(String.format("  §%c%d", EnderLinkedTileEntity.colorcodes[c], c));
		}
		section.build();
		builder.createSection("§7Change the color using an eye of ender.", true);
	}

}
