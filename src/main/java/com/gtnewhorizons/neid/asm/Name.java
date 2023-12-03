package com.gtnewhorizons.neid.asm;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public enum Name {

    hooks("com/gtnewhorizons/neid/Hooks"),
    MFQM("MoreFunQuicksandMod/main/MFQM"),
    ub_bud("exterminatorJeff/undergroundBiomes/worldGen/BiomeUndergroundDecorator"),
    extendedBlockStorage("net/minecraft/world/chunk/storage/ExtendedBlockStorage", "apz"),
    iChunkProvider("net/minecraft/world/chunk/IChunkProvider", "apu"),
    world("net/minecraft/world/World", "ahb"),
    hooks_getBlockId(Name.hooks, "getBlockId", null, null,
            "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)I"),
    ebs_getBlockLSBArray(Name.extendedBlockStorage, "getBlockLSBArray", "g", "func_76658_g", "()[B"),
    ub_bud_replaceChunkOres_world(Name.ub_bud, "replaceChunkOres", null, null, "(IILnet/minecraft/world/World;)V"),
    ub_bud_replaceChunkOres_iChunkProvider(Name.ub_bud, "replaceChunkOres", null, null,
            "(Lnet/minecraft/world/chunk/IChunkProvider;II)V"),
    MFQM_preInit(Name.MFQM, "preInit", null, null, "(Lcpw/mods/fml/common/event/FMLPreInitializationEvent;)V");

    private final Name clazz;
    public final String deobf;
    private final String obf;
    private final String srg;
    private final String desc;
    private String obfDesc;

    Name(final String deobf) {
        this(deobf, deobf);
    }

    Name(final String deobf, final String obf) {
        this.clazz = null;
        this.deobf = deobf;
        this.obf = obf;
        this.srg = deobf;
        this.desc = null;
    }

    Name(final Name clazz, final String deobf, final String obf, final String srg, final String desc) {
        this.clazz = clazz;
        this.deobf = deobf;
        this.obf = ((obf != null) ? obf : deobf);
        this.srg = ((srg != null) ? srg : deobf);
        this.desc = desc;
    }

    public boolean matches(final MethodNode x) {
        assert this.desc.startsWith("(");
        return (this.obf.equals(x.name) && this.obfDesc.equals(x.desc))
                || (this.srg.equals(x.name) && this.desc.equals(x.desc))
                || (this.deobf.equals(x.name) && this.desc.equals(x.desc));
    }

    public boolean matches(final MethodInsnNode x, final boolean obfuscated) {
        assert this.desc.startsWith("(");
        if (obfuscated) {
            return (this.clazz.obf.equals(x.owner) && this.obf.equals(x.name) && this.obfDesc.equals(x.desc))
                    || (this.clazz.srg.equals(x.owner) && this.srg.equals(x.name) && this.desc.equals(x.desc));
        }
        return this.clazz.deobf.equals(x.owner) && this.deobf.equals(x.name) && this.desc.equals(x.desc);
    }

    public MethodInsnNode staticInvocation(final boolean obfuscated) {
        assert this.desc.startsWith("(");
        if (obfuscated) {
            return new MethodInsnNode(184, this.clazz.srg, this.srg, this.desc, false);
        }
        return new MethodInsnNode(184, this.clazz.deobf, this.deobf, this.desc, false);
    }

    static {
        // This generates the obfuscated descriptors
        final StringBuilder sb = new StringBuilder();
        final Name[] VALUES = values();
        for (final Name name : VALUES) {
            if (name.desc != null) {
                int pos;
                int endPos;
                for (pos = 0, endPos = -1; (pos = name.desc.indexOf('L', pos)) != -1; pos = endPos + 1) {
                    sb.append(name.desc, endPos + 1, pos);
                    endPos = name.desc.indexOf(';', pos + 1);
                    String cName = name.desc.substring(pos + 1, endPos);
                    for (final Name name2 : VALUES) {
                        if (name2.deobf.equals(cName)) {
                            cName = name2.obf;
                            break;
                        }
                    }
                    sb.append('L');
                    sb.append(cName);
                    sb.append(';');
                }
                sb.append(name.desc, endPos + 1, name.desc.length());
                name.obfDesc = sb.toString();
                sb.setLength(0);
            }
        }
    }
}
