
package me.gamma.cookies.object.block.machine;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class BioPress extends AbstractProcessingMachine implements ItemConsumer, FluidSupplier {

	public static final IntegerProperty PROCESSING = new IntegerProperty("processing");

	private IntegerProperty bioMass;
	private int capacity;

	public BioPress(MachineTier tier) {
		super(tier);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("capacity", 0);
		this.bioMass = new IntegerProperty("bioMass", 0, this.capacity);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);

		ItemStackProperty[] inputProperties = this.getItemInputProperties();
		int[] inputSlots = this.getInputSlots();
		for(int i = 0; i < inputProperties.length; i++)
			inventory.setItem(inputSlots[i], inputProperties[i].fetch(block));

		this.updateTank(block);
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
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createItemInputBlockFaceConfig());
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(PROCESSING);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(this.bioMass).add(ITEM_INPUT_ACCESS_FLAGS, (byte) 0x3F).add(FLUID_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		final Inventory inventory = this.getGui(block);
		int[] inputSlots = this.getInputSlots();
		for(int i = 0; i < inputSlots.length; i++)
			drops.add(inventory.getItem(inputSlots[i]));

		return drops;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		super.onMainInventoryInteract(player, block, gui, event);

		return !ArrayUtils.contains(this.getInputSlots(), event.getSlot());
	}


	@Override
	public void tick(TileState block) {
		super.tick(block);
		this.tryPushFluid(block);
		this.updateTank(block);
	}


	@Override
	protected int createNextProcess(TileState block) {
		for(Provider<ItemStack> input : this.getItemInputs(block)) {
			ItemStack stack = ItemProvider.getStack(input);
			if(ItemUtils.isEmpty(stack))
				continue;

			if(ItemUtils.isCustomItem(stack))
				continue;

			float chance = ItemUtils.getCompostChance(stack.getType());
			if(chance <= 0.0F)
				continue;

			int mb = Math.round(chance * 200.0F);
			if(this.bioMass.fetch(block) + mb > this.capacity)
				return 0;

			input.get(1);
			PROCESSING.store(block, mb);
			block.update();

			return mb;
		}
		return 0;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {}


	@Override
	protected boolean finishProcess(TileState block) {
		this.bioMass.increase(block, PROCESSING.fetch(block));
		block.update();
		return true;
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
	public String getTitle() {
		return this.tier.getName() + " Bio Press";
	}


	@Override
	public String getMachineRegistryName() {
		return "bio_press";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.BIO_PRESS;
	}


	private int[] getInputSlots() {
		return MachineConstants.inputSlots[this.getTier().ordinal()];
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs(PersistentDataHolder holder) {
		return holder instanceof TileState block ? ItemProvider.fromInventory(this.getGui(block), this.getInputSlots()) : List.of();
	}


	@Override
	public List<FluidProvider> getFluidOutputs(PersistentDataHolder holder) {
		return List.of(FluidProvider.fromProperty(FluidType.BIO_MASS, this.bioMass, holder, this.capacity));
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, MachineConstants.BORDER_MATERIAL);
		for(int slot : new int[] { 14, 17, 23, 26, 32, 35 })
			gui.setItem(slot, MachineConstants.BORDER_MATERIAL);
		for(int slot : MachineConstants.inputBorderSlots[this.getTier().ordinal()])
			gui.setItem(slot, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int slot : MachineConstants.fillerSlots[this.getTier().ordinal()])
			gui.setItem(slot, MachineConstants.FILLER_MATERIAL);
		return gui;
	}


	private void updateTank(TileState block) {
		int biomass = this.bioMass.fetch(block);
		Inventory gui = this.getGui(block);
		int fill = (int) Math.round(biomass * 3.0D / this.capacity);
		int i = 0;
		ItemStack stack = new ItemBuilder(FluidType.BIO_MASS.createIcon()).addLore("§f" + biomass + " §7mb").build();
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
