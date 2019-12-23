package io.github.portlek.tdg.api.type;

import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum ActionType {

	MESSAGE("message", "messages", "msg"),
	COMMAND("command", "commands", "cmd"),
	SOUND("sound", "sounds"),
	OPEN_MENU("open-menu", "open_menu", "open"),
	PARTICLES("particles", "particle"),
	CLOSE_MENU("close-menu", "close_menu", "close"),
	ADD_ISLAND_LEVEL("add-island-level", "add_island_level", "add-island_level", "add_island-level"),
	REMOVE_ISLAND_LEVEL("remove-island-level", "remove_island_level", "remove-island_level", "remove_island-level"),
	TAKE_MONEY("take-money", "take_money"),
	GIVE_MONEY("give-money", "give_money"),
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