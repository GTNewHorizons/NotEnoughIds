package com.gtnewhorizons.neid.mixins;

public enum TargetedMod {

    VANILLA("Minecraft", null);

    public final String modName;

    public final String coreModClass;

    public final String modId;

    TargetedMod(String modName, String coreModClass) {
        this(modName, coreModClass, null);
    }

    TargetedMod(String modName, String coreModClass, String modId) {
        this.modName = modName;
        this.coreModClass = coreModClass;
        this.modId = modId;
    }

    @Override
    public String toString() {
        return "TargetedMod{modName'" + modName + "', coreModClass='" + coreModClass + "', modId='" + modId + "'}";
    }
}
