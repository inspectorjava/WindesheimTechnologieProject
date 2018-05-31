package nl.windesheim.codeparser.analyzers.composite;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Find Self referring list declarations.
 */
public class FindSelfReferringListDeclaration extends VoidVisitorAdapter<ClassOrInterfaceDeclaration> {


    /**
     * field declerations.
     */
    private List<FieldDeclaration> fieldDeclerations;

    /**
     * Default constructor.
     */
    FindSelfReferringListDeclaration() {
        this.fieldDeclerations = new ArrayList<>();
    }

    @Override
    public void visit(final FieldDeclaration fieldDeclaration, final ClassOrInterfaceDeclaration compositeClass) {
        super.visit(fieldDeclaration, compositeClass);

        // Now we resolve some stuff


        // Sub type of lists and generic type of sub class

        for (VariableDeclarator variableDeclaration : fieldDeclaration.getVariables()) {
            ResolvedType resolvedType = variableDeclaration.getType().resolve();
            if (!resolvedType.isReferenceType()) {
                continue;
            }

            // Get type of property
            ResolvedReferenceTypeDeclaration typeDeclaration
                    = ((ResolvedReferenceType) resolvedType).getTypeDeclaration();

            // If the type of property is of List type
            if (!(typeDeclaration.getQualifiedName().equals(List.class.getName()))) {
                continue;
            }

            if (!(variableDeclaration.getType() instanceof ClassOrInterfaceType)) {
                continue;
            }

            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) variableDeclaration.getType();
            if (!classOrInterfaceType.getTypeArguments().isPresent()) {
                continue;
            }


            Type type = classOrInterfaceType.getTypeArguments().get().get(0);
            ResolvedType resolvedListType = type.resolve();

            // If type of list e.g. List<Foo> == compositeClass we found the right list
            String listType = ((ReferenceTypeImpl) resolvedListType).getQualifiedName();
            String conName = compositeClass.getName().toString();


            // Check if it isnt and return if possible
            if (!listType.equals(conName)) {
                continue;
            }

            this.fieldDeclerations.add(fieldDeclaration);
        }
    }

    /**
     * Get field declarations.
     * @return
     */
    List<FieldDeclaration> getFieldDeclerations() {
        return fieldDeclerations;
    }
}
