package ru.fewizz.idextender.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public enum Name {
    // self
    hooks("ru/fewizz/idextender/Hooks"),

    hooks_create16BArray(hooks, "create16BArray", null, null, "()[S"),
    hooks_getBlockData(
            hooks, "getBlockData", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)[B"),
    hooks_setBlockData(
            hooks, "setBlockData", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;[BI)V"),
    hooks_writeChunkToNbt(
            hooks,
            "writeChunkToNbt",
            null,
            null,
            "(Lnet/minecraft/nbt/NBTTagCompound;Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V"),
    hooks_readChunkFromNbt(
            hooks,
            "readChunkFromNbt",
            null,
            null,
            "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;Lnet/minecraft/nbt/NBTTagCompound;)V"),
    hooks_getBlockId(hooks, "getBlockId", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)I"),
    hooks_getBlockById(
            hooks,
            "getBlock",
            null,
            null,
            "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)Lnet/minecraft/block/Block;"),
    hooks_setBlockId(
            hooks, "setBlockId", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;IIII)V"),
    hooks_getIdFromBlockWithCheck(
            hooks, "getIdFromBlockWithCheck", null, null, "(Lnet/minecraft/block/Block;Lnet/minecraft/block/Block;)I"),
    hooks_copyBlockDataFromPacket(
            hooks, "copyBlockDataFromPacket", null, null, "(Lnet/minecraft/world/chunk/Chunk;[BIZ)I"),
    hooks_grabDataFromChunkBulkPacket(hooks, "grabDataFromChunkBulkPacket", null, null, "([BI[[B[IZ)V"),
    hooks_handleMultiBlockChange_handleMultiBlockChange_readBlockIDAdditionalBitsIfNeed(
            hooks,
            "handleMultiBlockChange_readBlockIDAdditionalBitsIfNeed",
            null,
            null,
            "(Ljava/io/DataInputStream;II)I"),

    // vanilla
    acl("net/minecraft/world/chunk/storage/AnvilChunkLoader", "aqk"),
    block("net/minecraft/block/Block", "aji"),
    chunk("net/minecraft/world/chunk/Chunk", "apx"),
    extendedBlockStorage("net/minecraft/world/chunk/storage/ExtendedBlockStorage", "apz"),
    iChunkProvider("net/minecraft/world/chunk/IChunkProvider", "apu"),
    nbtTagCompound("net/minecraft/nbt/NBTTagCompound", "dh"),
    nhpc("net/minecraft/client/network/NetHandlerPlayClient", "bjb"),
    nibbleArray("net/minecraft/world/chunk/NibbleArray", "apv"),
    packet("net/minecraft/network/Packet", "ft"),
    packetBuffer("net/minecraft/network/PacketBuffer", "et"),
    s22("net/minecraft/network/play/server/S22PacketMultiBlockChange", "gk"),
    s21("net/minecraft/network/play/server/S21PacketChunkData", "gx"),
    s21_extracted("net/minecraft/network/play/server/S21PacketChunkData$Extracted", "gy"),
    s26("net/minecraft/network/play/server/S26PacketMapChunkBulk", "gz"),
    world("net/minecraft/world/World", "ahb"),
    dataWatcher("net/minecraft/entity/DataWatcher", "te"),
    dataWatcher_watchableObject("net/minecraft/entity/DataWatcher$WatchableObject", "tf"),
    renderGlobal("net/minecraft/client/renderer/RenderGlobal", "bma"),
    playerControllerMP("net/minecraft/client/multiplayer/PlayerControllerMP", "bje"),
    itemInWorldManager("net/minecraft/server/management/ItemInWorldManager", "mx"),
    entityPlayer("net/minecraft/entity/player/EntityPlayer", "yz"),

    ebs_getBlock(
            extendedBlockStorage,
            "getBlockByExtId",
            "a",
            "func_150819_a",
            "(III)Lnet/minecraft/block/Block;"), // ExtendedBlockStorage
    ebs_setBlock(extendedBlockStorage, "func_150818_a", "a", null, "(IIILnet/minecraft/block/Block;)V"),
    ebs_getBlockLSBArray(extendedBlockStorage, "getBlockLSBArray", "g", "func_76658_g", "()[B"),
    ebs_getBlockMSBArray(
            extendedBlockStorage, "getBlockMSBArray", "i", "func_76660_i", "()Lnet/minecraft/world/chunk/NibbleArray;"),
    ebs_setBlockMSBArray(
            extendedBlockStorage,
            "setBlockMSBArray",
            "a",
            "func_76673_a",
            "(Lnet/minecraft/world/chunk/NibbleArray;)V"),
    ebs_isEmpty(extendedBlockStorage, "isEmpty", "a", "func_76663_a", "()Z"),
    ebs_removeInvalidBlocks(extendedBlockStorage, "removeInvalidBlocks", "e", "func_76672_e", "()V"),
    dataWatcher_addObject(dataWatcher, "addObject", "a", "func_75682_a", "(ILjava/lang/Object;)V"),
    dataWatcher_writeWatchableObjectToPacketBuffer(
            dataWatcher,
            "writeWatchableObjectToPacketBuffer",
            "a",
            "func_151510_a",
            "(Lnet/minecraft/network/PacketBuffer;Lnet/minecraft/entity/DataWatcher$WatchableObject;)V"),
    dataWatcher_readWatchedListFromPacketBuffer(
            dataWatcher,
            "readWatchedListFromPacketBuffer",
            "b",
            "func_151508_b",
            "(Lnet/minecraft/network/PacketBuffer;)Ljava/util/List;"),
    dataWatcher_writeWatchedListToPacketBuffer(
            dataWatcher,
            "writeWatchedListToPacketBuffer",
            "a",
            "func_151507_a",
            "(Ljava/util/List;Lnet/minecraft/network/PacketBuffer;)V"),
    dataWatcher_func_151509_a(
            dataWatcher, "func_151509_a", "a", "func_151509_a", "(Lnet/minecraft/network/PacketBuffer;)V"),
    ebs_blockRefCount(extendedBlockStorage, "blockRefCount", "b", "field_76682_b", "I"),
    ebs_tickRefCount(extendedBlockStorage, "tickRefCount", "c", "field_76683_c", "I"),
    ebs_lsb(extendedBlockStorage, "blockLSBArray", "d", "field_76680_d", "[B"),
    acl_writeChunkToNBT(
            acl,
            "writeChunkToNBT",
            "a",
            "func_75820_a",
            "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V"), // AnvilChunkLoader
    acl_readChunkFromNBT(
            acl,
            "readChunkFromNBT",
            "a",
            "func_75823_a",
            "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;"),
    block_getIdFromBlock(block, "getIdFromBlock", "b", "func_149682_b", "(Lnet/minecraft/block/Block;)I"),
    block_getBlockByID(block, "getBlockById", "e", "func_149729_e", "(I)Lnet/minecraft/block/Block;"),
    chunk_fillChunk(chunk, "fillChunk", "a", "func_76607_a", "([BIIZ)V"),
    packet_readPacketData(packet, "readPacketData", "a", "func_148837_a", "(Lnet/minecraft/network/PacketBuffer;)V"),
    packet_writePacketData(packet, "writePacketData", "b", "func_148840_b", "(Lnet/minecraft/network/PacketBuffer;)V"),
    nhpc_handleMultiBlockChange(
            nhpc,
            "handleMultiBlockChange",
            "a",
            "func_147287_a",
            "(Lnet/minecraft/network/play/server/S22PacketMultiBlockChange;)V"), // NetHandlerPlayClient
    s22_init_server(s22, "<init>", null, null, "(I[SLnet/minecraft/world/chunk/Chunk;)V"), // S22PacketMultiBlockChange
    s22_get_array(s22, "func_148921_d", "d", null, "()[B"),
    s22_get_blocks_count(s22, "func_148922_e", "e", null, "()I"),
    s21_undefined1(s21, "func_149275_c", "c", null, "()I"),
    s21_undefined2(
            s21,
            "func_149269_a",
            "a",
            null,
            "(Lnet/minecraft/world/chunk/Chunk;ZI)Lnet/minecraft/network/play/server/S21PacketChunkData$Extracted;"),
    // s21_readSize(s21, "readSize", null, null, "I"),
    s21_data(s21, "field_149278_f", "f", null, "[B"),
    s26_sections(s26, "field_149265_c", "c", null, "[I"),
    s26_hasSky(s26, "field_149267_h", "h", null, "Z"),
    s26_sectionsData(s26, "field_149260_f", "f", null, "[[B"),
    s26_readSize(s26, "readSize", null, null, "I"),
    renderGlobal_playAuxSFX(
            renderGlobal, "playAuxSFX", "a", "func_72706_a", "(Lnet/minecraft/entity/player/EntityPlayer;IIIII)V"),
    playerControllerMP_onPlayerDestroyBlock(playerControllerMP, "onPlayerDestroyBlock", "a", "func_78751_a", "(IIII)Z"),
    itemInWorldManager_tryHarvestBlock(itemInWorldManager, "tryHarvestBlock", "b", "func_73084_b", "(III)Z"),
    world_breakBlock(world, "func_147480_a", "a", null, "(IIIZ)Z"),

    // underground biomes
    ub_bud("exterminatorJeff/undergroundBiomes/worldGen/BiomeUndergroundDecorator"),

    ub_bud_replaceChunkOres_world(ub_bud, "replaceChunkOres", null, null, "(IILnet/minecraft/world/World;)V"),
    ub_bud_replaceChunkOres_iChunkProvider(
            ub_bud,
            "replaceChunkOres",
            null,
            null,
            "(Lnet/minecraft/world/chunk/IChunkProvider;II)V"), // BiomeUndergroundDecorator

    // MFQM
    MFQM("MoreFunQuicksandMod/main/MFQM"),

    MFQM_preInit(MFQM, "preInit", null, null, "(Lcpw/mods/fml/common/event/FMLPreInitializationEvent;)V");

    // for non-mc classes
    private Name(String deobf) {
        this(deobf, deobf);
    }

    // for mc classes
    private Name(String deobf, String obf) {
        this.clazz = null;
        this.deobf = deobf;
        this.obf = obf;
        this.srg = deobf;
        this.desc = null;
    }

    // for fields and methods
    private Name(Name clazz, String deobf, String obf, String srg, String desc) {
        this.clazz = clazz;
        this.deobf = deobf;
        this.obf = obf != null ? obf : deobf;
        this.srg = srg != null ? srg : deobf;
        this.desc = desc;
    }

    public boolean matches(MethodNode x) {
        assert desc.startsWith("(");
        return matches(x.name, x.desc);
    }

    public boolean matches(FieldNode x) {
        assert !desc.startsWith("(");
        return matches(x.name, x.desc);
    }

    public boolean matches(MethodInsnNode x) {
        assert desc.startsWith("(");
        return matches(x.owner, x.name, x.desc);
    }

    public boolean matches(String name, String desc) {
        if (IETransformer.isObfuscated)
            return obf.equals(name) && obfDesc.equals(desc) || srg.equals(name) && desc.equals(desc);
        else return deobf.equals(name) && desc.equals(desc);
    }

    public boolean matches(String owner, String name, String desc) {
        if (!matches(name, desc)) return false;
        if (IETransformer.isObfuscated) return clazz.obf.equals(owner) || clazz.srg.equals(owner);
        else return clazz.deobf.equals(owner);
    }

    public MethodInsnNode invocation(int opcode) {
        assert desc.startsWith("(");
        if (IETransformer.isObfuscated) // srg invocation
        return new MethodInsnNode(opcode, clazz.srg, srg, desc, false);
        else return new MethodInsnNode(opcode, clazz.deobf, deobf, desc, false);
    }

    public MethodInsnNode invokeStatic() {
        return invocation(INVOKESTATIC);
    }

    public MethodInsnNode invokeSpecial() {
        return invocation(INVOKESPECIAL);
    }

    public MethodInsnNode invokeVirtual() {
        return invocation(INVOKEVIRTUAL);
    }

    public FieldInsnNode field(int opcode) {
        assert !desc.startsWith("(");
        if (IETransformer.isObfuscated) // srg access
        return new FieldInsnNode(opcode, clazz.srg, srg, desc);
        else {
            return new FieldInsnNode(opcode, clazz.deobf, deobf, desc);
        }
    }

    public FieldInsnNode getStatic() {
        return field(GETSTATIC);
    }

    public FieldInsnNode getField() {
        return field(GETFIELD);
    }

    public FieldInsnNode putStatic() {
        return field(PUTSTATIC);
    }

    public FieldInsnNode putField() {
        return field(PUTFIELD);
    }

    private static void translateDescs() {
        StringBuilder sb = new StringBuilder();
        for (Name name : Name.values()) {
            if (name.desc == null) continue;
            int pos = 0;
            int endPos = -1;
            while ((pos = name.desc.indexOf('L', pos)) != -1) {
                sb.append(name.desc, endPos + 1, pos);
                endPos = name.desc.indexOf(';', pos + 1);
                String cName = name.desc.substring(pos + 1, endPos);
                for (Name name2 : Name.values()) {
                    if (name2.deobf.equals(cName)) {
                        cName = name2.obf;
                        break;
                    }
                }
                sb.append('L');
                sb.append(cName);
                sb.append(';');
                pos = endPos + 1;
            }
            sb.append(name.desc, endPos + 1, name.desc.length());
            name.obfDesc = sb.toString();
            sb.setLength(0);
            // System.out.printf("deobf: %s, obf: %s, desc: %s, obfDesc: %s%n", name.deobf, name.obf, name.desc,
            // name.obfDesc);
        }
    }

    public final Name clazz;
    public final String deobf;
    public final String obf;
    public final String srg;
    public final String desc;
    public String obfDesc;

    static {
        translateDescs();
    }
}
