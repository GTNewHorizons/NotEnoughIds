package com.gtnewhorizons.neid.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

public enum TargetMods implements ITargetMod {

    THERMOS(new TargetModBuilder().setTargetClass("org.bukkit.World"));

    private final TargetModBuilder builder;

    TargetMods(TargetModBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public TargetModBuilder getBuilder() {
        return builder;
    }
}
