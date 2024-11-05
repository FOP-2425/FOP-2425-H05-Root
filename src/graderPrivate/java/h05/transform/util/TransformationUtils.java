package h05.transform.util;

import h05.transform.SolutionClassNode;
import h05.transform.SolutionMergingClassTransformer;
import h05.transform.SubmissionClassInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;

import static org.objectweb.asm.Opcodes.*;

/**
 * A collection of utility methods useful for bytecode transformations.
 * @author Daniel Mangold
 */
public final class TransformationUtils {

    private TransformationUtils() {}

    /**
     * Automatically box primitive types using the supplied {@link MethodVisitor}.
     * If the given type is not a primitive type, this method does nothing.
     *
     * @param mv   the {@link MethodVisitor} to use
     * @param type the type of the value
     */
    public static void boxType(MethodVisitor mv, Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
            case Type.BYTE -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
            case Type.SHORT -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
            case Type.CHAR -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
            case Type.INT -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            case Type.FLOAT -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
            case Type.LONG -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
            case Type.DOUBLE -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
        }
    }

    /**
     * Automatically unbox primitive types using the supplied {@link MethodVisitor}.
     * If the given type is not a primitive type, this method does nothing.
     *
     * @param mv   the {@link MethodVisitor} to use
     * @param type the type of the value
     */
    public static void unboxType(MethodVisitor mv, Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN -> {
                mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
            }
            case Type.BYTE -> {
                mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
            }
            case Type.SHORT -> {
                mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
            }
            case Type.CHAR -> {
                mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
            }
            case Type.INT -> {
                mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
            }
            case Type.FLOAT -> {
                mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
            }
            case Type.LONG -> {
                mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
            }
            case Type.DOUBLE -> {
                mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
            }
        }
    }

    /**
     * Calculates the true index of variables in the locals array.
     * Variables with type long or double occupy two slots in the locals array,
     * so the "expected" or "natural" index of these variables might be shifted.
     *
     * @param types the parameter types
     * @param index the "natural" index of the variable
     * @return the true index
     */
    public static int getLocalsIndex(Type[] types, int index) {
        int localsIndex = 0;
        for (int i = 0; i < index; i++) {
            localsIndex += (types[i].getSort() == Type.LONG || types[i].getSort() == Type.DOUBLE) ? 2 : 1;
        }
        return localsIndex;
    }

    /**
     * Attempts to read and process a solution class from {@code resources/classes/}.
     *
     * @param className the name of the solution class
     * @return the resulting {@link SolutionClassNode} object
     */
    public static SolutionClassNode readSolutionClass(String className) {
        ClassReader solutionClassReader;
        String solutionClassFilePath = "/classes/%s.bin".formatted(className);
        try (InputStream is = SolutionMergingClassTransformer.class.getResourceAsStream(solutionClassFilePath)) {
            if (is == null) {
                throw new IOException("No such resource: " + solutionClassFilePath);
            }
            solutionClassReader = new ClassReader(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SolutionClassNode solutionClassNode = new SolutionClassNode(className);
        solutionClassReader.accept(solutionClassNode, 0);
        return solutionClassNode;
    }

    /**
     * Attempts to read and process a submission class.
     *
     * @param transformationContext a {@link TransformationContext} object
     * @param className             the name of the submission class
     * @return the resulting {@link SubmissionClassInfo} object
     */
    public static SubmissionClassInfo readSubmissionClass(TransformationContext transformationContext, String className) {
        ClassReader submissionClassReader;
        String submissionClassFilePath = "/%s.class".formatted(className);
        try (InputStream is = SolutionMergingClassTransformer.class.getResourceAsStream(submissionClassFilePath)) {
            submissionClassReader = new ClassReader(is);
        } catch (IOException e) {
            return null;
        }
        SubmissionClassInfo submissionClassInfo = new SubmissionClassInfo(
            transformationContext,
            submissionClassReader.getClassName(),
            new ForceSignatureAnnotationProcessor(submissionClassReader)
        );
        submissionClassReader.accept(submissionClassInfo, 0);
        return submissionClassInfo;
    }
}
