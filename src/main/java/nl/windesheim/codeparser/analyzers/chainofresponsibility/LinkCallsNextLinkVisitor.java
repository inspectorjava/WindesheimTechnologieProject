package nl.windesheim.codeparser.analyzers.chainofresponsibility;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if a 'link' classes a next 'link'.
 */
public class LinkCallsNextLinkVisitor extends GenericVisitorAdapter<Boolean, ClassOrInterfaceDeclaration> {

    @Override
    public Boolean visit(final MethodCallExpr callExpr, final ClassOrInterfaceDeclaration commonParent) {
        //Call the visit recursion
        Boolean superResult = super.visit(callExpr, commonParent);
        if (superResult != null) {
            return superResult;
        }

        Expression scope = getMethodCallScope(callExpr);

        if (scope == null) {
            return null;
        }

        //If the object scope wants to access a field in the class
        if (scope instanceof FieldAccessExpr) {
            FieldAccessExpr fieldAccess = (FieldAccessExpr) scope;
            //If the method doesn't want to access a variable in this class it is not a call to a next 'link'
            if (!(fieldAccess.getScope() instanceof ThisExpr)) {
                return null;
            }

            //Check if the variable is a reverence to a next chain
            if (isReferenceToNextLink(fieldAccess.getName(), callExpr, commonParent)) {
                return true;
            }

            //If the object scope is just a variable it can also be from the class scope
        } else if (scope instanceof NameExpr) {
            NameExpr nameExpr = (NameExpr) scope;

            //Check if the variable is a reverence to a next chain
            if (isReferenceToNextLink(nameExpr.getName(), callExpr, commonParent)) {
                return true;
            }
        }

        return null;
    }

    /**
     * @param callExpr the method call expression for which to get the scope
     * @return the scope of the method call expression
     */
    private Expression getMethodCallScope(final MethodCallExpr callExpr) {
        //Find the method deceleration surrounding the call
        Node currentNode = callExpr;
        while (!(currentNode instanceof MethodDeclaration)) {
            if (!currentNode.getParentNode().isPresent()) {
                break;
            }
            currentNode = currentNode.getParentNode().get();
        }

        //If the call expression is not within a method we can continue
        if (!(currentNode instanceof MethodDeclaration)) {
            return null;
        }

        MethodDeclaration thisMethod = (MethodDeclaration) currentNode;

        //If the method that is called is not the same as the current method we can continue
        if (!thisMethod.getName().equals(callExpr.getName())) {
            return null;
        }

        //If the method call is not on a object. So the call is not to a next link
        if (!callExpr.getScope().isPresent()) {
            return null;
        }

        return callExpr.getScope().get();
    }

    /**
     * Check if a variable name is a next 'link' object.
     *
     * @param name         the name of the variable to check
     * @param callExpr     the call expression which calls the variable
     * @param commonParent the common parent of the 'link' in which the call expression is located
     * @return true/false
     */
    private boolean isReferenceToNextLink(final SimpleName name, final MethodCallExpr callExpr,
                                          final ClassOrInterfaceDeclaration commonParent) {
        //Get the 'link' class
        Node currentNode = callExpr;
        while (!(currentNode instanceof ClassOrInterfaceDeclaration)) {
            if (!currentNode.getParentNode().isPresent()) {
                break;
            }
            currentNode = currentNode.getParentNode().get();
        }

        if (!(currentNode instanceof ClassOrInterfaceDeclaration)) {
            return false;
        }
        ClassOrInterfaceDeclaration containingClass = (ClassOrInterfaceDeclaration) currentNode;

        //Get te possible fields
        List<FieldDeclaration> fields = findFieldDeclarations(containingClass);

        //for each possible next 'link' field
        for (FieldDeclaration field : fields) {
            //for each variable in the field declaration
            for (VariableDeclarator variable : field.getVariables()) {
                //if the variable type is not a class or interface it can't be the 'common parent'
                Type variableType = variable.getType();
                if (!(variableType instanceof ClassOrInterfaceType)) {
                    continue;
                }

                //If the variable type name is not equal to the 'common parent' name it isn't the next 'link'
                if (!((ClassOrInterfaceType) variableType).getName().equals(commonParent.getName())) {
                    continue;
                }

                //If the variable name is equal to the name which is called in the method call expression
                // the link calls the next 'link'
                if (variable.getName().equals(name)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param linkClass the 'link' class
     * @return a list of field declarations
     */
    private List<FieldDeclaration> findFieldDeclarations(final ClassOrInterfaceDeclaration linkClass) {
        //Initialize a list of fields which can be next 'links'
        List<FieldDeclaration> fields = new ArrayList<>();

        //Add all fields the 'link' class
        fields.addAll(linkClass.findAll(FieldDeclaration.class));

        //For each superclass of the 'link'
        for (ClassOrInterfaceType superClass : linkClass.getExtendedTypes()) {
            //Find the ClassOrInterfaceDeclaration of the superclass
            ResolvedReferenceType resolved = superClass.resolve();
            if (!(resolved instanceof ReferenceTypeImpl)) {
                continue;
            }

            ResolvedReferenceTypeDeclaration typeDeclaration = resolved.getTypeDeclaration();

            if (!(typeDeclaration instanceof JavaParserClassDeclaration)) {
                continue;
            }

            ClassOrInterfaceDeclaration typeClass = ((JavaParserClassDeclaration) typeDeclaration).getWrappedNode();

            //For each non-private field in the superclass
            // add the field to the list of possible next 'link' fields
            for (FieldDeclaration field : typeClass.getFields()) {
                if (!field.getModifiers().contains(Modifier.PRIVATE)) {
                    fields.add(field);
                }
            }
        }

        return fields;
    }
}
