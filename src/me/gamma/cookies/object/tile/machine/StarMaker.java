
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import me.gamma.cookies.init.Inventories;
import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.machine.StarMakerBlock;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.gui.task.InventoryTask;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.gui.util.ColorGrid;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.GuiUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class StarMaker extends AbstractItemProcessingMachine<StarMaker, StarMakerBlock> {

	private static final Random random = new Random();

	private static final int COLORS_SLOT = 12;
	private static final int FADE_COLORS_SLOT = 13;
	private static final int SHAPE_SLOT = 22;
	private static final int FLICKER_SLOT = 30;
	private static final int TRAIL_SLOT = 31;

	private static final String KEY_COLOR_CONFIGS = "colorconfigs";
	private static final String KEY_FIREWORK_SHAPE = "fireworkshape";
	private static final String KEY_FLICKER = "flicker";
	private static final String KEY_TRAIL = "trail";
	private static final String KEY_PROCESSING = "processing";

	private final ColorConfig[] configs = new ColorConfig[7];
	private FireworkShape fireworkShape = FireworkShape.BALL;
	private boolean flicker = false;
	private boolean trail = false;
	private ItemStack processing = null;

	public StarMaker(StarMakerBlock customBlock, Block block) {
		super(customBlock, block);

		for(int i = 0; i < configs.length; i++)
			this.configs[i] = new ColorConfig();
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		List<PersistentDataObject> array = data.getObjectList(KEY_COLOR_CONFIGS);
		if(array == null || array.size() != this.configs.length)
			return false;

		for(int i = 0; i < this.configs.length; i++)
			if(!this.configs[i].load(null, array.get(i)))
				return false;

		this.fireworkShape = PersistentDataUtils.getEnum(data, KEY_FIREWORK_SHAPE, FireworkShape.class);
		if(this.fireworkShape == null)
			return false;

		this.flicker = data.getBoolean(KEY_FLICKER, false);
		this.trail = data.getBoolean(KEY_TRAIL, false);
		this.processing = PersistentDataUtils.getItemStack(data, KEY_PROCESSING);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		List<PersistentDataObject> array = new ArrayList<>(this.configs.length);
		for(int i = 0; i < this.configs.length; i++) {
			PersistentDataObject o = new PersistentDataObject(data.getAdapterContext());
			this.configs[i].save(null, o);
			array.add(o);
		}
		data.setObjectList(KEY_COLOR_CONFIGS, array);

		PersistentDataUtils.setEnum(data, KEY_FIREWORK_SHAPE, this.fireworkShape);
		data.setBoolean(KEY_FLICKER, this.flicker);
		data.setBoolean(KEY_TRAIL, this.trail);
		PersistentDataUtils.setItemStack(data, KEY_PROCESSING, this.processing);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);

		inventory.setItem(COLORS_SLOT, new ItemBuilder(Material.GRAY_DYE).setName("§7Select Main Colors").build());
		inventory.setItem(FADE_COLORS_SLOT, new ItemBuilder(Material.GRAY_DYE).setName("§7Select Fade Colors").build());

		this.updateShape();
		this.updateFlicker();
		this.updateTrail();
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.FIREWORK_STAR;
	}


	@Override
	protected ItemBuilder createProgressIcon(double progress) {
		ItemBuilder builder = super.createProgressIcon(progress);
		if(progress == 0.0D)
			return builder;

		final Color green = Color.fromRGB(0x1E9600);
		final Color yellow = Color.fromRGB(0xFFF200);
		final Color red = Color.fromRGB(0xFF0000);
		return builder.setItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP).setColor(progress < 0.5D ? ColorUtils.combine(red, yellow, 2.0D * progress) : ColorUtils.combine(yellow, green, 2.0D * progress - 1.0D));
	}


	@Override
	public int[] getInputSlots() {
		return new int[] { 9, 10, 11, 18, 19, 20, 27, 28, 29 };
	}


	@Override
	public int[] getOutputSlots() {
		return new int[] { 25 };
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == COLORS_SLOT) {
			openColorSelectionPanel(player, false);
		} else if(slot == FADE_COLORS_SLOT) {
			openColorSelectionPanel(player, true);
		} else if(slot == SHAPE_SLOT) {
			ClickType click = event.getClick();
			int amount = click.isLeftClick() ? -1 : click.isRightClick() ? 1 : 0;
			if(amount != 0) {
				this.fireworkShape = EnumUtils.cycle(this.fireworkShape, amount);
				this.updateShape();
			}
		} else if(slot == FLICKER_SLOT) {
			this.flicker = !this.flicker;
			this.updateFlicker();
		} else if(slot == TRAIL_SLOT) {
			this.trail = !this.trail;
			this.updateTrail();
		} else {
			return super.onMainInventoryInteract(player, gui, event);
		}

		return true;
	}


	private void updateShape() {
		this.getInventory().setItem(SHAPE_SLOT, this.fireworkShape.createMenu());
	}


	private void updateFlicker() {
		this.getInventory().setItem(FLICKER_SLOT, GuiUtils.createBooleanMenu("§6Flicker", this.flicker, new ItemStack(Material.GLOWSTONE_DUST), "§aEnabled", new ItemStack(Material.GUNPOWDER), "§cDisabled").createMenu());
	}


	private void updateTrail() {
		this.getInventory().setItem(TRAIL_SLOT, GuiUtils.createBooleanMenu("§bTrail", this.trail, new ItemStack(Material.DIAMOND), "§aEnabled", new ItemStack(Material.FIREWORK_STAR), "§cDisabled").createMenu());
	}


	private Provider<ItemStack> getIngredient(Predicate<ItemStack> predicate) {
		for(Provider<ItemStack> input : this.getItemInputs())
			if(predicate.test(ItemProvider.getStack(input)))
				return input;

		return null;
	}


	@Override
	protected int createNextProcess() {
		super.createNextProcess();

		ItemStack fireworkStar = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta meta = (FireworkEffectMeta) fireworkStar.getItemMeta();
		FireworkEffect.Builder builder = FireworkEffect.builder();

		Provider<ItemStack> input;
		List<Provider<ItemStack>> ingredients = new ArrayList<>();

		input = this.getIngredient(stack -> ItemUtils.isType(stack, Material.GUNPOWDER));
		if(input == null)
			return 0;
		ingredients.add(input);

		for(int i = 0; i < 7; i++) {
			Color color = this.configs[i].getColor();
			if(color != null)
				builder.withColor(color);

			color = this.configs[i].getFadeColor();
			if(color != null)
				builder.withFade(color);
		}

		if(this.fireworkShape.hasIngredient()) {
			input = this.getIngredient(this.fireworkShape::isIngredient);
			if(input == null)
				return 0;
			ingredients.add(input);
		}
		builder.with(this.fireworkShape.getType());

		if(this.flicker) {
			input = this.getIngredient(stack -> ItemUtils.isType(stack, Material.GLOWSTONE_DUST));
			if(input == null)
				return 0;
			ingredients.add(input);
			builder.withFlicker();
		}

		if(this.trail) {
			input = this.getIngredient(stack -> ItemUtils.isType(stack, Material.DIAMOND));
			if(input == null)
				return 0;
			ingredients.add(input);
			builder.withTrail();
		}

		meta.setEffect(builder.build());
		fireworkStar.setItemMeta(meta);

		ingredients.forEach(p -> p.get(1));

		this.processing = fireworkStar;

		return 200;
	}


	@Override
	protected boolean finishProcess() {
		super.finishProcess();

		this.tryPushItems();
		return ItemUtils.isEmpty(this.processing = this.storeOutput(this.processing));
	}

	private class ColorConfig implements DataStorage {

		private static final String KEY_COLOR_MODE = "colormode";
		private static final String KEY_COLOR = "color";
		private static final String KEY_FADE_COLOR_MODE = "fadecolormode";
		private static final String KEY_FADE_COLOR = "fadecolor";

		ColorMode colorMode;
		Color color;
		ColorMode fadeColorMode;
		Color fadeColor;

		@Override
		public boolean load(Chunk chunk, PersistentDataObject data) {
			this.colorMode = PersistentDataUtils.getEnum(data, KEY_COLOR_MODE, ColorMode.class);
			if(this.colorMode == null)
				return false;

			this.color = PersistentDataUtils.getColor(data, KEY_COLOR);
			if(this.color == null)
				return false;

			this.fadeColorMode = PersistentDataUtils.getEnum(data, KEY_FADE_COLOR_MODE, ColorMode.class);
			if(this.fadeColorMode == null)
				return false;

			this.fadeColor = PersistentDataUtils.getColor(data, KEY_FADE_COLOR);
			if(this.fadeColor == null)
				return false;

			return true;
		}


		@Override
		public boolean save(Chunk chunk, PersistentDataObject data) {
			PersistentDataUtils.setEnum(data, KEY_COLOR_MODE, this.colorMode);
			PersistentDataUtils.setColor(data, KEY_COLOR, this.color);
			PersistentDataUtils.setEnum(data, KEY_FADE_COLOR_MODE, this.fadeColorMode);
			PersistentDataUtils.setColor(data, KEY_FADE_COLOR, this.fadeColor);

			return true;
		}


		public Color getColor() {
			return switch (this.colorMode) {
				case DISABLED -> null;
				case RANDOM -> Color.fromRGB(random.nextInt(0x1000000));
				case MANUAL -> this.color;
				default -> null;
			};
		}


		public Color getFadeColor() {
			return switch (this.fadeColorMode) {
				case DISABLED -> null;
				case RANDOM -> Color.fromRGB(random.nextInt(0x1000000));
				case MANUAL -> this.fadeColor;
				default -> null;
			};
		}

	}

	private void openColorSelectionPanel(HumanEntity player, boolean fade) {
		Inventories.COLOR_SELECTION_PANEL.openGui(player, new Data(this, fade));
	}

	private static record Data(StarMaker starMaker, boolean fade) {

		ColorMode getColorMode(int i) {
			ColorConfig config = this.starMaker.configs[i];
			return fade ? config.fadeColorMode : config.colorMode;
		}


		Color getColor(int i) {
			ColorConfig config = this.starMaker.configs[i];
			return fade ? config.fadeColor : config.color;
		}


		void setColor(int i, Color color) {
			ColorConfig config = this.starMaker.configs[i];
			if(fade) {
				config.fadeColor = color;
			} else {
				config.color = color;
			}
		}


		ColorMode cycleColorMode(int i, int amount) {
			ColorConfig config = this.starMaker.configs[i];
			if(this.fade) {
				return config.fadeColorMode = EnumUtils.cycle(config.fadeColorMode, amount);
			} else {
				return config.colorMode = EnumUtils.cycle(config.colorMode, amount);
			}
		}

	}

	public static class ColorSelectionInventory implements InventoryProvider<Data> {

		@Override
		public String getIdentifier() {
			return "star_naker_color_selection_panel";
		}


		@Override
		public int getIdentifierSlot() {
			return 0;
		}


		@Override
		public String getTitle(Data data) {
			return "Color Selection Panel";
		}


		@Override
		public int rows() {
			return 4;
		}


		@Override
		public Sound getSound() {
			return Sound.ITEM_BOOK_PAGE_TURN;
		}


		@Override
		public Inventory createGui(Data data) {
			Inventory gui = InventoryProvider.super.createGui(data);
			InventoryUtils.fillBorder(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
			for(int i = 0; i < 7; i++) {
				gui.setItem(i + 10, data.getColorMode(i).createMenu());
				gui.setItem(i + 19, new ItemBuilder(Material.FIREWORK_STAR).setColor(data.getColor(i)).build());
			}
			gui.setItem(4, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6<---").build());
			return gui;
		}


		public InventoryTask createInventoryTask(Inventory inventory, Data data) {
			return new StaticInventoryTask(inventory) {

				@Override
				public void open(HumanEntity player) {
					super.open(player);
					for(int i = 0; i < 7; i++)
						inventory.setItem(19 + i, new ItemBuilder(Material.FIREWORK_STAR).setColor(data.getColor(i)).build());
				}

			};
		}


		@Override
		public boolean onMainInventoryInteract(Player player, Data data, Inventory gui, InventoryClickEvent event) {
			int slot = event.getSlot();
			if(slot == 4) {
				History.travelBack(player);
				return true;
			}

			if(slot < 10 || slot > 25)
				return true;

			int column = slot % 9 - 1;
			if(0 <= column && column < 7) {
				if(slot < 18) {
					ClickType click = event.getClick();
					int amount = 0;
					if(click.isLeftClick()) {
						amount = -1;
					} else if(click.isRightClick()) {
						amount = 1;
					}
					if(amount != 0) {
						ColorMode mode = data.cycleColorMode(column, amount);
						gui.setItem(slot, mode.createMenu());
					}
					return true;
				} else {
					ColorGrid.openColorWheel(player, Holder.create(() -> data.getColor(column), color -> data.setColor(column, color)));
				}
			}

			return true;
		}

	}

	public static enum FireworkShape implements GuiUtils.Menu {

		BALL(Type.BALL, null, "§bSmall Ball", Material.FIREWORK_STAR),
		BALL_LARGE(Type.BALL_LARGE, "§3Large Ball", Material.FIRE_CHARGE),
		STAR(Type.STAR, "§eStar", Material.GOLD_NUGGET),
		BURST(Type.BURST, "§fBurst", Material.FEATHER),
		CREEPER(Type.CREEPER, Tag.ITEMS_SKULLS::isTagged, "§aCreeper", Material.CREEPER_HEAD);

		private final FireworkEffect.Type type;
		private final Predicate<Material> ingredientPredicate;
		private final String title;
		private final ItemStack icon;

		private FireworkShape(FireworkEffect.Type type, String title, Material icon) {
			this(type, m -> m == icon, title, icon);
		}


		private FireworkShape(FireworkEffect.Type type, Predicate<Material> ingredientPredicate, String title, Material icon) {
			this.type = type;
			this.ingredientPredicate = ingredientPredicate;
			this.title = title;
			this.icon = new ItemStack(icon);
		}


		public FireworkEffect.Type getType() {
			return this.type;
		}


		public boolean hasIngredient() {
			return this.ingredientPredicate != null;
		}


		public boolean isIngredient(ItemStack stack) {
			return !ItemUtils.isEmpty(stack) && !ItemUtils.isCustomItem(stack) && this.ingredientPredicate.test(stack.getType());
		}


		@Override
		public int size() {
			return values().length;
		}


		@Override
		public String getName() {
			return "§6Shape";
		}


		@Override
		public int selected() {
			return this.ordinal();
		}


		@Override
		public ItemStack getIcon(int index) {
			return values()[index].icon;
		}


		@Override
		public String get(int index) {
			return values()[index].title;
		}

	}

	public static enum ColorMode implements GuiUtils.Menu {

		MANUAL("§aManual", Material.LIME_STAINED_GLASS_PANE),
		RANDOM("§eRandom", Material.YELLOW_STAINED_GLASS_PANE),
		DISABLED("§cDisabled", Material.RED_STAINED_GLASS_PANE);

		private final String title;
		private final ItemStack icon;

		private ColorMode(String title, Material icon) {
			this.title = title;
			this.icon = new ItemStack(icon);
		}


		@Override
		public int size() {
			return values().length;
		}


		@Override
		public String getName() {
			return "§6Color Mode";
		}


		@Override
		public int selected() {
			return this.ordinal();
		}


		@Override
		public ItemStack getIcon(int index) {
			return values()[index].icon;
		}


		@Override
		public String get(int index) {
			return values()[index].title;
		}

	}

	@Override
	public StarMaker castTileEntity() {
		return this;
	}

}
