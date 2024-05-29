package com.gtnewhorizons.neid;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import com.gtnewhorizons.neid.mixins.Mixins;

@LateMixin
public class NEIDLateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.neid.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Mixins.getLateMixins(loadedMods);
    }
}
