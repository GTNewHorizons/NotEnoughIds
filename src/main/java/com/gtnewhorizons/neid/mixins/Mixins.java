package com.gtnewhorizons.neid.mixins;

import java.util.List;
import java.util.function.Supplier;

import com.gtnewhorizon.gtnhlib.mixin.IMixins;
import com.gtnewhorizon.gtnhlib.mixin.ITargetedMod;
import com.gtnewhorizon.gtnhlib.mixin.MixinBuilder;
import com.gtnewhorizon.gtnhlib.mixin.Phase;
import com.gtnewhorizon.gtnhlib.mixin.Side;
import com.gtnewhorizon.gtnhlib.mixin.TargetedMod;
import com.gtnewhorizons.neid.Common;
import com.gtnewhorizons.neid.NEIDConfig;

public enum Mixins implements IMixins {

    // spotless:off
    VANILLA_STARTUP(new MixinBuilder("Start Vanilla").addTargetedMod(TargetedMod.VANILLA)
        .setSide(Side.BOTH).setPhase(Phase.EARLY).addMixinClasses(
            "minecraft.MixinWorld",
            "minecraft.MixinExtendedBlockStorage",
            "minecraft.MixinStatList",
            "minecraft.MixinBlockFire",
            "minecraft.MixinS24PacketBlockAction",
            "minecraft.MixinItemInWorldManager",
            "minecraft.MixinAnvilChunkLoader",
            "minecraft.MixinBlock"
        ).setApplyIf(() -> true)),
    VANILLA_STARTUP_CLIENT(new MixinBuilder("Start Vanilla Client").addTargetedMod(TargetedMod.VANILLA)
        .setSide(Side.CLIENT).setPhase(Phase.EARLY).addMixinClasses(
            "minecraft.client.MixinRenderGlobal",
            "minecraft.client.MixinPlayerControllerMP"
        ).setApplyIf(() -> true)),
    VANILLA_STARTUP_DATAWATCHER(new MixinBuilder("Start Vanilla DataWatcher").addTargetedMod(TargetedMod.VANILLA)
        .setSide(Side.BOTH).setPhase(Phase.EARLY).addMixinClasses(
            "minecraft.MixinDataWatcher"
    ).setApplyIf(() -> NEIDConfig.ExtendDataWatcher));
    // spotless:on
    private final List<String> mixinClasses;
    private final List<ITargetedMod> targetedMods;
    private final List<ITargetedMod> excludedMods;
    private final Supplier<Boolean> applyIf;
    private final Phase phase;
    private final Side side;

    Mixins(MixinBuilder builder) {
        this.mixinClasses = builder.mixinClasses;
        this.targetedMods = builder.targetedMods;
        this.excludedMods = builder.excludedMods;
        this.applyIf = builder.applyIf;
        this.phase = builder.phase;
        this.side = builder.side;
        if (this.mixinClasses.isEmpty()) {
            throw new RuntimeException("No mixin class specified for Mixin : " + this.name());
        }
        if (this.targetedMods.isEmpty()) {
            throw new RuntimeException("No targeted mods specified for Mixin : " + this.name());
        }
        if (this.applyIf == null) {
            throw new RuntimeException("No ApplyIf function specified for Mixin : " + this.name());
        }
        if (this.phase == null) {
            throw new RuntimeException("No Phase specified for Mixin : " + this.name());
        }
        if (this.side == null) {
            throw new RuntimeException("No Side function specified for Mixin : " + this.name());
        }
    }

    @Override
    public List<String> getMixinClasses() {
        return mixinClasses;
    }

    @Override
    public Supplier<Boolean> getApplyIf() {
        return applyIf;
    }

    @Override
    public Phase getPhase() {
        return phase;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public List<ITargetedMod> getTargetedMods() {
        return targetedMods;
    }

    @Override
    public List<ITargetedMod> getExcludedMods() {
        return excludedMods;
    }
}
