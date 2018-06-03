package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.*;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;

import java.util.*;

public class AbstractObserverFinder extends VoidVisitorAdapter<Void> {
    private TypeSolver typeSolver;

    private List<EligibleObserverPattern> observerPatterns;

    public AbstractObserverFinder (final TypeSolver typeSolver, final List<EligibleObserverPattern> observerPatterns) {
        super();
        this.typeSolver = typeSolver;
        this.observerPatterns = observerPatterns;
    }

    public void visit (ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        // TODO Lookup van observercollection optimaliseren
        ResolvedReferenceTypeDeclaration classTypeDeclaration = classDeclaration.resolve();

        // Check whether the class is being called somewhere in an observercollection
        for (EligibleObserverPattern observerPattern : observerPatterns) {
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

    private void findUpdateMethod (final AbstractObserver abstractObserver, final EligibleObserverPattern observerPattern, final ObserverCollection observerCollection) {
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
