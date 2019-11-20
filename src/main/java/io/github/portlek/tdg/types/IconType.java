package io.github.portlek.tdg.types;

import org.cactoos.iterable.IterableOf;
import org.cactoos.scalar.FirstOf;
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

	public static IconType fromString(@NotNull String name) {
		try {
			return new FirstOf<>(
				s -> s.type.equalsIgnoreCase(name),
				new IterableOf<>(
					values()
				),
				() -> HEAD
			).value();
		} catch (Exception exception) {
			return HEAD;
		}
	}

}