
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.gui.task.InventoryTask;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.gui.util.ColorWheel;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.ColorProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.core.MinecraftItemHelper;
import me.gamma.cookies.util.math.MathHelper;



public class Dyer extends AbstractItemProcessingMachine implements FluidSupplier {

	private static final int dyeCapacity = 4000;

	public static final ItemStack RANDOM_COLOR_OFF = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§eRandom Color: §cOff").build();
	public static final ItemStack RANDOM_COLOR_ON = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Random Color: §aOn").build();

	public static final IntegerProperty RED = new IntegerProperty("red", 0, dyeCapacity);
	public static final IntegerProperty GREEN = new IntegerProperty("green", 0, dyeCapacity);
	public static final IntegerProperty BLUE = new IntegerProperty("blue", 0, dyeCapacity);
	public static final IntegerProperty BLACK = new IntegerProperty("black", 0, dyeCapacity);
	public static final ColorProperty COLOR = new ColorProperty("color");
	public static final BooleanProperty RANDOM_COLOR = new BooleanProperty("random_color");
	public static final ItemStackProperty PROCESSING = new ItemStackProperty("processing");

	public Dyer() {
		super(null);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.DYE_FABRICATOR;
	}


	@Override
	public String getMachineRegistryName() {
		return "dyer";
	}


	@Override
	public String getTitle(TileState block) {
		return this.getTitle();
	}


