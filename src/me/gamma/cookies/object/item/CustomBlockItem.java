
package me.gamma.cookies.object.item;


import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class CustomBlockItem extends AbstractBlockItem<AbstractCustomBlock> {

	private final String name;
	private String[] description;

	public CustomBlockItem(AbstractCustomBlock block, String name) {
		super(block);
		this.name = name;
	}


	public CustomBlockItem setDescription(String... description) {
		this.description = description;
		return this;
	}


	@Override
	public String getTitle() {
		return this.name;
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		if(description != null) {
			Section section = builder.createSection(null, true);
			for(String line : this.description)
				section.add(line);
		}
	}

}
