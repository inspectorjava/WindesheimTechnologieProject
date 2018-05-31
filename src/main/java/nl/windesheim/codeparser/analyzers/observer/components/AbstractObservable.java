package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import nl.windesheim.codeparser.ClassOrInterface;

import java.util.ArrayList;
import java.util.List;

public class AbstractObservable {
    private ClassOrInterfaceDeclaration abstractObservableClass;
    private ResolvedReferenceTypeDeclaration classType;
    private List<ObserverCollection> observerCollections;
    private List<ClassOrInterfaceDeclaration> concreteClasses;

    public AbstractObservable(final ClassOrInterfaceDeclaration abstractObservableClass, final List<ObserverCollection> observerCollections) {
        this(abstractObservableClass, null, observerCollections);
    }

    public AbstractObservable(final ClassOrInterfaceDeclaration abstractObservableClass, final ResolvedReferenceTypeDeclaration classType, final List<ObserverCollection> observerCollections) {
        this.abstractObservableClass = abstractObservableClass;
        this.classType = classType;
        this.observerCollections = observerCollections;
        this.concreteClasses = new ArrayList<>();
    }

    public ClassOrInterfaceDeclaration getAbstractObservableClass() {
        return abstractObservableClass;
    }

    public ResolvedReferenceTypeDeclaration getClassType () {
        // TODO Gooi een exception op als dit niet lukt
        if (classType == null) {
            classType = abstractObservableClass.resolve();
        }

        return classType;
    }

    public List<ObserverCollection> getObserverCollections () {
        return observerCollections;
    }

    public List<ClassOrInterfaceDeclaration> getConcreteClasses () {
        return concreteClasses;
    }

    public AbstractObservable setConcreteClasses (final List<ClassOrInterfaceDeclaration> concreteClasses) {
        this.concreteClasses = concreteClasses;
        return this;
    }

    public AbstractObservable addConcreteClass (final ClassOrInterfaceDeclaration concreteClass) {
        this.concreteClasses.add(concreteClass);
        return this;
    }
}
