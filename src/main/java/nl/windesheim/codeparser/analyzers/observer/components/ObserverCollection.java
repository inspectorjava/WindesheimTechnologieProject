package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Information on a potential collection of Observers, as defined in an AbstractObservable class.
 */
public class ObserverCollection {
    /**
     * Declaration of the variable which contains the observer collection.
     */
    private final VariableDeclarator variable;

    /**
     * The collection type.
     */
    private final ResolvedReferenceType fieldType;

    /**
     * The type of the elements in the collection.
     */
    private final ResolvedReferenceType parameterType;

    /**
     * A list of attach methods operating on this collection.
     */
    private final List<MethodDeclaration> attachMethods;

    /**
     * A list of detach methods operating on this collection.
     */
    private final List<MethodDeclaration> detachMethods;

    /**
     * A list of notification methods operating on this collection.
     */
    private final List<NotificationMethod> notifyMethods;

    /**
     * ObserverCollection constructor.
     *
     * @param variable Declaration of the variable which contains the observer collection
     * @param fieldType          The collection type
     * @param parameterType      The type of the elements in the collection
     */
    public ObserverCollection(final VariableDeclarator variable,
                              final ResolvedReferenceType fieldType,
                              final ResolvedReferenceType parameterType) {
        this.variable = variable;
        this.fieldType = fieldType;
        this.parameterType = parameterType;

        attachMethods = new ArrayList<>();
        detachMethods = new ArrayList<>();
        notifyMethods = new ArrayList<>();
    }

    /**
     * @return Declaration of the variable which contains the observer collection
     */
    public VariableDeclarator getVariableDeclarator() {
        return variable;
    }

    /**
     * @return The collection type
     */
    public ResolvedReferenceType getFieldType() {
        return fieldType;
    }

    /**
     * @return The type of the elements in the collection
     */
    public ResolvedReferenceType getParameterType() {
        return parameterType;
    }

    /**
     * @return Whether the observer collection has associated attach methods
     */
    public boolean hasAttachMethods() {
        return !attachMethods.isEmpty();
    }

    /**
     * @return The associated attach methods
     */
    public List<MethodDeclaration> getAttachMethods() {
        return attachMethods;
    }

    /**
     * @param attach Attach method associated with the observer collection
     * @return this
     */
    public ObserverCollection addAttachMethod(final MethodDeclaration attach) {
        this.attachMethods.add(attach);
        return this;
    }

    /**
     * @return Whether the observer collection has associated detach methods
     */
    public boolean hasDetachMethods() {
        return !detachMethods.isEmpty();
    }

    /**
     * @return The associated detach methods
     */
    public List<MethodDeclaration> getDetachMethods() {
        return detachMethods;
    }

    /**
     * @param detach Detach method associated with the observer collection
     * @return this
     */
    public ObserverCollection addDetachMethod(final MethodDeclaration detach) {
        this.detachMethods.add(detach);
        return this;
    }

    /**
     * @return Whether the observer collection has associated notification methods
     */
    public boolean hasNotificationMethods() {
        return !notifyMethods.isEmpty();
    }

    /**
     * @return The associated notification methods
     */
    public List<NotificationMethod> getNotificationMethods() {
        return notifyMethods;
    }

    /**
     * @param notify Notification method associated with the observer collection
     * @return this
     */
    public ObserverCollection addNotificationMethod(final NotificationMethod notify) {
        this.notifyMethods.add(notify);
        return this;
    }

    /**
     * @return Whether the collection can be regarded as a valid observer collection
     */
    public boolean isObserverCollection() {
        return hasAttachMethods()
               && hasNotificationMethods();
    }
}
