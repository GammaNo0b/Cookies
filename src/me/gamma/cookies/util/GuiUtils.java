
package me.gamma.cookies.util;


import java.util.function.Supplier;

import org.bukkit.inventory.ItemStack;



public class GuiUtils {

	/**
	 * A simple menu.
	 * 
	 * @author gamma
	 *
	 */
	public static interface Menu {

		/**
		 * Returns the size of this menu.
		 * 
		 * @return the size
		 */
		int size();

		/**
		 * Returns the name of this menu.
		 * 
		 * @return the name
		 */
		String getName();

		/**
		 * Returns the index of the selected menu point.
		 * 
		 * @return the selected menu point
		 */
		int selected();

		/**
		 * Returns the icon of this menu item.
		 * 
		 * @param index the index
		 * @return the icon
		 */
		ItemStack getIcon(int index);


		/**
		 * Creates the icon for the current menu item.
		 * 
		 * @param index the index
		 * @return the menu icon item builder
		 */
		default ItemBuilder createIcon(int index) {
			return new ItemBuilder(this.getIcon(index));
		}


		/**
		 * Returns the menu point at the given index.
		 * 
		 * @param index the index
		 * @return the menu point
		 */
		String get(int index);


		/**
		 * Creates the menu item.
		 * 
		 * @return the menu item
		 */
		default ItemStack createMenu() {
			ItemBuilder builder = this.createIcon(this.selected());
			if(this.size() > 0) {
				builder.setName("§7" + this.getName());
				builder.addLore("");
				for(int i = 0; i < this.size(); i++) {
					String name = this.get(i);
					if(this.selected() == i) {
						builder.addLore("§7► " + name);
					} else {
						builder.addLore("§8  " + name);
					}
				}
			}
			return builder.build();
		}

	}

	/**
	 * A boolean menu with only two items: true and false.
	 */
	public static abstract class BooleanMenu implements Menu {

		protected boolean value;

		public BooleanMenu(boolean value) {
			this.value = value;
		}


		@Override
		public int size() {
			return 2;
		}


		@Override
		public int selected() {
			return this.value ? 0 : 1;
		}


		@Override
		public ItemStack getIcon(int index) {
			return this.getIcon(index == 0);
		}


		public abstract ItemStack getIcon(boolean value);


		@Override
		public ItemBuilder createIcon(int index) {
			return this.createIcon(index == 0);
		}


		public ItemBuilder createIcon(boolean value) {
			return Menu.super.createIcon(value ? 0 : 1);
		}


		@Override
		public String get(int index) {
			return this.get(index == 0);
		}


		public abstract String get(boolean value);

	}

	/**
	 * Creates a new boolean menu.
	 * 
	 * @param name             the name of the menu
	 * @param value            the currently selected value
	 * @param enabledMaterial  the enabled menu item icon
	 * @param enabledString    the enabled menu item string
	 * @param disabledMaterial the disabled menu item icon
	 * @param disabledString   the disabled menu item string
	 * @return the new boolean menu
	 */
	public static BooleanMenu createBooleanMenu(String name, boolean value, ItemStack enabledMaterial, String enabledString, ItemStack disabledMaterial, String disabledString) {
		return new BooleanMenu(value) {

			@Override
			public String getName() {
				return name;
			}


			@Override
			public ItemStack getIcon(boolean value) {
				return value ? enabledMaterial : disabledMaterial;
			}


			@Override
			public String get(boolean value) {
				return value ? enabledString : disabledString;
			}

		};
	}


	/**
	 * Creates a new menu.
	 * 
	 * @param <E>     the enum type
	 * @param element the currently selected element
	 * @return the menu
	 */
	public static <E extends Enum<E> & Supplier<ItemStack>> Menu createEnumMenu(final E element) {
		return new Menu() {

			final Class<E> clazz = element.getDeclaringClass();
			final E[] values = clazz.getEnumConstants();

			@Override
			public int size() {
				return values.length;
			}


			@Override
			public int selected() {
				return element.ordinal();
			}


			@Override
			public String getName() {
				return Utils.splitCamelCase(this.clazz.getSimpleName());
			}


			@Override
			public ItemStack getIcon(int index) {
				return this.values[index].get();
			}


			@Override
			public String get(int index) {
				return Utils.toCapitalWords(this.values[index]);
			}

		};
	}

}
