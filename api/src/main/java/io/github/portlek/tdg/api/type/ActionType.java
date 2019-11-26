package io.github.portlek.tdg.api.type;

import org.cactoos.list.ListOf;
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

	@NotNull
	public static ActionType fromString(@NotNull String name) {
		for (ActionType actionType : values()) {
			if (actionType.types.stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
				return actionType;
			}
		}

		return NONE;
	}

}