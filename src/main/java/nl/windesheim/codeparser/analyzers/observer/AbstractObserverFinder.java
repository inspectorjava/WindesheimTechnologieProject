package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObserver;
import nl.windesheim.codeparser.analyzers.observer.components.NotificationMethod;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;
import nl.windesheim.codeparser.patterns.ObserverPattern;

import java.util.*;

public class AbstractObserverFinder extends VoidVisitorAdapter<Void> {
    private TypeSolver typeSolver;

    private List<ObserverPattern> observerPatterns;

    public AbstractObserverFinder (final TypeSolver typeSolver, final List<ObserverPattern> observerPatterns) {
        super();
        this.typeSolver = typeSolver;
        this.observerPatterns = observerPatterns;
    }

    public void visit (ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        // TODO Lookup van observercollection optimaliseren
        ResolvedReferenceTypeDeclaration classTypeDeclaration = classDeclaration.resolve();

        // Check whether the class is being called somewhere in an observercollection
        for (ObserverPattern observerPattern : observerPatterns) {
            AbstractObservable abstractObservable = observerPattern.getAbstractObservable();
            List<ObserverCollection> observerCollections = abstractObservable.getObserverCollections();

            for (ObserverCollection observerCollection : observerCollections) {
                ResolvedReferenceTypeDeclaration parameterType = observerCollection.getParameterType().getTypeDeclaration();

                if (parameterType.equals(classTypeDeclaration)) {
                    AbstractObserver abstractObserver = new AbstractObserver(classDeclaration, classTypeDeclaration);
                    findUpdateMethod(abstractObserver, observerPattern, observerCollection);
                }
            }
        }
    }

    public void findUpdateMethod (final AbstractObserver abstractObserver, final ObserverPattern observerPattern, final ObserverCollection observerCollection) {
        // TODO Efficienter maken?
        // Class or interface contains the update-method as called in the notification method
        List<NotificationMethod> notificationMethods = observerCollection.getNotificationMethods();

        Set<ResolvedMethodDeclaration> methods = abstractObserver.getResolvedTypeDeclaration().getDeclaredMethods();
        for (ResolvedMethodDeclaration method : methods) {
            for (NotificationMethod notificationMethod : notificationMethods) {
                ResolvedMethodDeclaration resolvedNotificationMethod = JavaParserFacade.get(typeSolver).solve(notificationMethod.getMethodCall()).getCorrespondingDeclaration();

                if (resolvedNotificationMethod.getQualifiedSignature().equals(method.getQualifiedSignature())) {
                    abstractObserver.setUpdateMethod(resolvedNotificationMethod);
                    observerPattern.setAbstractObserver(abstractObserver);
                }
            }
        }
    }
}
