package nl.windesheim.codeparser.analyzers.composite;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import nl.windesheim.codeparser.ClassOrInterface;


import java.util.ArrayList;
import java.util.List;

/**
 * Find Self referring list declarations.
 */
public class FindSelfReferringListDeclaration extends VoidVisitorAdapter<ClassOrInterfaceDeclaration> {


    /**
     * field declerations
     */
    private List<FieldDeclaration> fieldDeclerations;

    /**
     * Default constructor.
     */
    public FindSelfReferringListDeclaration() {
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

            //if (resolvedListType instanceof ) {

            //}
            //variableDeclaration.getType().resolve().equals()


        }


        this.fieldDeclerations.add(fieldDeclaration);
    }

    /**
     * Get field declarations.
     * @return
     */
    public List<FieldDeclaration> getFieldDeclerations() {
        return fieldDeclerations;
    }
}
