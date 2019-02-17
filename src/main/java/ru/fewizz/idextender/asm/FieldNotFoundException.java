package ru.fewizz.idextender.asm;

public class FieldNotFoundException extends AsmTransformException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FieldNotFoundException(String field) {
		super("can't find field " + field);
	}
}
