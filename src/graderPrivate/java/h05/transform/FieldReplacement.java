package h05.transform;

import org.objectweb.asm.Type;

public class FieldReplacement<T> {

    public static final String INTERNAL_NAME = Type.getInternalName(FieldReplacement.class);
    public static final String INTERNAL_DESCRIPTOR = "L" + INTERNAL_NAME + ";";

    private final int originalModifiers;
    private final String originalDescriptor;
    private final String originalSignature;
    private T value;

    public FieldReplacement(T initialValue) {
        this(0, null, null, initialValue);
    }

    public FieldReplacement(int originalModifiers, String originalDescriptor, String originalSignature) {
        this(originalModifiers, originalDescriptor, originalSignature, null);
    }

    public FieldReplacement(int originalModifiers, String originalDescriptor, String originalSignature, T initialValue) {
        this.originalModifiers = originalModifiers;
        this.originalDescriptor = originalDescriptor;
        this.originalSignature = originalSignature;
        this.value = initialValue;
    }

    public int getOriginalModifiers() {
        return originalModifiers;
    }

    public String getOriginalDescriptor() {
        return originalDescriptor;
    }

    public String getOriginalSignature() {
        return originalSignature;
    }

    /**
     * Returns the current value of this field.
     *
     * <p>
     * If the current value is {@code null}, the default value according to
     * <a href="https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.12.5">Chapter 4.12.5</a>
     * of the JLS is returned.
     * </p>
     *
     * @return the current value of this field
     */
    @SuppressWarnings("unchecked")
    public T get() {
        return (T) switch (originalDescriptor) {
            case "Z" -> false;
            case "B", "S", "C", "I" -> 0;
            case "F" -> 0F;
            case "J" -> 0L;
            case "D" -> 0D;
            default -> value;
        };
    }

    /**
     * Sets the current value of this field to the specified one.
     *
     * @param value the new value for this field
     */
    public void set(T value) {
        this.value = value;
    }

    record FieldHeader(int access, String name, String descriptor, String signature) {}
}
