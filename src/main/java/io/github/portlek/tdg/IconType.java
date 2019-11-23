package io.github.portlek.tdg;

import org.jetbrains.annotations.NotNull;

public enum IconType {

	HEAD("head"),
	BLOCK("block"),
	ITEM("item"),
	TOOL("tool");

	private final String type;

	IconType(String type) {
		this.type = type;
	}

	@NotNull
	public static IconType fromString(@NotNull String name) {
		for (IconType iconType : values()) {
			if (iconType.type.equalsIgnoreCase(name)) {
				return iconType;
			}
		}

		return HEAD;
	}

}