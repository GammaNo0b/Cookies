
package me.gamma.cookies.object.tile.machine;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.HoneyExtractorBlock;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class HoneyExtractor extends AbstractProcessingMachine<HoneyExtractor, HoneyExtractorBlock> implements FluidSupplier {

	private static final int honey_capacity = 4000;

	private static final String KEY_HONEY = "honey";
	private static final String KEY_EXTRACTING = "extracting";

	private byte fluidOutputAccessFlags = 0x3f;
	private int honey = 0;
	private int extracting = 0;

	public HoneyExtractor(HoneyExtractorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidSupplier.super.load(chunk, data);

		this.honey = data.getInteger(KEY_HONEY, 0);
		this.extracting = data.getInteger(KEY_EXTRACTING, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidSupplier.super.save(chunk, data);

		data.setInteger(KEY_HONEY, this.honey);
		data.setInteger(KEY_EXTRACTING, this.extracting);

		return true;
	}


	@Override
	public void tick() {
		super.tick();
		this.updateTank();
	}


	@Override
	protected int createNextProcess() {
		for(BlockFace face : BlockUtils.cartesian) {
			Block relative = this.block.getRelative(face);
			if(!(relative.getBlockData() instanceof Beehive beehive))
				continue;

			int level = beehive.getHoneyLevel();
			if(level > 0) {
				this.extracting = 50;
				beehive.setHoneyLevel(level - 1);
				relative.setBlockData(beehive);
				return 200;
			}
		}

		return 0;
	}


	@Override
	protected boolean finishProcess() {
		this.tryPushFluid();

		int transfer = Math.min(honey_capacity - this.honey, this.extracting);
		this.honey += transfer;
		this.extracting -= transfer;
		return this.extracting == 0;
	}


	private void updateTank() {
		Inventory gui = this.getInventory();
		int fill = (int) Math.round(this.honey * 3.0D / honey_capacity);
		int i = 0;
		ItemStack stack = new ItemBuilder(FluidType.HONEY.createIcon()).addLore("§f" + this.honey + " §7mb").build();
		for(; i < fill; i++)
			for(int j = 3; j < 8; j++)
				gui.setItem((3 - i) * 9 + j, stack);
		stack.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < 3; i++)
			for(int j = 3; j < 8; j++)
				gui.setItem((3 - i) * 9 + j, stack);
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return progress < 0.75D ? Material.GLASS_BOTTLE : Material.HONEY_BOTTLE;
	}


	@Override
	public List<FluidProvider> getFluidOutputs() {
		return List.of(FluidProvider.fromHolder(FluidType.HONEY, Holder.create(() -> this.honey, i -> { this.honey = i; }), honey_capacity));
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
	public HoneyExtractor castTileEntity() {
		return this;
	}

}
