
package me.gamma.cookies.object.tile.machine;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.machine.AbstractItemProcessingMachineBlock;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractItemProcessingMachine<T extends AbstractItemProcessingMachine<T, B>, B extends AbstractItemProcessingMachineBlock<B, T>> extends AbstractItemGenerationMachine<T, B> implements ItemConsumer {

	public static final String KEY_LOCK_INPUT = "lockinput";
	public static final String KEY_USE_OUTPUT = "useoutput";

	private static final ItemStack INPUT_UNLOCKED_ICON = new ItemBuilder(Material.TRIPWIRE_HOOK).setName("§cInput Unlocked").addLore("  §7Uses up all items in the input slots.").build();
	private static final ItemStack INPUT_LOCKED_ICON = new ItemBuilder(Material.TRIPWIRE_HOOK).setName("§aInput Locked").addLore("  §7Keeps one item in each slot.").build();
	private static final ItemStack ONLY_INPUT_ICON = new ItemBuilder(Material.HOPPER).setName("§cOnly take from input").addLore("  §7Processing items are only taken from input").build();
	private static final ItemStack USE_OUTPUT_ICON = new ItemBuilder(Material.HOPPER).setName("§aReuse items from output").addLore("  §7Processing items can be taken both from input and output.").build();

	private byte itemInputAccessFlags = 0x3F;
	private boolean lockInput = false;
	private boolean useOutput = false;

	public AbstractItemProcessingMachine(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		ItemConsumer.super.load(chunk, data);

		if(this.customBlock.getInputModeSlot() >= 0) {
			this.lockInput = data.getBoolean(KEY_LOCK_INPUT, false);
			this.updateLockInput();
		}

		if(this.customBlock.getOutputModeSlot() >= 0) {
			this.useOutput = data.getBoolean(KEY_USE_OUTPUT, false);
			this.updateUseOutput();
		}

		Inventory inventory = this.getInventory();
		int[] inputSlots = this.getInputSlots();
		for(int i = 0; i < inputSlots.length; i++)
			inventory.setItem(inputSlots[i], PersistentDataUtils.getItemStack(data, "iteminput" + i));

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		ItemConsumer.super.save(chunk, data);

		if(this.customBlock.getInputModeSlot() >= 0)
			data.setBoolean(KEY_LOCK_INPUT, this.lockInput);

		if(this.customBlock.getOutputModeSlot() >= 0)
			data.setBoolean(KEY_USE_OUTPUT, this.useOutput);

		Inventory inventory = this.getInventory();
		int[] inputSlots = this.getInputSlots();
		for(int i = 0; i < inputSlots.length; i++)
			PersistentDataUtils.setItemStack(data, "iteminput" + i, inventory.getItem(inputSlots[i]));

		return true;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, gui, event))
			return false;

		int slot = event.getSlot();
		if(slot == this.customBlock.getInputModeSlot()) {
			this.lockInput = !this.lockInput;
			this.updateLockInput();
			return true;
		} else if(slot == this.customBlock.getOutputModeSlot()) {
			this.useOutput = !this.useOutput;
			this.updateUseOutput();
			return true;
		}

		return !ArrayUtils.contains(this.getInputSlots(), slot);
	}


	@Override
	public void listBlockFaceProperties(List<BlockFaceConfig.Config> configs) {
		configs.add(this.createItemInputBlockFaceConfig());
		super.listBlockFaceProperties(configs);
	}


	@Override
	public byte getItemInputAccessFlags() {
		return this.itemInputAccessFlags;
	}


	@Override
	public void setItemInputAccessFlags(byte flags) {
		this.itemInputAccessFlags = flags;
	}


	@Override
	public int[] getInputSlots() {
		return MachineConstants.getInputSlots(this.customBlock.getTier());
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs() {
		return ItemProvider.fromInventory(this.getInventory(), this.getInputSlots());
	}


	@Override
	public void destroy() {
		super.destroy();

		for(Provider<ItemStack> provider : this.getItemInputs())
			ItemUtils.dropItem(ItemProvider.get(provider), this.block);
	}


	@Override
	public void tick() {
		this.tryPullItems();
		super.tick();
	}


	private void updateLockInput() {
		this.getInventory().setItem(this.customBlock.getInputModeSlot(), this.lockInput ? INPUT_LOCKED_ICON : INPUT_UNLOCKED_ICON);
	}


	private void updateUseOutput() {
		this.getInventory().setItem(this.customBlock.getOutputModeSlot(), this.useOutput ? USE_OUTPUT_ICON : ONLY_INPUT_ICON);
	}


	/**
	 * Returns a map of useable items.
	 * 
	 * @param block the block
	 * @return the useable items
	 */
	protected Map<ItemStack, Integer> getUseableItems() {
		Map<ItemStack, Integer> items = new HashMap<>();

		for(Provider<ItemStack> provider : this.getItemInputs()) {
			ItemStack stack = ItemProvider.getStack(provider);
			if(ItemUtils.isEmpty(stack))
				continue;

			ItemStack type = stack.clone();
			type.setAmount(1);
			items.merge(type, stack.getAmount() - (this.lockInput ? 1 : 0), Integer::sum);
		}

		if(this.useOutput) {
			for(Provider<ItemStack> provider : this.getItemOutputs()) {
				ItemStack stack = ItemProvider.getStack(provider);
				if(ItemUtils.isEmpty(stack))
					continue;

				ItemStack type = stack.clone();
				type.setAmount(1);
				items.merge(type, stack.getAmount(), Integer::sum);
			}
		}

		return items;
	}


	/**
	 * Tries to consume all the items from the given list from the input and maybe output slots of the given block. Returns true if all items could be
	 * consumed and false otherwise. The list of items will contain the items not beeing consumed.
	 * 
	 * @param items the list of items to consume
	 * @return whether all items could be consumed
	 */
	protected boolean consumeInputs(List<ItemStack> items) {
		consumeInputs(items, this.getItemInputs(), this.lockInput ? 1 : 0);
		if(this.useOutput && !items.isEmpty())
			consumeInputs(items, this.getItemOutputs(), 0);

		return items.isEmpty();
	}


	private static void consumeInputs(List<ItemStack> items, List<Provider<ItemStack>> inputs, int keep) {
		Iterator<ItemStack> iterator = items.iterator();
		while(iterator.hasNext()) {
			ItemStack stack = iterator.next();
			if(ItemUtils.isEmpty(stack)) {
				iterator.remove();
				continue;
			}

			int amount = stack.getAmount();

			for(Provider<ItemStack> provider : inputs) {
				if(provider.isEmpty())
					continue;

				if(!provider.match(stack))
					continue;

				amount -= provider.get(amount);
				if(amount <= 0)
					break;
			}

			stack.setAmount(amount);
			if(ItemUtils.isEmpty(stack))
				iterator.remove();
		}
	}

}
