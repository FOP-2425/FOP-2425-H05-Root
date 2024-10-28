package h05.transform;

import kotlin.Pair;
import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.testing.ClassTransformer;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SolutionMergingClassTransformer implements ClassTransformer {

    private final String projectPrefix;
    private final List<String> availableSolutionClasses;

    public SolutionMergingClassTransformer(String projectPrefix, List<String> availableSolutionClasses) {
        this.projectPrefix = projectPrefix;
        this.availableSolutionClasses = availableSolutionClasses.stream()
            .map(s -> s.replace('.', '/'))
            .toList();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        String submissionClassName = reader.getClassName();
        String solutionClassName;
        ForceSignatureAnnotationProcessor forceSignatureAnnotationProcessor = new ForceSignatureAnnotationProcessor(reader);
        if (forceSignatureAnnotationProcessor.classIdentifierIsForced()) {
            solutionClassName = forceSignatureAnnotationProcessor.forcedClassIdentifier();
        } else {
            // If not forced, get the closest matching solution class (at least 90% similarity)
            solutionClassName = availableSolutionClasses.stream()
                .map(s -> new Pair<>(s, MatchingUtils.similarity(submissionClassName, s)))
                .filter(pair -> pair.getSecond() >= 0.90)
                .max(Comparator.comparing(Pair::getSecond))
                .map(Pair::getFirst)
                .orElse(null);
        }

        // If no matching solution class was found, skip merge
        if (solutionClassName == null) {
            System.err.printf("No corresponding solution class found for %s. Only applying default transformations.%n", submissionClassName);
            reader.accept(new SubmissionClassVisitor(writer, projectPrefix, submissionClassName), 0);
        } else {
            ClassReader solutionClassReader;
            String solutionClassFilePath = "/classes/%s.bin".formatted(solutionClassName);
            try (InputStream is = getClass().getResourceAsStream(solutionClassFilePath)) {
                if (is == null) {
                    throw new IOException("No such resource: " + solutionClassFilePath);
                }
                solutionClassReader = new ClassReader(is.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SolutionClassNode solutionClassNode = new SolutionClassNode(solutionClassName);
            solutionClassReader.accept(solutionClassNode, 0);
            reader.accept(new SubmissionClassVisitor(writer, projectPrefix, solutionClassName, forceSignatureAnnotationProcessor, solutionClassNode), 0);
        }
    }
}
