package ru.fewizz.idextender.asm;

import org.objectweb.asm.tree.ClassNode;

public interface IClassNodeTransformer {

    void transform(final ClassNode p0, final boolean p1);
}
