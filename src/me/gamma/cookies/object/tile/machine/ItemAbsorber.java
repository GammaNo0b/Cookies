
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.machine.ItemAbsorberBlock;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.gui.ItemInventoryHolder;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.core.MinecraftItemHelper;



public class ItemAbsorber extends AbstractGuiMachine<ItemAbsorber, ItemAbsorberBlock> implements ItemSupplier, ItemInventoryHolder {

	private static final String KEY_ITEM = "item";

	private static final int[] slots = new int[] { 13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34 };

	private final int range;

	private byte itemOutputAccessFlags = 0x3F;

	public ItemAbsorber(ItemAbsorberBlock customBlock, Block block) {
		super(customBlock, block);

		this.range = customBlock.getRange();
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		ItemSupplier.super.load(chunk, data);

		Inventory gui = this.getInventory();
		for(int slot : slots)
			gui.setItem(slot, PersistentDataUtils.getItemStack(data, KEY_ITEM + slot));

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		ItemSupplier.super.save(chunk, data);

		Inventory gui = this.getInventory();
		for(int slot : slots)
			PersistentDataUtils.setItemStack(data, KEY_ITEM + slot, gui.getItem(slot));

		return true;
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);

		configs.add(this.createItemOutputBlockFaceConfig());
	}


	@Override
	public void destroy() {
		super.destroy();

		for(ItemStack stack : this.getContents())
			ItemUtils.dropItem(stack, this.block);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		for(int i = 0; i < slots.length; i++)
			if(slots[i] == slot)
				return false;

		return super.onMainInventoryInteract(player, gui, event);
	}


	public List<ItemStack> getContents() {
		List<ItemStack> contents = new ArrayList<>();

		Inventory gui = this.getInventory();
		for(int slot : slots)
			contents.add(gui.getItem(slot));

		contents.removeIf(ItemUtils::isEmpty);

		return contents;
	}


	@Override
	public Inventory getInventory() {
		return super.getInventory();
	}


	@Override
	public int[] getInputSlots() {
		return new int[0];
	}


	@Override
	public int[] getOutputSlots() {
		return slots;
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs() {
		return ItemProvider.fromInventory(this.getInventory(), slots);
	}


	@Override
	public byte getItemOutputAccessFlags() {
		return this.itemOutputAccessFlags;
	}


	@Override
	public void setItemOutputAccessFlags(byte flags) {
		this.itemOutputAccessFlags = flags;
	}


	@Override
	protected boolean run() {
		this.tryPushItems();
		Location front = block.getLocation().add(0.5D, 0.5D, 0.5D).add(((Rotatable) block.getBlockData()).getRotation().getDirection().multiply(0.25D));
		for(Entity entity : block.getLocation().getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.5D, 0.5D), this.range, this.range, this.range)) {
			if(entity instanceof Item) {
				Item item = (Item) entity;
				ItemStack stack = item.getItemStack();
				if(ItemUtils.isEmpty(stack))
					continue;

				final Color c = MinecraftItemHelper.getItemRarityColor(MinecraftItemHelper.getItemRarity(stack));
				ParticleManager.drawAnimatedLine(item.getLocation(), front, 1, 10, pos -> pos.getWorld().spawnParticle(Particle.DUST, pos, 1, 0.1F, 0.1F, 0.1F, new Particle.DustOptions(c, 1.0F)));
				int amount = stack.getAmount();
				stack = ItemConsumer.addStack(stack, amount, this.getItemOutputs());
				if(ItemUtils.isEmpty(stack))
					item.remove();

				if(amount - stack.getAmount() > 0)
					return true;
			}
		}
		return false;
	}


	@Override
	public ItemAbsorber castTileEntity() {
		return this;
	}

}
