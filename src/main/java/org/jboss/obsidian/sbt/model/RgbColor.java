package org.jboss.obsidian.sbt.model;

/**
 * @author Nike-Inc Backstopper samples
 */
public enum RgbColor {
	RED, GREEN, BLUE;

	@SuppressWarnings("unused")
	public static RgbColor toRgbColor(String colorString) {
		for (RgbColor color : values()) {
			if (color.name().equalsIgnoreCase(colorString))
				return color;
		}
		throw new IllegalArgumentException(
			"Cannot convert the string: \"" + colorString + "\" to a valid RgbColor enum value."
		);
	}
}
