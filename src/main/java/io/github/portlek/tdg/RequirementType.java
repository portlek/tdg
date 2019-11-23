package io.github.portlek.tdg;

import org.cactoos.iterable.IterableOf;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.FirstOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum RequirementType {

    CLICK_TYPE("click", "click-type"),
    PERMISSIONS("permission", "permissions", "perm", "perms"),
    NONE("none");

    private final List<String> types;

    RequirementType(String... types) {
        this.types = new ListOf<>(types);
    }

    @NotNull
    public static RequirementType fromString(@NotNull String name) {
        for (RequirementType requirementType : values()) {
            if (requirementType.types.stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                return requirementType;
            }
        }

        return NONE;
    }

}
