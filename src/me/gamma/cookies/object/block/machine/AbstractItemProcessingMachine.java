
package me.gamma.cookies.object.block.machine;


import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public abstract class AbstractItemProcessingMachine extends AbstractItemGenerationMachine implements ItemConsumer {

	private static final BooleanProperty LOCK_INPUT = new BooleanProperty("lockinput");
	private static final BooleanProperty USE_OUTPUT = new BooleanProperty("useoutput");

	private static final ItemStack INPUT_UNLOCKED_ICON = new ItemBuilder(Material.TRIPWIRE_HOOK).setName("§cInput Unlocked").addLore("  §7Uses up all items in the input slots.").build();
	private static final ItemStack INPUT_LOCKED_ICON = new ItemBuilder(Material.TRIPWIRE_HOOK).setName("§aInput Locked").addLore("  §7Keeps one item in each slot.").build();
	private static final ItemStack ONLY_INPUT_ICON = new ItemBuilder(Material.HOPPER).setName("§cOnly take from input").addLore("  §7Processing items are only taken from input").build();
	private static final ItemStack USE_OUTPUT_ICON = new ItemBuilder(Material.HOPPER).setName("§aReuse items from output").addLore("  §7Processing items can be taken both from input and output.").build();

	public AbstractItemProcessingMachine(MachineTier tier) {
		super(tier);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);

		ItemStackProperty[] inputProperties = this.getItemInputProperties();
		int[] inputSlots = this.getInputSlots();
		for(int i = 0; i < inputProperties.length; i++)
			inventory.setItem(inputSlots[i], inputProperties[i].fetch(block));

		if(this.getInputModeSlot() >= 0)
			inventory.setItem(this.getInputModeSlot(), LOCK_INPUT.fetch(block) ? INPUT_LOCKED_ICON : INPUT_UNLOCKED_ICON);
		if(this.getOutputModeSlot() >= 0)
			inventory.setItem(this.getOutputModeSlot(), USE_OUTPUT.fetch(block) ? USE_OUTPUT_ICON : ONLY_INPUT_ICON);
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		super.saveInventory(block, inventory);

		ItemStackProperty[] inputProperties = this.getItemInputProperties();
		int[] inputSlots = this.getInputSlots();
		for(int i = 0; i < inputProperties.length; i++)
			inputProperties[i].store(block, inventory.getItem(inputSlots[i]));

		block.update();
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(LOCK_INPUT).add(USE_OUTPUT).add(ITEM_INPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public void listBlockFaceProperties(List<BlockFaceConfig.Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(configs.size() - 1, this.createItemInputBlockFaceConfig());
	}


	/**
	 * Returns the slot to store the input mode button.
	 * 
	 * @return the slot
	 */
	protected int getInputModeSlot() {
		return MachineConstants.INPUT_MODE_SLOT;
	}


	/**
	 * Returns the slot to store the output mode button.
	 * 
	 * @return the slot
	 */
	protected int getOutputModeSlot() {
		return MachineConstants.OUTPUT_MODE_SLOT;
	}


	/**
	 * Returns the item input slots.
	 * 
	 * @return the input slots
	 */
	protected int[] getInputSlots() {
		return MachineConstants.getInputSlots(this.tier);
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs(TileState block) {
		Inventory inventory = this.getGui(block);
		return ItemProvider.fromInventory(inventory, this.getInputSlots());
	}


	@Override
	public void tick(TileState block) {
		this.tryPullItems(block);
		super.tick(block);
	}


	/**
	 * Returns an array of useable items.
	 * 
	 * @param block the block
	 * @return the useable items
	 */
	protected ItemStack[] getUseableItems(TileState block) {
		boolean lockInput = LOCK_INPUT.fetch(block);
		boolean useOutput = USE_OUTPUT.fetch(block);

		List<Provider<ItemStack>> inputs = this.getItemInputs(block);
		List<Provider<ItemStack>> outputs = null;
		int length = inputs.size();
		if(useOutput) {
			outputs = this.getItemOutputs(block);
			length += outputs.size();
		}

		ItemStack[] items = new ItemStack[length];
		int i = 0;
		if(lockInput) {
			for(Provider<ItemStack> provider : inputs) {
				ItemStack stack = ItemProvider.getStack(provider);
				ItemUtils.increaseItem(stack, -1);
				items[i++] = stack;
			}
		} else {
			for(Provider<ItemStack> provider : inputs)
				items[i++] = ItemProvider.getStack(provider);
		}

		if(useOutput)
			for(Provider<ItemStack> provider : outputs)
				items[i++] = ItemProvider.getStack(provider);

		return items;
	}


	/**
	 * Tries to consume all the items from the given list from the input and maybe output slots of the given block. Returns true if all items could be
	 * consumed and false otherwise. The list of items will contain the items not beeing consumed.
	 * 
	 * @param items the list of items to consume
	 * @return whether all items could be consumed
	 */
	protected boolean consumeInputs(TileState block, List<ItemStack> items) {
		consumeInputs(items, this.getItemInputs(block), LOCK_INPUT.fetch(block) ? 1 : 0);
		if(USE_OUTPUT.fetch(block) && !items.isEmpty())
			consumeInputs(items, this.getItemOutputs(block), 0);

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

			Iterator<Provider<ItemStack>> proviterator = inputs.iterator();
			while(proviterator.hasNext()) {
				Provider<ItemStack> provider = proviterator.next();
				if(provider.isEmpty()) {
					proviterator.remove();
					continue;
				}

				if(!provider.match(stack))
					continue;

				ItemUtils.increaseItem(stack, -provider.get(Math.min(provider.amount() - keep, stack.getAmount())));
				if(ItemUtils.isEmpty(stack))
					break;
			}

			if(ItemUtils.isEmpty(stack))
				iterator.remove();
		}
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		for(Provider<ItemStack> provider : this.getItemInputs(block))
			drops.add(ItemProvider.getStack(provider));

		for(Provider<ItemStack> provider : this.getItemOutputs(block))
			drops.add(ItemProvider.getStack(provider));

		return drops;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, block, gui, event))
			return false;

		int slot = event.getSlot();
		if(slot == this.getInputModeSlot()) {
			boolean mode = LOCK_INPUT.toggle(block);
			block.update();
			gui.setItem(this.getInputModeSlot(), mode ? INPUT_LOCKED_ICON : INPUT_UNLOCKED_ICON);
			return true;
		} else if(slot == this.getOutputModeSlot()) {
			boolean mode = USE_OUTPUT.toggle(block);
			block.update();
			gui.setItem(this.getOutputModeSlot(), mode ? USE_OUTPUT_ICON : ONLY_INPUT_ICON);
			return true;
		}

		return !ArrayUtils.contains(this.getInputSlots(), slot);
	}


	/**
	 * Returns an array of item stack properties to store the inputs of a machine.
	 * 
	 * @return the array
	 */
	private ItemStackProperty[] getItemInputProperties() {
		int[] inputSlots = this.getInputSlots();
		return ArrayUtils.generate(inputSlots.length, i -> new ItemStackProperty("in" + inputSlots[i]), ItemStackProperty[]::new);
	}

}
