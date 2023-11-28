package com.gtnewhorizons.neid.asm;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class AsmUtil implements Opcodes {

    public static MethodNode findMethod(final ClassNode cn, final String name) {
        return findMethod(cn, name, false);
    }

    public static MethodNode findMethod(final ClassNode cn, final String name, final boolean optional) {
        for (final MethodNode methodNode : cn.methods) {
            if (methodNode.name.equals(name)) {
                return methodNode;
            }
        }
        if (optional) {
            return null;
        }
        throw new MethodNotFoundException(name);
    }

    public static MethodNode findMethod(final ClassNode cn, final Name name) {
        return findMethod(cn, name, false);
    }

    public static MethodNode findMethod(final ClassNode cn, final Name name, final boolean optional) {
        for (final MethodNode methodNode : cn.methods) {
            if (name.matches(methodNode)) {
                return methodNode;
            }
        }
        if (optional) {
            return null;
        }
        throw new MethodNotFoundException(name.deobf);
    }

    public static FieldNode findField(final ClassNode cn, final String name, final boolean optional) {
        for (final FieldNode fieldNode : cn.fields) {
            if (name.equals(fieldNode.name)) {
                return fieldNode;
            }
        }
        if (optional) {
            return null;
        }
        throw new FieldNotFoundException(name);
    }

    public static void modifyIntConstantInMethod(final MethodNode method, final int oldValue, final int newValue) {
        modifyIntConstantInMethod(method, oldValue, newValue, false);
    }

    public static boolean modifyIntConstantInMethod(final MethodNode method, final int oldValue, final int newValue,
            final boolean optional) {
        boolean found = false;
        boolean foundOnce = false;
        final ListIterator<AbstractInsnNode> it = method.instructions.iterator();
        while (it.hasNext()) {
            final AbstractInsnNode insn = it.next();
            if (insn.getOpcode() == ICONST_0 && oldValue == 0) {
                found = true;
            } else if (insn.getOpcode() == ICONST_1 && oldValue == 1) {
                found = true;
            } else if (insn.getOpcode() == ICONST_2 && oldValue == 2) {
                found = true;
            } else if (insn.getOpcode() == ICONST_3 && oldValue == 3) {
                found = true;
            } else if (insn.getOpcode() == ICONST_4 && oldValue == 4) {
                found = true;
            } else if (insn.getOpcode() == ICONST_5 && oldValue == 5) {
                found = true;
            } else if (insn.getOpcode() == LDC) {
                final LdcInsnNode node = (LdcInsnNode) insn;
                if (node.cst instanceof Integer && (int) node.cst == oldValue) {
                    found = true;
                }
            } else if (insn.getOpcode() == SIPUSH || insn.getOpcode() == BIPUSH) {
                final IntInsnNode node2 = (IntInsnNode) insn;
                if (node2.operand == oldValue) {
                    found = true;
                }
            }
            if (found) {
                foundOnce = true;
                if (newValue == 0) {
                    it.set(new InsnNode(ICONST_0));
                } else if (newValue == 1) {
                    it.set(new InsnNode(ICONST_1));
                } else if (newValue == 2) {
                    it.set(new InsnNode(ICONST_2));
                } else if (newValue == 3) {
                    it.set(new InsnNode(ICONST_3));
                } else if (newValue == 4) {
                    it.set(new InsnNode(ICONST_4));
                } else if (newValue == 5) {
                    it.set(new InsnNode(ICONST_5));
                } else if (newValue >= -128 && newValue <= 127) {
                    it.set(new IntInsnNode(BIPUSH, newValue));
                } else if (newValue >= -32768 && newValue <= 32767) {
                    it.set(new IntInsnNode(SIPUSH, newValue));
                } else {
                    it.set(new LdcInsnNode(newValue));
                }
                found = false;
            }
        }
        if (!foundOnce && !optional) {
            throw new AsmTransformException("can't find constant value " + oldValue + " in method " + method.name);
        }
        return foundOnce;
    }

}
