
package me.gamma.cookies.object.item.resources;


import static me.gamma.cookies.object.block.network.EnderLinkedBlock.COLOR;
import static me.gamma.cookies.object.block.network.EnderLinkedBlock.colorcodes;

import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.item.AbstractBlockItem;



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
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		int color = COLOR.fetch(holder);
		Section section = builder.createSection("§7Ender Color:", false);
		for(int i = 0; i < 3; i++) {
			int c = (color >> (i << 2)) & 0xF;
			section.add(String.format("  §%c%d", colorcodes[c], c));
		}
		section.build();
		builder.createSection("§7Change the color using an eye of ender.", true);
	}

}
