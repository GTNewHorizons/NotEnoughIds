package ru.fewizz.idextender.asm;

import java.io.PrintWriter;
import java.util.ListIterator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class AsmUtil implements Opcodes {
    public static MethodNode findMethod(ClassNode cn, String name, String desc, boolean optional) {
        for (MethodNode ret : cn.methods) {
            if (ret.name.equals(name) && (desc == null || ret.desc.equals(desc))) return ret;
        }

        if (!optional) throw new MethodNotFoundException(name);
        return null;
    }

    public static MethodNode findMethod(ClassNode cn, String name, boolean optional) {
        return findMethod(cn, name, null, optional);
    }

    public static MethodNode findMethod(ClassNode cn, String name) {
        return findMethod(cn, name, null, false);
    }

    public static MethodNode findMethod(ClassNode cn, Name name) {
        return findMethod(cn, name, false);
    }

    public static MethodNode findMethod(ClassNode cn, Name name, boolean optional) {
        MethodNode m = findMethod(cn, name.deobf, name.desc, true);
        if (m == null) m = findMethod(cn, name.srg, name.desc, true);
        if (m == null) m = findMethod(cn, name.obf, name.obfDesc, true);
        if (m == null && !optional) throw new MethodNotFoundException(name.deobf);
        return m;
    }

    public static FieldNode findField(ClassNode cn, String name, String desc, boolean optional) {
        for (FieldNode ret : cn.fields) {
            if (name.equals(ret.name) && (desc == null || ret.desc.equals(desc))) return ret;
        }
        if (!optional) throw new FieldNotFoundException(name);
        return null;
    }

    public static FieldNode findField(ClassNode cn, Name name, boolean optional) {
        FieldNode f = findField(cn, name.deobf, name.desc, true);
        if (f == null) f = findField(cn, name.srg, name.desc, true);
        if (f == null) f = findField(cn, name.obf, name.obfDesc, true);
        if (f == null && !optional) throw new MethodNotFoundException(name.deobf);
        return f;
    }

    public static FieldNode findField(ClassNode cn, String name, boolean optional) {
        return findField(cn, name, null, optional);
    }

    public static FieldNode findField(ClassNode cn, String name) {
        return findField(cn, name, null, false);
    }

    public static FieldNode findField(ClassNode cn, Name name) {
        return findField(cn, name, false);
    }

    public static void makePublic(MethodNode x) {
        x.access = (x.access & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
    }

    public static void makePublic(FieldNode x) {
        x.access = (x.access & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
    }

    public static boolean transformIntConst(ClassNode cn, String method, int oldValue, int newValue) {
        return transformIntConst(cn, findMethod(cn, method), oldValue, newValue, false);
    }

    public static boolean transformIntConst(ClassNode cn, Name method, int oldValue, int newValue) {
        return transformIntConst(cn, findMethod(cn, method), oldValue, newValue, false);
    }

    public static boolean transformIntConst(ClassNode cn, MethodNode method, int oldValue, int newValue) {
        return transformIntConst(cn, method, oldValue, newValue, false);
    }

    static void setIntConst(InsnList il, AbstractInsnNode loc, int cst) {
        AbstractInsnNode n;
        if (cst >= -1 && cst <= 5) n = new InsnNode(ICONST_0 + cst);
        else if (cst >= Byte.MIN_VALUE && cst <= Byte.MAX_VALUE) n = new IntInsnNode(Opcodes.BIPUSH, cst);
        else if (cst >= Short.MIN_VALUE && cst <= Short.MAX_VALUE) n = new IntInsnNode(Opcodes.SIPUSH, cst);
        else n = new LdcInsnNode(new Integer(cst));
        il.set(loc, n);
    }

    public static boolean transformIntConst(
            ClassNode cn, MethodNode method, int oldValue, int newValue, boolean optional) {
        int occur = 0;
        InsnList il = method.instructions;

        for (ListIterator<AbstractInsnNode> it = il.iterator(); it.hasNext(); ) {
            AbstractInsnNode insn = it.next();

            if (insn.getOpcode() >= ICONST_M1
                    && insn.getOpcode() <= ICONST_5
                    && oldValue == insn.getOpcode() - ICONST_0) setIntConst(il, insn, newValue);
            else if ((insn.getOpcode() == SIPUSH || insn.getOpcode() == BIPUSH)
                    && ((IntInsnNode) insn).operand == oldValue) setIntConst(il, insn, newValue);
            else if (insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst.equals(oldValue))
                setIntConst(il, insn, newValue);
            else continue;

            occur++;
        }

        if (occur == 0 && !optional)
            throw new AsmTransformException("can't find constant value " + oldValue + " in method " + method.name);

        return occur > 0;
    }

    enum Action {
        CONTINUE,
        FOUND_RETURN,
        FOUND_CONTINUE
    }

    public abstract static class InsnNodeSearcher {
        abstract Action test(AbstractInsnNode insn);
    }

    public static AbstractInsnNode find(AbstractInsnNode begin, InsnNodeSearcher s) {
        AbstractInsnNode res = null;
        for (AbstractInsnNode insn = begin; insn.getNext() != null; insn = insn.getNext()) {
            Action a = s.test(insn);
            if (a == Action.FOUND_RETURN) return insn;
            if (a == Action.FOUND_CONTINUE) res = insn;
        }
        return res;
    }

    public static MethodInsnNode findMethodInsnNode(AbstractInsnNode begin, final Name name) {
        return (MethodInsnNode) find(begin, new InsnNodeSearcher() {
            @Override
            Action test(AbstractInsnNode insn) {
                if (insn.getType() == AbstractInsnNode.METHOD_INSN && name.matches((MethodInsnNode) insn))
                    return Action.FOUND_RETURN;
                return Action.CONTINUE;
            }
        });
    }

    public static VarInsnNode findVarInsnNode(AbstractInsnNode begin, final int opcode, final int var) {
        return (VarInsnNode) find(begin, new InsnNodeSearcher() {
            @Override
            Action test(AbstractInsnNode insn) {
                if (insn.getType() == AbstractInsnNode.VAR_INSN
                        && insn.getOpcode() == opcode
                        && ((VarInsnNode) insn).var == var) return Action.FOUND_RETURN;
                return Action.CONTINUE;
            }
        });
    }

    public static IntInsnNode findIntInsnNode(AbstractInsnNode begin, final int val) {
        return (IntInsnNode) find(begin, new InsnNodeSearcher() {
            @Override
            Action test(AbstractInsnNode insn) {
                if (insn.getType() == AbstractInsnNode.INT_INSN && ((IntInsnNode) insn).operand == val)
                    return Action.FOUND_RETURN;
                return Action.CONTINUE;
            }
        });
    }

    public static MethodInsnNode findMethodInsnNode(AbstractInsnNode begin, final String name, final String desc) {
        return (MethodInsnNode) find(begin, new InsnNodeSearcher() {
            @Override
            Action test(AbstractInsnNode insn) {
                if (insn.getType() == AbstractInsnNode.METHOD_INSN
                        && ((MethodInsnNode) insn).name.equals(name)
                        && (desc == null || ((MethodInsnNode) insn).desc.equals(desc))) return Action.FOUND_RETURN;
                return Action.CONTINUE;
            }
        });
    }

    public static InsnNode findInsnNode(AbstractInsnNode begin, final int opcode) {
        return (InsnNode) find(begin, new InsnNodeSearcher() {
            @Override
            Action test(AbstractInsnNode insn) {
                if (insn.getOpcode() == opcode) return Action.FOUND_RETURN;
                return Action.CONTINUE;
            }
        });
    }

    public static void removeRange(InsnList il, AbstractInsnNode begIn, AbstractInsnNode endEx) {
        ListIterator<AbstractInsnNode> it = il.iterator(il.indexOf(begIn));
        if (it.hasNext()) { // Bug?
            it.next();
            it.previous();
        } else if (it.hasPrevious()) {
            it.previous();
            it.next();
        } else throw new RuntimeException();
        for (; begIn != endEx; begIn = it.next()) {
            it.remove();
        }
    }

    public static AbstractInsnNode getRelative(AbstractInsnNode insn, int way) {
        while (way != 0) {
            if (way > 0) insn = insn.getNext();
            else insn = insn.getPrevious();
            way -= way / Math.abs(way);
        }
        return insn;
    }

    public static void dump(InsnList list) {
        Textifier textifier = new Textifier();
        TraceMethodVisitor visitor = new TraceMethodVisitor(textifier);
        list.accept(visitor);

        PrintWriter writer = new PrintWriter(System.out);
        textifier.print(writer);
        writer.flush();
    }
}
