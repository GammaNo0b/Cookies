
package me.gamma.cookies.util;


import org.bukkit.Color;

import me.gamma.cookies.util.math.MathHelper;



public class ColorUtils {

	/**
	 * A color code sequence fading from dark red to magenta.
	 * 
	 * <ul>
	 * <li>a: dark red</li>
	 * <li>c: red</li>
	 * <li>6: orange</li>
	 * <li>e: yellow</li>
	 * <li>a: green</li>
	 * <li>b: aqua</li>
	 * <li>3: dark aqua</li>
	 * <li>9: blue</li>
	 * <li>1: dark blue</li>
	 * <li>5: purple</li>
	 * <li>d: magenta</li>
	 * </ul>
	 */
	public static final char[] RAINBOW_COLOR_SEQUENCE = "4c6eab3915d".toCharArray();

	/**
	 * A color code sequence fading from red over yellow to green.
	 * 
	 * <ul>
	 * <li>a: dark red</li>
	 * <li>c: red</li>
	 * <li>6: orange</li>
	 * <li>e: yellow</li>
	 * <li>a: green</li>
	 * <li>2: dark green</li>
	 * </ul>
	 */
	public static final char[] STOPLIGHT_PROGRESS = "4c6ea2".toCharArray();

	/**
	 * Colors the given string with the colors in the colorSequence array with colorLength amount of letters at a time.
	 * 
	 * @param string        the string to be colored.
	 * @param colorSequence the sequence of color codes to be used
	 * @param colorLength   the amount of letters that should last for one color
	 * @return the colored string
	 */
	public static String color(String string, char[] colorSequence, int colorLength) {
		if(colorSequence.length == 0)
			return string;

		if(colorSequence.length == 1)
			return "§" + colorSequence[0] + string;

		char[] original = string.toCharArray();
		char[] result = new char[original.length * 3];
		for(int i = 0; i < original.length; i++) {
			result[i * 3] = '§';
			result[i * 3 + 1] = colorSequence[i / colorLength % colorSequence.length];
			result[i * 3 + 2] = original[i];
		}

		return new String(result);
	}


	/**
	 * Returns the color in the given color sequence corresponding to the progress made.
	 * 
	 * @param progress      the progress
	 * @param colorSequence the color sequence
	 * @return the progress color
	 */
	public static char getProgressColor(double progress, char[] colorSequence) {
		return colorSequence[MathHelper.clamp(0, colorSequence.length - 1, (int) Math.round(progress * (colorSequence.length - 1)))];
	}


	/**
	 * Colors the string in a color dependent on the progress.
	 * 
	 * @param progress      the progress
	 * @param start         the minimum value of the progress
	 * @param end           the maximum value of the progress
	 * @param colorSequence the colors
	 * @return the colored string
	 */
	public static String colorProgress(double progress, double start, double end, char[] colorSequence) {
		return String.format("§%c%d", getProgressColor((progress - start) / end, colorSequence), (int) progress);
	}


	/**
	 * Returns the complementary color of the given one.
	 * 
	 * @param color the color
	 * @return the complementary color
	 */
	public static Color complement(Color color) {
		float[] hsb = java.awt.Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		return Color.fromRGB(java.awt.Color.HSBtoRGB(1.0F - hsb[0], hsb[1], hsb[2]) & 0xFFFFFF);
	}


	public static Color combine(Color from, Color to, double d) {
		int a1 = from.getAlpha();
		int a2 = to.getAlpha();
		int r1 = from.getRed();
		int r2 = to.getRed();
		int g1 = from.getGreen();
		int g2 = to.getGreen();
		int b1 = from.getBlue();
		int b2 = to.getBlue();
		double f = 1 - d;
		return Color.fromARGB((int) (a1 * f + a2 * d), (int) (r1 * f + r2 * d), (int) (g1 * f + g2 * d), (int) (b1 * f + b2 * d));
	}

}
