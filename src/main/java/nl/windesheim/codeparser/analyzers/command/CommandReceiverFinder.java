package nl.windesheim.codeparser.analyzers.command;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Finder for the command receivers inside a command class.
 */
public class CommandReceiverFinder extends VoidVisitorAdapter<ClassOrInterfaceDeclaration> {

    /**
     * The context, interface pairs found in the last visit.
     */
    private final List<ClassOrInterfaceDeclaration> classes = new ArrayList<>();

    /**
     * A list of errors which were encountered.
     */
    private final List<Exception> errors = new ArrayList<>();

    /**
     * Type solver.
     */
    private CombinedTypeSolver typeSolver;

    /**
     * @return A list of context, interface pairs which were found in the last visit
     */
    public List<ClassOrInterfaceDeclaration> getClasses() {
        return classes;
    }

    /**
     * @return a list of errors which were encountered
     */
    public List<Exception> getErrors() {
        return errors;
    }

    /**
     * Set the type solver.
     *
     * @param typeSolver Current type solver.
     */
    public void setTypeSolver(final CombinedTypeSolver typeSolver) {
        this.typeSolver = typeSolver;
    }

    /**
     * Reset the list of classes.
     */
    public void reset() {
        errors.clear();
        classes.clear();
    }

    @Override
    public void visit(final MethodCallExpr methodCall, final ClassOrInterfaceDeclaration commandDefinition) {
        super.visit(methodCall, commandDefinition);

        // Without a type solver the command analyzer can't function.
        if (typeSolver == null) {
            return;
        }

        Node node = methodCall;
        while (!(node instanceof MethodDeclaration)) {
            if (!node.getParentNode().isPresent()) {
                return;
            }
            node = node.getParentNode().get();
        }
        MethodDeclaration methodDeclaration = (MethodDeclaration) node;

        if (!methodExitsInDefinition(methodDeclaration, commandDefinition)
                || !methodCall.getScope().isPresent()
                || !methodReturnsVoid(methodDeclaration)
                || methodIsGetterOrSetter(methodDeclaration)) {

            return;
        }

        ResolvedType resolvedType;

        try {
            JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(typeSolver);
            resolvedType = javaSymbolSolver.calculateType(methodCall.getScope().get());
        } catch (UnsolvedSymbolException exception) {
            errors.add(exception);
            return;
        }

        // Pass only when the type is an referenced type.
        if (!(resolvedType instanceof ResolvedReferenceType)) {
            return;
        }

        ResolvedReferenceTypeDeclaration referenceType = ((ResolvedReferenceType) resolvedType).getTypeDeclaration();
        // Resolve only classes and exclude interfaces.
        if (!(referenceType instanceof JavaParserClassDeclaration)) {
            return;
        }

        ClassOrInterfaceDeclaration receiverClass = ((JavaParserClassDeclaration) referenceType).getWrappedNode();

        classes.add(receiverClass);
    }

    /**
     * Check if the called method exists in the command definition.
     *
     * @param methodDeclaration Delectation of the current method.
     * @param commandDefinition Definition of the command pattern.
     * @return If the method is defined in the command pattern.
     */
    private boolean methodExitsInDefinition(
            final MethodDeclaration methodDeclaration,
            final ClassOrInterfaceDeclaration commandDefinition
    ) {
        for (MethodDeclaration methodDefinition : commandDefinition.getMethods()) {

            // The current method exists in the command declaration.
            if (methodDefinition.getNameAsString().equals(methodDeclaration.getNameAsString())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the method return type is void.
     *
     * @param methodDeclaration Delectation of the current method.
     * @return If the method return type is void.
     */
    private boolean methodReturnsVoid(
            final MethodDeclaration methodDeclaration
    ) {
        if (methodDeclaration.getType().toString().equals("void")) {
            return true;
        }

        return false;
    }

    /**
     * Check if the method name contains not the words getter or setter.
     *
     * @param methodDeclaration Delectation of the current method.
     * @return If the method is an getter or setter.
     */
    private boolean methodIsGetterOrSetter(
            final MethodDeclaration methodDeclaration
    ) {
        String methodName = methodDeclaration.getNameAsString().toLowerCase();
        // When the method contains get or set it is certain getter or setter.
        if (methodName.contains("get") || methodName.contains("set")) {
            return true;
        }

        return false;
    }
}
