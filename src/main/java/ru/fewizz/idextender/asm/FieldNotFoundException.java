package ru.fewizz.idextender.asm;

public class FieldNotFoundException extends AsmTransformException {

    public FieldNotFoundException(final String field) {
        super("can't find field " + field);
    }
}
