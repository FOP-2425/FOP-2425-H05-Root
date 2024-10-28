package transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Forces the annotated type or member to be mapped to the specified one.
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface ForceSignature {

    String INTERNAL_NAME = ForceSignature.class.getCanonicalName().replace('.', '/');
    String INTERNAL_DESCRIPTOR = "L" + INTERNAL_NAME + ";";

    /**
     * The identifier of the annotated type / member.
     * The value must be as follows:
     * <ul>
     *     <li>Types: the fully qualified name of the type (e.g., {@code java.lang.Object}</li>
     *     <li>Fields: the name / identifier of the field</li>
     *     <li>Constructors: Always {@code <init>}, regardless of the class</li>
     *     <li>Methods: the name / identifier of the method</li>
     * </ul>
     *
     * @return the type / member identifier
     */
    String identifier();

    /**
     * The method descriptor as specified by
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.3">Chapter 4.3</a>
     * of the Java Virtual Machine Specification.
     * <br>
     * Note: This has no effect for types or fields.
     *
     * @return an array of type names matching the annotated method's parameter types
     */
    String descriptor() default "";
}