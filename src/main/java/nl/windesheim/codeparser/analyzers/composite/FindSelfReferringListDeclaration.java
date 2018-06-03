package nl.windesheim.codeparser.analyzers.composite;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Find Self referring list declarations.
 */
public class FindSelfReferringListDeclaration extends VoidVisitorAdapter<ClassOrInterfaceDeclaration> {

    /**
     * A list of errors which were encountered.
     */
    private final List<Exception> errors = new ArrayList<>();

    /**
     * field declarations.
     */
    private final List<FieldDeclaration> fieldDeclarations = new ArrayList<>();

    @Override
    public void visit(final FieldDeclaration fieldDeclaration, final ClassOrInterfaceDeclaration compositeClass) {
        super.visit(fieldDeclaration, compositeClass);

        // Now we resolve some stuff
        // Sub type of lists and generic type of sub class
        for (VariableDeclarator varDeclaration : fieldDeclaration.getVariables()) {

            ClassOrInterfaceType interfaceType = getListType(varDeclaration);

            if (interfaceType == null) {
                continue;
            }

            Type type = interfaceType.getTypeArguments().get().get(0);
            ResolvedType resolvedListType;
            try {
                resolvedListType = type.resolve();
            } catch (UnsolvedSymbolException exception) {
                continue;
            }

            if (!(resolvedListType instanceof ReferenceTypeImpl)) {
                continue;
            }

            ResolvedReferenceTypeDeclaration refTypeDecl;
            try {
                refTypeDecl = ((ReferenceTypeImpl) type.resolve())
                        .getTypeDeclaration();
            } catch (UnsolvedSymbolException exception) {
                continue;
            }

            if (!(refTypeDecl instanceof JavaParserInterfaceDeclaration)) {
                continue;
            }

            if (!((JavaParserInterfaceDeclaration) refTypeDecl).getWrappedNode().equals(compositeClass)) {
                continue;
            }

            this.fieldDeclarations.add(fieldDeclaration);
        }
    }

    /**
     * @param varDeclaration variable declaraion
     * @return list type
     */
    private ClassOrInterfaceType getListType(final VariableDeclarator varDeclaration) {
        ResolvedType resolvedType;

        try {
            resolvedType = varDeclaration.getType().resolve();
        } catch (UnsolvedSymbolException exception) {
            return null;
        }

        if (!resolvedType.isReferenceType()) {
            return null;
        }

        // Get type of property
        ResolvedReferenceTypeDeclaration typeDeclaration
                = ((ResolvedReferenceType) resolvedType).getTypeDeclaration();

        // If the type of property is of List type
        if (!(typeDeclaration.getQualifiedName().equals(List.class.getName()))) {
            return null;
        }

        if (!(varDeclaration.getType() instanceof ClassOrInterfaceType)) {
            return null;
        }

        ClassOrInterfaceType interfaceType = (ClassOrInterfaceType) varDeclaration.getType();
        if (!interfaceType.getTypeArguments().isPresent()) {
            return null;
        }

        return interfaceType;
    }

    /**
     * Resets the field declarations list.
     */
    public void reset() {
        fieldDeclarations.clear();
        errors.clear();
    }

    /**
     * @return a list of encountered errors
     */
    public List<Exception> getErrors() {
        return errors;
    }

    /**
     * Get field declarations.
     *
     * @return list of fielddeclarations
     */
    public List<FieldDeclaration> getFieldDeclerations() {
        return fieldDeclarations;
    }
}
