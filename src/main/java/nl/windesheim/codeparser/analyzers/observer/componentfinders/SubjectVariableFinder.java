package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import nl.windesheim.codeparser.analyzers.observer.components.*;

import java.util.List;

public class SubjectVariableFinder {
    public VariableDeclarator findSubjectVariable(
            final ObserverClass observer,
            final List<SubjectClass> subjects
    ) {
        List<FieldDeclaration> fields = observer.getClassDeclaration().getFields();

        // Check whether a field variable type matches the type of the abstract subject
        for (FieldDeclaration field : fields) {
            for (VariableDeclarator variableDecl : field.getVariables()) {
                ResolvedType variableType = variableDecl.getType().resolve();

                if (variableType.isReferenceType()) {
                    ResolvedReferenceTypeDeclaration variableTypeDecl = variableType.asReferenceType().getTypeDeclaration();

                    for (SubjectClass subject : subjects) {
                        if (variableTypeDecl.equals(subject.getResolvedTypeDeclaration())) {
                            return variableDecl;
                        }
                    }
                }
            }
        }

        return null;
    }
}
