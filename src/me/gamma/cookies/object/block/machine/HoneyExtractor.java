
package me.gamma.cookies.object.block.machine;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;



public class HoneyExtractor extends AbstractProcessingMachine implements FluidSupplier {

	private static final IntegerProperty HONEY = Properties.HONEY;
	private static final IntegerProperty EXTRACTING_HONEY = new IntegerProperty("extractinghoney");

	private static final int honey_capacity = 4000;

	public HoneyExtractor() {
		super(null);
	}


	@Override
	public List<FluidProvider> getFluidOutputs(PersistentDataHolder holder) {
		return Arrays.asList(FluidProvider.fromProperty(FluidType.HONEY, HONEY, holder, honey_capacity));
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return progress < 0.75D ? Material.GLASS_BOTTLE : Material.HONEY_BOTTLE;
	}


	@Override
	public String getTitle() {
		return "§fHoney Extractor";
	}


	@Override
	public String getMachineRegistryName() {
		return "honey_extractor";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.HONEY_EXTRACTOR;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(HONEY).add(EXTRACTING_HONEY).add(FLUID_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 1;
	}


	@Override
	protected int getUpgradeSlot() {
		return 10;
	}


	@Override
	protected int getProgressSlot() {
		return 19;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 28;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 37;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		InventoryUtils.fillBorder(gui, filler);
		gui.setItem(11, filler);
		gui.setItem(20, filler);
		gui.setItem(29, filler);
		return gui;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, TileState data, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public void tick(TileState block) {
		super.tick(block);
		this.updateTank(block);
	}


	@Override
	protected int createNextProcess(TileState block) {
		for(BlockFace face : BlockUtils.cartesian) {
			Block relative = block.getBlock().getRelative(face);
			if(!(relative.getBlockData() instanceof Beehive beehive))
				continue;

			int level = beehive.getHoneyLevel();
			if(level > 0) {
				EXTRACTING_HONEY.store(block, 50);
				beehive.setHoneyLevel(level - 1);
				relative.setBlockData(beehive);
				return 200;
			}
		}

		return 0;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {
		this.tryPushFluid(block);
	}


	@Override
	protected boolean finishProcess(TileState block) {
		this.tryPushFluid(block);

		int honey = HONEY.fetch(block);
		int extracting = EXTRACTING_HONEY.fetch(block);
		int transfer = Math.min(honey_capacity - honey, extracting);
		honey += transfer;
		extracting -= transfer;
		HONEY.store(block, honey);
		EXTRACTING_HONEY.store(block, extracting);
		return extracting == 0;
	}


	private void updateTank(TileState block) {
		int honey = HONEY.fetch(block);
		Inventory gui = this.getGui(block);
		int fill = (int) Math.round(honey * 3.0D / honey_capacity);
		int i = 0;
		ItemStack stack = new ItemBuilder(FluidType.HONEY.createIcon()).addLore("§f" + honey + " §7mb").build();
		for(; i < fill; i++)
			for(int j = 3; j < 8; j++)
				gui.setItem((3 - i) * 9 + j, stack);
		stack.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < 3; i++)
			for(int j = 3; j < 8; j++)
				gui.setItem((3 - i) * 9 + j, stack);
	}

}
