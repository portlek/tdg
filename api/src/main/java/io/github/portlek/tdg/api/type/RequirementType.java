package io.github.portlek.tdg.api.type;

import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum RequirementType {

    CLICK_TYPE("click-type", "click-types", "click", "clicks"),
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
