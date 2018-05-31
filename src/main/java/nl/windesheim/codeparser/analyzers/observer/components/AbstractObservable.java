package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import nl.windesheim.codeparser.ClassOrInterface;

import java.util.ArrayList;
import java.util.List;

public class AbstractObservable extends ObservableClass {
    private List<ObserverCollection> observerCollections;

    public AbstractObservable(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null, new ArrayList<>());
    }

    public AbstractObservable(final ClassOrInterfaceDeclaration classDeclaration, final ResolvedReferenceTypeDeclaration resolvedTypeDeclaration) {
        this(classDeclaration, resolvedTypeDeclaration, new ArrayList<>());
    }

    public AbstractObservable(final ClassOrInterfaceDeclaration classDeclaration, final ResolvedReferenceTypeDeclaration resolvedTypeDeclaration, final List<ObserverCollection> observerCollections) {
        super(classDeclaration, resolvedTypeDeclaration);
        this.observerCollections = observerCollections;
    }

    public List<ObserverCollection> getObserverCollections () {
        return observerCollections;
    }

    public AbstractObservable addObserverCollection (final ObserverCollection observerCollection) {
        observerCollections.add(observerCollection);
        return this;
    }
}
