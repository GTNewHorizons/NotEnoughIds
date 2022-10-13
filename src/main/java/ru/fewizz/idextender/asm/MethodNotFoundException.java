package ru.fewizz.idextender.asm;

public class MethodNotFoundException extends AsmTransformException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MethodNotFoundException(String method) {
        super("can't find method " + method);
    }
}
