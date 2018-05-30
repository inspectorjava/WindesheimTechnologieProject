package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;

public class AbstractObservable {
    private ClassOrInterfaceDeclaration classDeclaration;
    private List<ObserverCollection> observerCollections;

    public AbstractObservable(final ClassOrInterfaceDeclaration classDeclaration, final List<ObserverCollection> observerCollections) {
        this.classDeclaration = classDeclaration;
        this.observerCollections = observerCollections;
    }

    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    public List<ObserverCollection> getObserverCollections () {
        return observerCollections;
    }
}
