
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.Vector;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.VectorProperty;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class Quarry extends AbstractItemProcessingMachine {

	public static final VectorProperty BREAK_POS = Properties.POS;
	public static final ListProperty<ItemStack, ItemStackProperty> DROPS = new ListProperty<>("drops", ItemStackProperty::new);

	private static final int RESET_SLOT = 18;
	private static final ItemStack RESET_ICON = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§cReset").build();

	private List<String> dimensions;
	private boolean blacklisted;

	public Quarry() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.dimensions = config.getStringList("dimensions");
		this.blacklisted = config.getBoolean("blacklisted", true);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);
		inventory.setItem(RESET_SLOT, RESET_ICON);
	}


	@Override
	public String getTitle() {
		return "§dQuarry";
	}


	@Override
	public String getMachineRegistryName() {
		return "quarry";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.QUARRY;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.NETHERITE_PICKAXE;
	}


	@Override
	public int rows() {
		return 5;
	}


	private boolean isValidDimension(World world) {
		return this.dimensions.contains(world.getName()) != this.blacklisted;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(BREAK_POS).add(DROPS);
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		InventoryUtils.fillTopBottom(gui, filler);
		gui.setItem(9, filler);
		gui.setItem(27, filler);
		for(int i : new int[] { 12, 21, 28, 29, 30 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : new int[] { 14, 23, 32 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { 10, 11, 19, 20 };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 15, 16, 17, 24, 25, 26, 33, 34, 35 };
	}


	@Override
	public boolean onBlockPlace(Player player, PersistentDataHolder holder, TileState block) {
		if(super.onBlockPlace(player, holder, block))
			return true;

		this.reset(block);

		return false;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, block, gui, event))
			return false;

		if(event.getSlot() == RESET_SLOT)
			this.reset(block);

		return true;
	}


	private Block nextBlock(TileState block) {
		Vector old = BREAK_POS.fetch(block);

		int x = old.getBlockX();
		int y = old.getBlockY();
		int z = old.getBlockZ();

		if(++x >= 16) {
			x = 0;

			if(++z >= 16) {
				z = 0;

				if(y <= block.getWorld().getMinHeight())
					return null;

				y--;
			}
		}

		BREAK_POS.store(block, new Vector(x, y, z));

		Location location = block.getLocation().subtract(old.getX(), 0, old.getZ()).subtract(1, 1, 1);
		location.setY(old.getY());
		return location.getBlock();
	}


	@Override
	protected int createNextProcess(TileState block) {
		super.createNextProcess(block);

		if(!this.isValidDimension(block.getWorld()))
			return 0;

		Block b = this.nextBlock(block);
		if(b == null) {
			return 0;
		} else if(b.isEmpty()) {
			return 2;
		}

		if(b.isLiquid()) {
			b.setType(Material.AIR);
			return 40;
		}

		if(BlockUtils.isCustomBlock(b))
			return 10;

		ItemStack bestTool = null;
		int bestSpeed = BlockUtils.getBlockBreakTime(b, null);
		if(bestSpeed <= 0)
			return 10;

		for(Provider<ItemStack> provider : this.getItemInputs(block)) {
			ItemStack tool = ItemProvider.getStack(provider);
			if(ItemUtils.isEmpty(tool))
				continue;

			int speed = Math.max(1, BlockUtils.getBlockBreakTime(b, tool));
			if(speed < bestSpeed) {
				bestSpeed = speed;
				bestTool = tool;
			}
		}

		List<ItemStack> drops = new ArrayList<>();
		drops.addAll(b.getDrops(bestTool));
		if(b.getState() instanceof BlockInventoryHolder holder) {
			Inventory inventory = holder.getInventory();
			drops.addAll(Arrays.asList(inventory.getContents()));
			inventory.clear();
		}
		DROPS.store(block, drops);

		Location back = block.getLocation().add(0.5D, 0.5D, 0.5D).add(((Rotatable) block.getBlockData()).getRotation().getDirection().multiply(-0.25D));
		final BlockData data = b.getBlockData().clone();
		ParticleManager.drawAnimatedLine(b.getLocation().add(0.5D, 0.5D, 0.5D), back, 1, 50, pos -> pos.getWorld().spawnParticle(Particle.BLOCK, pos, 1, 0.1F, 0.1F, 0.1F, data));
		b.setType(Material.AIR);

		return bestSpeed;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		super.finishProcess(block);

		List<ItemStack> results = DROPS.fetch(block);
		while(!this.storeOutputs(block, results)) {
			if(!this.tryPushItems(block)) {
				DROPS.store(block, results);
				return false;
			}
		}

		DROPS.store(block, results);
		while(this.tryPushItems(block));

		return true;
	}


	private void reset(TileState block) {
		BREAK_POS.store(block, new Vector(0, block.getY() - 1, 0));
		block.update();
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		drops.addAll(DROPS.fetch(block));

		return drops;
	}


	@Override
	protected int getInputModeSlot() {
		return 38;
	}

}
