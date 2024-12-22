
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import me.gamma.cookies.init.Inventories;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.gui.task.InventoryTask;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.gui.util.ColorWheel;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.ColorProperty;
import me.gamma.cookies.object.property.EnumProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.GuiUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class StarMaker extends AbstractItemProcessingMachine {

	private static final Random random = new Random();

	private static final int COLORS_SLOT = 12;
	private static final int FADE_COLORS_SLOT = 13;
	private static final int SHAPE_SLOT = 22;
	private static final int FLICKER_SLOT = 30;
	private static final int TRAIL_SLOT = 31;

	public static final ColorProperty[] COLORS = ArrayUtils.generate(7, i -> new ColorProperty("color" + i), ColorProperty[]::new);
	@SuppressWarnings("unchecked")
	public static final EnumProperty<ColorMode>[] COLOR_MODES = ArrayUtils.generate(7, i -> new EnumProperty<>("colormode" + i, ColorMode.class), EnumProperty[]::new);
	public static final ColorProperty[] FADE_COLORS = ArrayUtils.generate(7, i -> new ColorProperty("fadecolor" + i), ColorProperty[]::new);
	@SuppressWarnings("unchecked")
	public static final EnumProperty<ColorMode>[] FADE_COLOR_MODES = ArrayUtils.generate(7, i -> new EnumProperty<>("fadecolormode" + i, ColorMode.class), EnumProperty[]::new);
	public static final EnumProperty<FireworkShape> FIREWORK_SHAPE = new EnumProperty<>("shape", FireworkShape.class);
	public static final BooleanProperty FLICKER = new BooleanProperty("flicker");
	public static final BooleanProperty TRAIL = new BooleanProperty("trail");
	public static final ItemStackProperty PROCESSING = Properties.PROCESSING;

	public StarMaker() {
		super(null);
	}


	@Override
	public String getTitle() {
		return "§eStar Maker";
	}


	@Override
	public String getMachineRegistryName() {
		return "star_maker";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STAR_MAKER;
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
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(PROCESSING).addAll(COLORS).addAll(FADE_COLORS).addAll(COLOR_MODES, ColorMode.DISABLED).addAll(FADE_COLOR_MODES, ColorMode.DISABLED).add(FIREWORK_SHAPE).add(FLICKER).add(TRAIL);
	}


	private Provider<ItemStack> getIngredient(TileState block, Predicate<ItemStack> predicate) {
		for(Provider<ItemStack> input : this.getItemInputs(block))
			if(predicate.test(ItemProvider.getStack(input)))
				return input;

		return null;
	}


	private Color getColor(TileState block, ColorProperty colorProperty, EnumProperty<ColorMode> colorMode) {
		switch (colorMode.fetch(block)) {
			case DISABLED:
				return null;
			case RANDOM:
				return Color.fromRGB(random.nextInt(0x1000000));
			case MANUAL:
				return colorProperty.fetch(block);
			default:
				return null;
		}
	}


	@Override
	protected int createNextProcess(TileState block) {
		super.createNextProcess(block);

		ItemStack fireworkStar = new ItemStack(Material.FIREWORK_STAR);
		FireworkEffectMeta meta = (FireworkEffectMeta) fireworkStar.getItemMeta();
		FireworkEffect.Builder builder = FireworkEffect.builder();

		Provider<ItemStack> input;
		List<Provider<ItemStack>> ingredients = new ArrayList<>();

		input = this.getIngredient(block, stack -> ItemUtils.isType(stack, Material.GUNPOWDER));
		if(input == null)
			return 0;
		ingredients.add(input);

		for(int i = 0; i < 7; i++) {
			Color color = this.getColor(block, COLORS[i], COLOR_MODES[i]);
			if(color != null)
				builder.withColor(color);

			color = this.getColor(block, FADE_COLORS[i], FADE_COLOR_MODES[i]);
			if(color != null)
				builder.withFade(color);
		}

		FireworkShape shape = FIREWORK_SHAPE.fetch(block);
		if(shape.hasIngredient()) {
			input = this.getIngredient(block, shape::isIngredient);
			if(input == null)
				return 0;
			ingredients.add(input);
		}
		builder.with(shape.getType());

		if(FLICKER.fetch(block)) {
			input = this.getIngredient(block, stack -> ItemUtils.isType(stack, Material.GLOWSTONE_DUST));
			if(input == null)
				return 0;
			ingredients.add(input);
			builder.withFlicker();
		}

		if(TRAIL.fetch(block)) {
			input = this.getIngredient(block, stack -> ItemUtils.isType(stack, Material.DIAMOND));
			if(input == null)
				return 0;
			ingredients.add(input);
			builder.withTrail();
		}

		meta.setEffect(builder.build());
		fireworkStar.setItemMeta(meta);

		ingredients.forEach(p -> p.get(1));

		PROCESSING.store(block, fireworkStar);
		block.update();

		return 200;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		super.finishProcess(block);

		List<ItemStack> outputs = ArrayUtils.asList(PROCESSING.fetch(block));
		if(!this.storeOutputs(block, outputs))
			return false;

		PROCESSING.storeEmpty(block);
		block.update();

		return true;
	}


	@Override
	protected int getUpgradeSlot() {
		return 14;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 41;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 32;
	}


	@Override
	protected int getProgressSlot() {
		return 23;
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 5;
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { 9, 10, 11, 18, 19, 20, 27, 28, 29 };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 25 };
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		gui.setItem(21, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : ArrayUtils.array(15, 16, 17, 24, 26, 33, 34, 35))
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);

		inventory.setItem(COLORS_SLOT, new ItemBuilder(Material.GRAY_DYE).setName("§7Select Main Colors").build());
		inventory.setItem(FADE_COLORS_SLOT, new ItemBuilder(Material.GRAY_DYE).setName("§7Select Fade Colors").build());

		this.updateShape(block);
		this.updateFlicker(block);
		this.updateTrail(block);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == COLORS_SLOT) {
			openColorSelectionPanel(player, block, false);
		} else if(slot == FADE_COLORS_SLOT) {
			openColorSelectionPanel(player, block, true);
		} else if(slot == SHAPE_SLOT) {
			ClickType click = event.getClick();
			int amount = click.isLeftClick() ? -1 : click.isRightClick() ? 1 : 0;
			if(amount != 0) {
				FIREWORK_SHAPE.cycle(block, amount);
				this.updateShape(block);
			}
		} else if(slot == FLICKER_SLOT) {
			FLICKER.toggle(block);
			this.updateFlicker(block);
		} else if(slot == TRAIL_SLOT) {
			TRAIL.toggle(block);
			this.updateTrail(block);
		} else {
			return super.onMainInventoryInteract(player, block, gui, event);
		}

		block.update();
		return true;
	}


	private void updateShape(TileState block) {
		this.getGui(block).setItem(SHAPE_SLOT, FIREWORK_SHAPE.fetch(block).createMenu());
	}


	private void updateFlicker(TileState block) {
		this.getGui(block).setItem(FLICKER_SLOT, GuiUtils.createBooleanMenu("§6Flicker", FLICKER.fetch(block), new ItemStack(Material.GLOWSTONE_DUST), "§aEnabled", new ItemStack(Material.GUNPOWDER), "§cDisabled").createMenu());
	}


	private void updateTrail(TileState block) {
		this.getGui(block).setItem(TRAIL_SLOT, GuiUtils.createBooleanMenu("§bTrail", TRAIL.fetch(block), new ItemStack(Material.DIAMOND), "§aEnabled", new ItemStack(Material.FIREWORK_STAR), "§cDisabled").createMenu());
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

	public static void openColorSelectionPanel(HumanEntity player, TileState block, boolean fade) {
		Inventories.COLOR_SELECTION_PANEL.openGui(player, new Data(block, fade ? FADE_COLORS : COLORS, fade ? FADE_COLOR_MODES : COLOR_MODES));
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
				gui.setItem(i + 10, data.colormodes[i].fetch(data.block).createMenu());
				gui.setItem(i + 19, new ItemBuilder(Material.FIREWORK_STAR).setColor(data.colors[i].fetch(data.block)).build());
			}
			gui.setItem(4, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6<---").build());
			return gui;
		}


		public InventoryTask createInventoryTask(Inventory inventory, Data data) {
			return new StaticInventoryTask(inventory) {

				@Override
				public void open(HumanEntity player) {
					super.open(player);
					if(data.block.getBlock().getState() instanceof TileState state)
						for(int i = 0; i < 7; i++)
							inventory.setItem(19 + i, new ItemBuilder(Material.FIREWORK_STAR).setColor(data.colors[i].fetch(state)).build());
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
						ColorMode mode = data.colormodes[column].cycle(data.block, amount);
						data.block.update();
						gui.setItem(slot, mode.createMenu());
					}
					return true;
				} else {
					ColorWheel.openColorWheel(player, data.block, data.colors[column]);
				}
			}

			return true;
		}


		@Override
		public void storeData(Inventory inventory, Data data) {
			ItemStack identifier = inventory.getItem(this.getIdentifierSlot());
			InventoryUtils.storeLocationInStack(identifier, "location", "world", data.block.getLocation());
			for(int i = 0; i < 7; i++) {
				InventoryUtils.storeStringInStack(identifier, "color" + i, data.colors[i].getName());
				InventoryUtils.storeStringInStack(identifier, "colormode" + i, data.colormodes[i].getName());
			}
		}


		@SuppressWarnings("unchecked")
		@Override
		public Data fetchData(Inventory inventory) {
			ColorProperty[] colors = new ColorProperty[7];
			EnumProperty<ColorMode>[] colorModes = new EnumProperty[7];

			ItemStack identifier = inventory.getItem(this.getIdentifierSlot());

			Location location = InventoryUtils.getLocationFromStack(identifier, "location", "world");
			if(location == null)
				return null;

			if(!(location.getBlock().getState() instanceof TileState block))
				return null;

			for(int i = 0; i < 7; i++) {
				String name;

				name = InventoryUtils.getStringFromStack(identifier, "color" + i);
				if(name == null)
					return null;

				colors[i] = new ColorProperty(name);

				name = InventoryUtils.getStringFromStack(identifier, "colormode" + i);
				if(name == null)
					return null;

				colorModes[i] = new EnumProperty<>(name, ColorMode.class);
			}

			return new Data(block, colors, colorModes);
		}

	}

	public static record Data(TileState block, ColorProperty[] colors, EnumProperty<ColorMode>[] colormodes) {}

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

}
