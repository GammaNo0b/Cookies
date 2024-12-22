
package me.gamma.cookies.object.block;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.IItemSupplier;



public class CustomBlock extends AbstractCustomBlock {

	private final String identifier;
	private final Material material;
	private final String texture;
	private final List<ItemStack> drops = new ArrayList<>();

	public CustomBlock(String identifier, Material material) {
		this.identifier = identifier;
		this.material = material;
		this.texture = null;
	}


	public CustomBlock(String identifier, String texture) {
		this.identifier = identifier;
		this.material = Material.PLAYER_HEAD;
		this.texture = texture;
	}


	public CustomBlock addDrops(Material material) {
		return this.addDrops(new ItemStack(material));
	}


	public CustomBlock addDrops(ItemStack stack) {
		this.drops.add(stack);
		return this;
	}


	public CustomBlock addDrops(IItemSupplier supplier) {
		return this.addDrops(supplier.get());
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public Material getMaterial() {
		return this.material;
	}

}
