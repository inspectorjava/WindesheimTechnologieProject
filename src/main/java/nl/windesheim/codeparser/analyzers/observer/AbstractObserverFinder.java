package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.*;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractSubject;
import nl.windesheim.codeparser.analyzers.util.ErrorLog;

import java.util.List;
import java.util.Set;

/**
 * Visitor which finds all classes which can be an 'abstract observer'.
 */
public class AbstractObserverFinder extends VoidVisitorAdapter<Void> {
    /**
     * A tool which resolved relations between AST nodes.
     */
    private final TypeSolver typeSolver;

    /**
     * A list of potentially detected observer patterns.
     */
    private final List<EligibleObserverPattern> observerPatterns;

    /**
     * AbstractObserverFinder constructor.
     *
     * @param typeSolver       A TypeSolver which can be used by this class
     * @param observerPatterns A list of potential observer patterns which have already been detected
     */
    public AbstractObserverFinder(
            final TypeSolver typeSolver,
            final List<EligibleObserverPattern> observerPatterns
    ) {
        super();
        this.typeSolver = typeSolver;
        this.observerPatterns = observerPatterns;
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration classDeclaration, final Void arg) {
        try {
            ResolvedReferenceTypeDeclaration classType = classDeclaration.resolve();

            // Check whether the class is being called somewhere in an observer collection
            for (EligibleObserverPattern observerPattern : observerPatterns) {
                if (observerPattern.hasAbstractObserver()) {
                    continue;
                }

                AbstractSubject abstractSubject = observerPattern.getAbstractSubject();
                List<ObserverCollection> observerCols = abstractSubject.getObserverCollections();

                for (ObserverCollection observerCol : observerCols) {
                    ResolvedReferenceTypeDeclaration parameterType =
                            observerCol.getParameterType().getTypeDeclaration();

                    if (!parameterType.equals(classType)) {
                        continue;
                    }

                    AbstractObserver abstractObserver = new AbstractObserver(classDeclaration, classType);
                    if (!findUpdateMethod(abstractObserver, observerCol)) {
                        continue;
                    }

                    observerPattern.setAbstractObserver(abstractObserver);
                    observerPattern.setActiveCollection(observerCol);
                    break;
                }
            }
        } catch (UnsolvedSymbolException ex) {
            ErrorLog.getInstance().addError(ex);
        }
    }



    /**
     * Finds the update method in the AbstractObserver which is being referred to from the subject classes.
     *
     * @param abstObserver    The AbstractSubject referring to this AbstractObserver
     * @param observerCol     A potential observer collection
     */
    private boolean findUpdateMethod(
            final AbstractObserver abstObserver,
            final ObserverCollection observerCol
    ) {
        // Class or interface contains the update-method as called in the notification method
        List<NotificationMethod> notifyMethods = observerCol.getNotificationMethods();

        Set<ResolvedMethodDeclaration> methods = abstObserver.getResolvedTypeDeclaration().getDeclaredMethods();
        for (ResolvedMethodDeclaration method : methods) {
            for (NotificationMethod notify : notifyMethods) {
                try {
                    ResolvedMethodDeclaration resNotify =
                            JavaParserFacade
                                    .get(typeSolver)
                                    .solve(notify.getUpdateMethodCall())
                                    .getCorrespondingDeclaration();

                    if (resNotify.getQualifiedSignature().equals(method.getQualifiedSignature())) {
                        abstObserver.setUpdateMethod(resNotify);
                        return true;
                    }
                } catch (UnsolvedSymbolException ex) {
                    ErrorLog.getInstance().addError(ex);
                }
            }
        }

        return false;
    }
}
