
package me.gamma.cookies.object.block.network.energy;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.EnderLinkedTileEntity;
import me.gamma.cookies.object.tile.network.energy.Tesseract;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class TesseractBlock extends EnderLinkedBlock<Holder<Integer>> {

	private static final String KEY_ENERGY = "energy";

	@Override
	protected Holder<Integer> newResource() {
		return new Holder.BasicHolder<>(0);
	}


	@Override
	protected boolean loadResource(Holder<Integer> resource, PersistentDataObject data) {
		resource.set(data.getInteger(KEY_ENERGY, 0));
		return true;
	}


	@Override
	protected boolean saveResource(Holder<Integer> resource, PersistentDataObject data) {
		data.setInteger(KEY_ENERGY, resource.get());
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
	protected void displayResources(Player player, Holder<Integer> resource) {
		player.sendMessage("§7Stored Energy: §f" + resource.get() + " §cCC");
	}


	@Override
	public EnderLinkedBlock<Holder<Integer>> castCustomBlock() {
		return this;
	}


	@Override
	public EnderLinkedTileEntity<Holder<Integer>> createNewTileEntity(Block block) {
		return new Tesseract(this, block);
	}

}
