package io.github.portlek.tdg.types;

import org.cactoos.iterable.IterableOf;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.FirstOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum ActionType {
	
	MESSAGE("message", "messages", "msg"),
	COMMAND("command", "commands", "cmd"),
	SOUND("sound", "sounds"),
	OPEN_MENU("open-menu", "open"),
	PARTICLES("particles", "particle"),
	CLOSE_MENU("close-menu", "close"),
	NONE("none", "null");

	@NotNull
	private final List<String> types;

	ActionType(@NotNull String... types) {
		this.types = new ListOf<>(
			types
		);
	}

	public static ActionType fromString(@NotNull String name) {
		try {
			return new FirstOf<>(
				s -> s.types.contains(name),
				new IterableOf<>(
					values()
				),
				() -> NONE
			).value();
		} catch (Exception exception) {
			return NONE;
		}
	}

}