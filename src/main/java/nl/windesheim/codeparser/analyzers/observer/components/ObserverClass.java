package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

/**
 * Encapsulates information on an observer class.
 */
public class ObserverClass extends EligibleObserverComponent {
    /**
     * The declaration of the update method.
     */
    private ResolvedMethodDeclaration updateMethod;

    /**
     * Whether the class has a field containing a reference to a Subject.
     */
    private VariableDeclarator subjectVar;

    /**
     * Whether the class calls the attach method.
     */
    private boolean hasAttachStmt;

    /**
     * Whether the class calls the detach method.
     */
    private boolean hasDetachStmt;

    /**
     * ObserverClass constructor.
     *
     * @param classDeclaration The class or interface defining the observer
     */
    public ObserverClass(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    /**
     * ObserverClass constructor.
     *
     * @param classDeclaration The class or interface defining the observer
     * @param resolvedType     The type of the subject class
     */
    public ObserverClass(
            final ClassOrInterfaceDeclaration classDeclaration,
            final ResolvedReferenceTypeDeclaration resolvedType
    ) {
        this(classDeclaration, resolvedType, null);
    }

    /**
     * ObserverClass constructor.
     *
     * @param classDeclaration The class or interface defining the observer
     * @param resolvedType     The type of the observer class
     * @param updateMethod     The declaration of the update method
     */
    public ObserverClass(final ClassOrInterfaceDeclaration classDeclaration,
                            final ResolvedReferenceTypeDeclaration resolvedType,
                            final ResolvedMethodDeclaration updateMethod) {
        super(classDeclaration, resolvedType);
        this.updateMethod = updateMethod;

        this.subjectVar = null;
        this.hasAttachStmt = false;
        this.hasDetachStmt = false;
    }

    /**
     * @return The declaration of the update method associated with the abstract observer
     */
    public ResolvedMethodDeclaration getUpdateMethod() {
        return updateMethod;
    }

    /**
     * @param updateMethod The declaration of the update method associated with the abstract observer
     */
    public void setUpdateMethod(final ResolvedMethodDeclaration updateMethod) {
        this.updateMethod = updateMethod;
    }

    /**
     * @return VariableDeclarator which refers to a Subject
     */
    public VariableDeclarator getSubjectVariable() {
        return subjectVar;
    }

    /**
     * @param abstSubjectVar VariableDeclarator which refers to a Subject
     */
    public void setSubjectVariable(final VariableDeclarator abstSubjectVar) {
        this.subjectVar = abstSubjectVar;
    }

    /**
     * @return Whether the class contains a call to an attach method
     */
    public boolean isHasAttachStatement() {
        return hasAttachStmt;
    }

    /**
     * @param hasAttachStmt Whether the class contains a call to an attach method
     */
    public void setHasAttachStatement(final boolean hasAttachStmt) {
        this.hasAttachStmt = hasAttachStmt;
    }

    /**
     * @return Whether the class contains a call to a detach method
     */
    public boolean isHasDetachStatement() {
        return hasDetachStmt;
    }

    /**
     * @param hasDetachStmt Whether the class contains a call to a detach method
     */
    public void setHasDetachStatement(final boolean hasDetachStmt) {
        this.hasDetachStmt = hasDetachStmt;
    }

    /**
     * @return Whether the update method declaration takes parameters
     */
    public boolean isUpdateMethodHasParameters() {
        return updateMethod != null && updateMethod.getNumberOfParams() > 0;
    }
}
