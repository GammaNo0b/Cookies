
package me.gamma.cookies.object.item;


import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.util.Utils;



public class CustomItem extends AbstractCustomItem {

	private final String identifier;
	private final String name;
	private final Material material;
	private final String texture;
	private String[] description = null;

	public CustomItem(String name, Material material) {
		this(Utils.getIdentifierFromName(name), name, material);
	}


	public CustomItem(String identifier, String name, Material material) {
		this.identifier = identifier;
		this.name = name;
		this.material = material;
		this.texture = null;
	}


	public CustomItem(String name, String texture) {
		this(Utils.getIdentifierFromName(name), name, texture);
	}


	public CustomItem(String identifier, String name, String texture) {
		this.identifier = identifier;
		this.name = name;
		this.material = Material.PLAYER_HEAD;
		this.texture = texture;
	}


	public CustomItem setDescription(String... description) {
		this.description = description;
		return this;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getTitle() {
		return this.name;
	}


	@Override
	public Material getMaterial() {
		return this.material;
	}


	@Override
	protected String getBlockTexture() {
		return this.texture;
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		if(this.description != null) {
			Section section = builder.createSection(null, true);
			for(String line : this.description)
				section.add(line);
		}
	}

}
