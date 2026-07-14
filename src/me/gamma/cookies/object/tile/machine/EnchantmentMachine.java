
package me.gamma.cookies.object.tile.machine;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.block.machine.EnchantmentMachineBlock;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class EnchantmentMachine<D extends EnchantmentMachine.ProcessingData, T extends EnchantmentMachine<D, T, B>, B extends EnchantmentMachineBlock<B, T>> extends AbstractItemProcessingMachine<T, B> implements FluidConsumer {

	public static final int EXPERIENCE_CAPACITY = 8000;

	public static final int INPUT_ITEM_SLOT = 20;
	public static final int INPUT_BOOK_SLOT = 21;
	public static final int OUTPUT_ITEM_SLOT = 23;
	public static final int OUTPUT_BOOK_SLOT = 24;
	public static final int EXPERIENCE_SLOT = 18;

	private static final String KEY_EXPERIENCE = "experience";
	private static final String KEY_PROCESSING = "processing";

	private byte fluidInputAccessFlags = 0x3f;

	protected int experience = 0;
	protected final D processing = this.createProcessingData();

	public EnchantmentMachine(B customBlock, Block block) {
		super(customBlock, block);
	}


	protected abstract D createProcessingData();


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidConsumer.super.load(chunk, data);

		this.experience = data.getInteger(KEY_EXPERIENCE, 0);
		this.processing.load(null, data.getObject(KEY_PROCESSING, new PersistentDataObject(data.getAdapterContext())));

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidConsumer.super.save(chunk, data);

		data.setInteger(KEY_EXPERIENCE, this.experience);
		PersistentDataObject object = new PersistentDataObject(data.getAdapterContext());
		this.processing.save(null, object);
		data.setObject(KEY_PROCESSING, object);

		return true;
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { INPUT_ITEM_SLOT, INPUT_BOOK_SLOT };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { OUTPUT_ITEM_SLOT, OUTPUT_BOOK_SLOT };
	}


	@Override
	public List<FluidProvider> getFluidInputs() {
		return List.of(FluidProvider.fromHolder(FluidType.EXPERIENCE, Holder.create(() -> this.experience, i -> { this.experience = i; }), EXPERIENCE_CAPACITY));
	}


	@Override
	public byte getFluidInputAccessFlags() {
		return this.fluidInputAccessFlags;
	}


	@Override
	public void setFluidInputAccessFlags(byte flags) {
		this.fluidInputAccessFlags = flags;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);

		this.updateExperience();
	}


	private void updateExperience() {
		this.getInventory().setItem(EXPERIENCE_SLOT, new ItemBuilder(FluidType.EXPERIENCE.createIcon()).addLore("§e" + this.experience + "mb").build());
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);

		configs.add(this.createFluidInputBlockFaceConfig());
	}


	@Override
	public void destroy() {
		super.destroy();

		this.processing.drop(this.block);
	}


	@Override
	public void tick() {
		this.updateExperience();

		this.tryPushItems();
		super.tick();
		this.tryPullItems();
		this.tryPullFluid();
	}


	/**
	 * Creates the next enchantment process. Returns the experience the process consumes.
	 * 
	 * @param item the first item to be processed
	 * @param book the second item to be processed
	 * @return the experience
	 */
	protected abstract int createEnchantmentProcess(ItemStack item, ItemStack book);


	/**
	 * Processes the given processing data.
	 * 
	 * @param data the data
	 */
	protected void process(D data) {}


	@Override
	protected int createNextProcess() {
		Inventory gui = this.getInventory();

		ItemStack item = gui.getItem(INPUT_ITEM_SLOT);
		if(ItemUtils.isEmpty(item))
			return 0;

		ItemStack book = gui.getItem(INPUT_BOOK_SLOT);

		int experience = this.createEnchantmentProcess(item, book);
		if(experience <= 0)
			return 0;

		if(this.experience < experience)
			return 0;

		this.experience -= experience;

		this.processing.item = ItemUtils.removeItem(item, 1);
		this.processing.book = ItemUtils.removeItem(book, 1);

		return experience;
	}


	@Override
	protected boolean finishProcess() {
		if(!this.processing.processed) {
			this.processing.processed = true;
			this.process(this.processing);
		}

		this.processing.item = this.storeOutput(this.processing.item);
		this.processing.book = this.storeOutput(this.processing.book);

		if(!ItemUtils.isEmpty(this.processing.item) || !ItemUtils.isEmpty(this.processing.book))
			return false;

		this.processing.empty();

		return true;
	}

	protected static record EnchantmentResult(ItemStack item, ItemStack book, int experience, int duration) {}

	protected static class ProcessingData implements DataStorage {

		private static final String KEY_PROCESSED = "processed";
		private static final String KEY_PROCESSING_ITEM = "processingitem";
		private static final String KEY_PROCESSING_BOOK = "processingbook";

		protected boolean processed = false;

		protected ItemStack item;
		protected ItemStack book;

		@Override
		public boolean load(Chunk chunk, PersistentDataObject data) {
			this.processed = data.getBoolean(KEY_PROCESSED, false);
			this.item = PersistentDataUtils.getItemStack(data, KEY_PROCESSING_ITEM);
			this.book = PersistentDataUtils.getItemStack(data, KEY_PROCESSING_BOOK);

			return true;
		}


		@Override
		public boolean save(Chunk chunk, PersistentDataObject data) {
			data.setBoolean(KEY_PROCESSED, this.processed);
			PersistentDataUtils.setItemStack(data, KEY_PROCESSING_ITEM, this.item);
			PersistentDataUtils.setItemStack(data, KEY_PROCESSING_BOOK, this.book);

			return true;
		}


		protected void empty() {
			this.processed = false;
			this.item = null;
			this.book = null;
		}


		protected void drop(Block block) {
			if(this.item != null)
				ItemUtils.dropItem(this.item, block);

			if(this.book != null)
				ItemUtils.dropItem(this.book, block);
		}

	}

}
