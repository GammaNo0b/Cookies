
package me.gamma.cookies.util;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;

import me.gamma.cookies.Cookies;



public class Utils {

	public static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

	public static String toBinaryString(byte b) {
		char[] str = new char[8];
		for(int i = 0; i < 8; i++, b >>= 1)
			str[7 - i] = (char) ('0' + (b & 1));
		return new String(str);
	}


	public static String getIdentifierFromName(String name) {
		return name.replaceAll("§[0-9a-fk-or]", "").replace(' ', '_').toLowerCase();
	}


	public static String formatTicks(long ticks) {
		if(ticks == 0)
			return "0t";	

		StringBuilder builder = new StringBuilder();

		final String[] units = { "t", "s", "min", "h" };
		final int[] ratios = { 20, 60, 60, 24 };

		long t;
		long next;
		long time = ticks;
		for(int i = 0; i < units.length; i++) {
			next = time / ratios[i];
			t = time - next * ratios[i];
			time = next;
			if(t > 0) {
				builder.insert(0, units[i]);
				builder.insert(0, t);
				builder.insert(0, ' ');
			}
		}

		return builder.substring(1);
	}


	public static boolean isEaster() {
		return currentTimeIsBetween(Calendar.APRIL, 1, Calendar.APRIL, 30);
	}


	public static boolean isHalloween() {
		return currentTimeIsBetween(Calendar.OCTOBER, 20, Calendar.NOVEMBER, 4);
	}


	public static boolean isChristmas() {
		return currentTimeIsBetween(Calendar.DECEMBER, 18, Calendar.DECEMBER, 30);
	}


	public static boolean currentTimeIsBetween(int startMonth, int startDate, int endMonth, int endDate) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar current = Calendar.getInstance();
		int year = current.get(Calendar.YEAR);
		start.set(year, startMonth, startDate, 0, 0, 0);
		end.set(year, endMonth, endDate, 0, 0, 0);
		return current.after(start) && current.before(end);
	}


	public static String splitCamelCase(String string) {
		if(string.isEmpty())
			return "";

		List<Character> list = new ArrayList<>(string.length());
		int len = string.length();
		char last = string.charAt(0);
		list.add(last);
		for(int i = 1; i < len; i++) {
			char c = string.charAt(i);
			if(!Character.isUpperCase(last) && Character.isUpperCase(c))
				list.add(' ');
			list.add(c);
		}

		char[] array = new char[list.size()];
		for(int i = 0; i < array.length; i++)
			array[i] = list.get(i);

		return new String(array);
	}


	public static String toCapitalWords(String string) {
		if(string.isEmpty())
			return string;
		StringBuilder builder = new StringBuilder();
		String[] split = string.split(" ");
		for(int i = 0; i < split.length; i++) {
			String str = split[i].toLowerCase();
			if(!str.isEmpty()) {
				char first = str.charAt(0);
				builder.append(str.replaceFirst(first + "", Character.toUpperCase(first) + "")).append(' ');
			}
		}
		return builder.deleteCharAt(builder.length() - 1).toString();
	}


	public static <E extends Enum<E>> String toCapitalWords(E element) {
		return toCapitalWords(element.name().replace('_', ' '));
	}


	public static String romanNumber(int n) {
		char[] main = "IXCM".toCharArray();
		char[] help = "VLDA".toCharArray();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < main.length; i++) {
			int j = n % 10;
			n /= 10;
			if(j % 5 == 4) {
				builder.insert(0, j == 4 ? help[i] : main[i + 1]);
				builder.insert(0, main[i]);
				continue;
			}
			for(int k = 0; k < j % 5; k++)
				builder.insert(0, main[i]);
			if(j >= 5)
				builder.insert(0, help[i]);
		}
		while(n > 5) {
			n -= 5;
			builder.insert(0, help[help.length - 1]);
		}
		while(n-- > 0)
			builder.insert(0, main[main.length - 1]);
		return builder.toString();
	}


	/**
	 * Runs the given task later in the same tick.
	 * 
	 * @param task the task
	 */
	public static void runLater(Runnable task) {
		runLater(0, task);
	}


	/**
	 * Runs the given task later.
	 * 
	 * @param ticks the amount of ticks after that the task should be run
	 * @param task  the task
	 */
	public static void runLater(long ticks, Runnable task) {
		Bukkit.getScheduler().runTaskLater(Cookies.INSTANCE, task, ticks);
	}

}
