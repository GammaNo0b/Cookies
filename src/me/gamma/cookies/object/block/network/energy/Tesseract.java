
package me.gamma.cookies.object.block.network.energy;


import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.energy.EnergyStorage;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.EnergyProperty;
import me.gamma.cookies.util.collection.Holder;



public class Tesseract extends EnderLinkedBlock<Holder<Integer>> implements EnergyStorage {

	private static final int CAPACITY = 100000;

	private static final EnergyProperty ENERGY = new EnergyProperty("energy", 0, CAPACITY);

	@Override
	protected Holder<Integer> newResource() {
		return new Holder<>(0);
	}


	@Override
	protected void loadResource(Holder<Integer> resource, PersistentDataContainer container) {
		resource.value = ENERGY.fetch(container);
	}


	@Override
	protected boolean saveResource(Holder<Integer> resource, PersistentDataContainer container) {
		if(resource.value == 0)
			return false;

		ENERGY.store(container, resource.value);
		return true;
	}


	@Override
	public String getIdentifier() {
		return "tesseract";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.TESSERACT;
	}


	@Override
	public byte getEnergyInputAccessFlags(PersistentDataHolder holder) {
		return 0x3F;
	}


	@Override
	public byte getEnergyOutputAccessFlags(PersistentDataHolder holder) {
		return 0x3F;
	}


	@Override
	public EnergyProvider getEnergyProvider(PersistentDataHolder holder) {
		return EnergyProvider.fromHolder(this.getResource(holder), CAPACITY);
	}


	@Override
	protected void displayResources(Player player, TileState block, Holder<Integer> resource) {
		player.sendMessage("§7Stored Energy: §f" + resource.value + " §cCC");
	}

}
