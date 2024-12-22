
package me.gamma.cookies.util;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.init.Registries;
import me.gamma.cookies.object.item.AbstractCustomItem;



public class JsonUtils {

	public static final JsonObject EMPTY = new JsonObject();

	static {
		EMPTY.addProperty("item", "minecraft:air");
		EMPTY.addProperty("count", 0);
	}

	public static JsonObject convertItemStack(ItemStack stack) {
		if(ItemUtils.isEmpty(stack))
			return EMPTY;

		JsonObject object = new JsonObject();
		AbstractCustomItem item = Items.getCustomItemFromStack(stack);
		String id = item == null ? "minecraft:" + stack.getType().name().toLowerCase() : "cookies:" + item.getIdentifier();
		object.addProperty("item", id);
		object.addProperty("count", stack.getAmount());
		return object;
	}


	public static ItemStack parseItemStack(JsonObject object) {
		String id = object.get("item").getAsString();
		String[] split = id.split(":");
		JsonElement counte = object.get("count");
		int count = 1;
		if(counte != null)
			count = counte.getAsInt();

		if(split.length == 1 || split.length == 2 && split[0].equals("minecraft")) {
			return new ItemStack(Material.matchMaterial(split[1]), count);
		} else if(split.length == 2 && split[0].equals("cookies")) {
			AbstractCustomItem item = Registries.ITEMS.filterFirst(i -> i.getIdentifier().equals(split[1]));
			if(item == null)
				return null;

			ItemStack stack = item.get();
			stack.setAmount(count);
			return stack;
		} else {
			return null;
		}
	}


	public static JsonArray convertItemStacks(ItemStack... stacks) {
		JsonArray array = new JsonArray(stacks.length);
		for(int i = 0; i < stacks.length; i++)
			array.add(convertItemStack(stacks[i]));
		return array;
	}


	public static ItemStack[] parseItemStacks(JsonElement element) {
		if(element == null)
			return new ItemStack[0];
		if(element.isJsonObject())
			return new ItemStack[] { parseItemStack(element.getAsJsonObject()) };
		if(element.isJsonArray()) {
			JsonArray array = element.getAsJsonArray();
			ItemStack[] stacks = new ItemStack[array.size()];
			for(int i = 0; i < stacks.length; i++)
				stacks[i] = parseItemStack(array.get(i).getAsJsonObject());
			return stacks;
		}
		return new ItemStack[0];
	}

}
