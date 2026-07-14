
package me.gamma.cookies.object.tile.machine;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.machine.BioPressBlock;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class BioPress extends AbstractProcessingMachine<BioPress, BioPressBlock> implements ItemConsumer, FluidSupplier {

	private static final String KEY_BIOMASS = "biomass";
	private static final String KEY_INPUT = "input";

	private final int capacity;

	private byte itemInputAccessFlags = 0x3F;
	private byte fluidOutputAccessFlags = 0x3F;

	private int biomass;

	public BioPress(BioPressBlock customBlock, Block block) {
		super(customBlock, block);

		this.capacity = customBlock.getBiomassCapacity();
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		ItemConsumer.super.load(chunk, data);
		FluidSupplier.super.load(chunk, data);

		this.biomass = data.getInteger(KEY_BIOMASS, 0);

		Inventory inventory = this.getInventory();
		int[] inputSlots = this.getInputSlots();
		for(int i = 0; i < inputSlots.length; i++)
			inventory.setItem(inputSlots[i], PersistentDataUtils.getItemStack(data, KEY_INPUT + i));

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		ItemConsumer.super.save(chunk, data);
		FluidSupplier.super.save(chunk, data);

		data.setInteger(KEY_BIOMASS, this.biomass);

		Inventory inventory = this.getInventory();
		int[] inputSlots = this.getInputSlots();
		for(int i = 0; i < inputSlots.length; i++)
			PersistentDataUtils.setItemStack(data, KEY_INPUT + i, inventory.getItem(inputSlots[i]));

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);

		this.updateTank();
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createItemInputBlockFaceConfig());
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	public void destroy() {
		super.destroy();

		for(Provider<ItemStack> provider : this.getItemInputs())
			ItemUtils.dropItem(ItemProvider.get(provider), this.block);
	}


	@Override
	public void tick() {
		super.tick();
		this.tryPushFluid();
		this.updateTank();
	}


	@Override
	protected int createNextProcess() {
		for(Provider<ItemStack> input : this.getItemInputs()) {
			ItemStack stack = ItemProvider.getStack(input);
			if(ItemUtils.isEmpty(stack))
				continue;

			if(ItemUtils.isCustomItem(stack))
				continue;

			if(!stack.getType().isCompostable())
				continue;

			float chance = stack.getType().getCompostChance();
			if(chance <= 0.0F)
				continue;

			int mb = Math.round(chance * 200.0F);
			if(this.biomass + mb > this.capacity)
				return 0;

			input.get(1);

			return mb;
		}
		return 0;
	}


	@Override
	protected void proceed() {
		this.biomass++;
	}


	@Override
	protected boolean finishProcess() {
		return true;
	}


	private int[] getInputSlots() {
		return MachineConstants.inputSlots[this.customBlock.getTier().ordinal()];
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		final Material[] stages = { Material.GREEN_STAINED_GLASS_PANE, Material.LIME_TERRACOTTA, Material.GREEN_CONCRETE_POWDER, Material.GREEN_WOOL, Material.GREEN_CONCRETE, Material.GREEN_TERRACOTTA };
		final int len = stages.length;
		for(int i = 0; i < len; i++)
			if(progress * len < i + 1)
				return stages[i];
		return stages[stages.length - 1];
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
	public byte getFluidOutputAccessFlags() {
		return this.fluidOutputAccessFlags;
	}


	@Override
	public void setFluidOutputAccessFlags(byte flags) {
		this.fluidOutputAccessFlags = flags;
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs() {
		return ItemProvider.fromInventory(this.getInventory(), this.getInputSlots());
	}


	@Override
	public List<FluidProvider> getFluidOutputs() {
		return List.of(FluidProvider.fromHolder(FluidType.BIO_MASS, Holder.create(() -> this.biomass, i -> { BioPress.this.biomass = i; }), this.capacity));
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		super.onMainInventoryInteract(player, gui, event);

		return !ArrayUtils.contains(this.getInputSlots(), event.getSlot());
	}


	private void updateTank() {
		Inventory gui = this.getInventory();
		int fill = (int) Math.round(this.biomass * 3.0D / this.capacity);
		int i = 0;
		ItemStack stack = FluidType.BIO_MASS.buildIcon().addLore("§f" + this.biomass + " §7mb").build();
		for(; i < fill; i++) {
			gui.setItem((3 - i) * 9 + 6, stack);
			gui.setItem((3 - i) * 9 + 7, stack);
		}
		stack.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < 3; i++) {
			gui.setItem((3 - i) * 9 + 6, stack);
			gui.setItem((3 - i) * 9 + 7, stack);
		}
	}


	@Override
	public BioPress castTileEntity() {
		return this;
	}

}