	@Override
	public String getTitle() {
		return "§fDyer";
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(RED).add(GREEN).add(BLUE).add(BLACK).add(COLOR, Color.BLACK).add(RANDOM_COLOR, false).add(ITEM_INPUT_ACCESS_FLAGS, (byte) 0x3f).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3f).add(FLUID_OUTPUT_ACCESS_FLAGS, (byte) 0x3f).add(PROCESSING);
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { 19 };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 25 };
	}


	@Override
	public List<FluidProvider> getFluidOutputs(TileState block) {
		return List.of(FluidProvider.fromProperty(FluidType.RED, RED, block, dyeCapacity), FluidProvider.fromProperty(FluidType.GREEN, GREEN, block, dyeCapacity), FluidProvider.fromProperty(FluidType.BLUE, BLUE, block, dyeCapacity), FluidProvider.fromProperty(FluidType.BLACK, BLACK, block, dyeCapacity));
	}


	private boolean consumeDye(TileState block, ItemStack stack) {
		int c = MinecraftItemHelper.getDyeColor(stack.getType());
		if(c != -1) {
			int r = (c >> 16) & 0xFF;
			int g = (c >> 8) & 0xFF;
			int b = c & 0xFF;
			int x = 256 - (r + g + b) / 3;

			int nr = RED.fetch(block) + r;
			int ng = GREEN.fetch(block) + g;
			int nb = BLUE.fetch(block) + b;
			int nx = BLACK.fetch(block) + x;

			if((r == 0 || nr > dyeCapacity) && (g == 0 || ng > dyeCapacity) && (b == 0 || nb > dyeCapacity) && (x == 0 || nx > dyeCapacity))
				return false;

			RED.store(block, Math.min(dyeCapacity, nr));
			GREEN.store(block, Math.min(dyeCapacity, ng));
			BLUE.store(block, Math.min(dyeCapacity, nb));
			BLACK.store(block, Math.min(dyeCapacity, nx));
			ItemUtils.increaseItem(stack, -1);
			return true;
		}

		return false;
	}


	@Override
	public void tick(TileState block) {
		super.tick(block);
		this.updateStorage(block);
	}


	@Override
	protected int createNextProcess(TileState block) {
		super.createNextProcess(block);
		this.tryPushFluid(block);

		Inventory gui = this.getGui(block);
		ItemStack stack = gui.getItem(19);
		if(ItemUtils.isEmpty(stack))
			return 0;

		if(this.consumeDye(block, stack))
			return 0;

		if(!this.hasColor(block))
			return 0;

		if(!(stack.getItemMeta() instanceof LeatherArmorMeta meta))
			return 0;

		// add new color
		Color color = RANDOM_COLOR.fetch(block) ? Color.fromRGB(MathHelper.random.nextInt() >>> 8) : COLOR.fetch(block);
		if(color.equals(meta.getColor()))
			return 0;

		meta.setColor(color);
		this.removeColor(block, color);
		stack = stack.clone();
		stack.setAmount(1);
		stack.setItemMeta(meta);
		PROCESSING.store(block, stack);
		ItemUtils.increaseItem(gui.getItem(19), -1);
		block.update();
		return 200;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {
		super.proceed(block, progress, goal);
		this.tryPushFluid(block);

		ParticleManager.spawnParticle(Particle.DUST, new Particle.DustOptions(((LeatherArmorMeta) PROCESSING.fetch(block).getItemMeta()).getColor(), 1.0F), 0.2D, 1, 3, block.getLocation().add(0.5D, 0.75D, 0.5D), 0.25D, 0.15D, 0.25D);
	}


	@Override
	protected boolean finishProcess(TileState block) {
		super.finishProcess(block);
		this.tryPushFluid(block);

		List<ItemStack> results = new ArrayList<>();
		results.add(PROCESSING.fetch(block));
		if(!this.storeOutputs(block, results))
			return false;

		PROCESSING.storeEmpty(block);
		block.update();

		this.tryPushItems(block);
		return true;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.BRUSH;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));

		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		gui.setItem(12, filler);
		gui.setItem(21, filler);
		gui.setItem(30, filler);

		for(int i : new int[] { 9, 10, 11, 18, 20, 27, 28, 29 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);

		for(int i : new int[] { 15, 16, 17, 24, 26, 33, 34, 35 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);

		gui.setItem(23, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§7Color").build());

		return gui;
	}


	@Override
	public InventoryTask createInventoryTask(Inventory inventory, TileState data) {
		return new StaticInventoryTask(inventory) {

			@Override
			public void open(HumanEntity player) {
				super.open(player);
				if(data.getBlock().getState() instanceof TileState state)
					Dyer.this.updateColor(state);
			}

		};
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);
		this.updateRandomColor(block);
		this.updateStorage(block);
		this.updateColor(block);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 14) {
			RANDOM_COLOR.toggle(block);
			block.update();
			this.updateRandomColor(block);
		} else if(slot == 23) {
			ColorWheel.openColorWheel(player, block, COLOR);
		}

		return super.onMainInventoryInteract(player, block, gui, event);
	}


	private void updateRandomColor(TileState block) {
		Inventory gui = this.getGui(block);
		gui.setItem(14, RANDOM_COLOR.fetch(block) ? RANDOM_COLOR_ON : RANDOM_COLOR_OFF);
	}


	private void updateStorage(TileState block) {
		Inventory gui = this.getGui(block);
		int r = RED.fetch(block);
		int g = GREEN.fetch(block);
		int b = BLUE.fetch(block);
		int x = BLACK.fetch(block);
		gui.setItem(32, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§fColor:").addLore("  §4Red: §c" + r).addLore("  §2Green: §a" + g).addLore("  §1Blue: §9" + b).addLore("  §8Black: §7" + x).build());
	}


	private void updateColor(TileState block) {
		Inventory gui = this.getGui(block);
		Color color = COLOR.fetch(block);
		this.updateColor(gui.getItem(23), color);
	}


	private void updateColor(ItemStack preview, Color color) {
		LeatherArmorMeta meta = (LeatherArmorMeta) preview.getItemMeta();
		meta.setColor(color);
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int x = 256 - (r + g + b) / 3;
		meta.setLore(List.of("§4Red: §c" + r, "§2Green: §a" + g, "§1Blue: §9" + b, "§8Black: §7" + x));
		preview.setItemMeta(meta);
	}


	private boolean hasColor(TileState block) {
		int lr = RED.fetch(block);
		int lg = GREEN.fetch(block);
		int lb = BLUE.fetch(block);
		int lx = BLACK.fetch(block);

		int r = 255;
		int g = 255;
		int b = 255;
		int x = 255;

		if(!RANDOM_COLOR.fetch(block)) {
			Color color = COLOR.fetch(block);
			r = color.getRed();
			g = color.getGreen();
			b = color.getBlue();
			x = 256 - (r + g + b) / 3;
		}

		return r <= lr && g <= lg && b <= lb && x <= lx;
	}


	private void removeColor(TileState block, Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int x = 256 - (r + g + b) / 3;
		RED.decrease(block, r);
		GREEN.decrease(block, g);
		BLUE.decrease(block, b);
		BLACK.decrease(block, x);
	}

}
