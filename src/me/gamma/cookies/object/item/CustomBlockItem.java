
package me.gamma.cookies.object.item;


import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.AbstractCustomBlock;



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
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		if(description != null) {
			Section section = builder.createSection(null, true);
			for(String line : this.description)
				section.add(line);
		}
	}

}
