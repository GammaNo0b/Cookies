
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.block.machine.DyerBlock;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.core.MinecraftItemHelper;
import me.gamma.cookies.util.math.MathHelper;



public class Dyer extends AbstractItemProcessingMachine<Dyer, DyerBlock> {

	public static final int INPUT_SLOT = 19;
	public static final int OUTPUT_SLOT = 25;
	public static final int DYE_INFO_SLOT = 32;
	public static final int COLOR_DISPLAY_SLOT = 23;
	public static final int RANDOMIZE_COLOR_SLOT = 14;

	private static final ItemStack RANDOM_COLOR_OFF = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§eRandom Color: §cOff").build();
	private static final ItemStack RANDOM_COLOR_ON = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Random Color: §aOn").build();

	private static final int dyeCapacity = 4000;
	private int red, green, blue, black;
	private Color selectedColor = Color.BLACK;
	private boolean randomizeColor = false;
	private ItemStack processing = null;
	private Color processingColor = Color.BLACK;

	public Dyer(DyerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	public Holder<Color> createColorHolder() {
		return Holder.create(() -> this.selectedColor, c -> {
			this.selectedColor = c;
			this.updateSelectedColor();
		});
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);
		this.updateRandomizeColor();
		this.updateSelectedColor();
		this.updateDyeStorage();
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.BRUSH;
	}


	@Override
	public int[] getInputSlots() {
		return new int[] { INPUT_SLOT };
	}


	@Override
	public int[] getOutputSlots() {
		return new int[] { OUTPUT_SLOT };
	}


	@Override
	protected int createNextProcess() {
		this.tryPullItems();

		Inventory gui = this.getInventory();
		ItemStack stack = gui.getItem(INPUT_SLOT);
		if(ItemUtils.isEmpty(stack))
			return 0;

		int dyeColor = MinecraftItemHelper.getDyeColor(stack);
		if(dyeColor != -1) {
			if(this.consumeColor(dyeColor, true))
				ItemUtils.increaseItem(stack, -1);
			return 0;
		}

		if(!this.hasColor())
			return 0;

		if(!(stack.getItemMeta() instanceof LeatherArmorMeta meta))
			return 0;

		// add new color
		Color color = this.randomizeColor ? Color.fromRGB(MathHelper.random.nextInt() >>> 8) : this.selectedColor;
		if(color.equals(meta.getColor()))
			return 0;

		meta.setColor(color);
		if(!this.consumeColor(color.asRGB(), false))
			return 0;

		this.processing = stack.clone();
		this.processing.setAmount(1);
		this.processing.setItemMeta(meta);
		this.processingColor = color;

		ItemUtils.increaseItem(stack, -1);

		return 200;
	}


	@Override
	protected void proceed() {
		super.proceed();

		ParticleManager.spawnParticle(Particle.DUST, new Particle.DustOptions(this.processingColor, 1.0F), 0.2D, 1, 3, block.getLocation().add(0.5D, 0.75D, 0.5D), 0.25D, 0.15D, 0.25D);
	}


	@Override
	protected boolean finishProcess() {
		List<ItemStack> results = new ArrayList<>();
		results.add(this.processing);
		if(!this.storeOutputs(results))
			return false;

		this.processing = null;

		this.tryPushItems();

		return true;
	}


	/**
	 * Calculates red, green, blue, black cost of the given color.
	 * 
	 * @param r amount of red of 256
	 * @param g amount of green of 256
	 * @param b amount of blue of 256
	 * @return rgbx cost
	 */
	private int[] getRGBXCost(int r, int g, int b) {
		int x = 255 - MathHelper.max(r, g, b);
		float t = r + g + b + x;
		return new int[] { Math.round(255.0F * r / t), Math.round(255.0F * g / t), Math.round(255.0F * b / t), Math.round(255.0F * x / t) };
	}


	/**
	 * Checks if there is enough dye in this dyer to dye randomly or the selected color.
	 * 
	 * @return if there is enough dye
	 */
	private boolean hasColor() {
		int[] available = { this.red, this.green, this.blue, this.black };
		int[] cost;

		if(this.randomizeColor) {
			cost = new int[] { 255, 255, 255, 255 };
		} else {
			cost = this.getRGBXCost(this.selectedColor.getRed(), this.selectedColor.getGreen(), this.selectedColor.getBlue());
		}

		for(int i = 0; i < 4; ++i)
			if(cost[i] > available[i])
				return false;

		return true;
	}


	/**
	 * Adds or removes the given color from this dyer.
	 * 
	 * @param color the color
	 * @param add   whether to add or remove the color
	 * @return if the operation was successful
	 */
	private boolean consumeColor(int color, boolean add) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;

		int[] cost = this.getRGBXCost(r, g, b);
		int cr = cost[0];
		int cg = cost[1];
		int cb = cost[2];
		int cx = cost[3];

		int nr, ng, nb, nx;

		if(add) {
			if((cr == 0 || this.red >= dyeCapacity) && (cg == 0 || this.green >= dyeCapacity) && (cb == 0 || this.blue > dyeCapacity) && (cx == 0 || this.black > dyeCapacity))
				return false;

			nr = Math.min(this.red + cr, 256);
			ng = Math.min(this.green + cg, 256);
			nb = Math.min(this.blue + cb, 256);
			nx = Math.min(this.black + cx, 256);
		} else {
			nr = this.red - cr;
			ng = this.green - cg;
			nb = this.blue - cb;
			nx = this.black - cx;

			if(nr < 0 || ng < 0 || nb < 0 || nx < 0)
				return false;
		}

		this.red = nr;
		this.green = ng;
		this.blue = nb;
		this.black = nx;

		this.updateDyeStorage();

		return true;
	}


	public void toggleRandomizeColor() {
		this.randomizeColor = !this.randomizeColor;
		this.updateRandomizeColor();
	}


	private void updateRandomizeColor() {
		this.getInventory().setItem(RANDOMIZE_COLOR_SLOT, this.randomizeColor ? RANDOM_COLOR_ON : RANDOM_COLOR_OFF);
	}


	private void updateSelectedColor() {
		int[] cost = this.getRGBXCost(this.selectedColor.getRed(), this.selectedColor.getGreen(), this.selectedColor.getBlue());
		this.getInventory().setItem(COLOR_DISPLAY_SLOT, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§fColor Preview").setLore(List.of("§4Red: §c" + cost[0], "§2Green: §a" + cost[1], "§1Blue: §9" + cost[2], "§8Black: §7" + cost[3])).setColor(this.selectedColor).build());
	}


	private void updateDyeStorage() {
		this.getInventory().setItem(DYE_INFO_SLOT, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§fColor:").addLore("  §4Red: §c" + this.red).addLore("  §2Green: §a" + this.green).addLore("  §1Blue: §9" + this.blue).addLore("  §8Black: §7" + this.black).build());
	}


	@Override
	public Dyer castTileEntity() {
		return this;
	}

}
