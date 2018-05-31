package nl.windesheim.codeparser.analyzers.chainofresponsibility;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.analyzers.util.visitor.EligibleCommonParentFinder;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.ChainOfResponsibility;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * The analyzer for the chain of responsibility pattern.
 *
 * A chain of responsibility pattern has to have the following:
 * - A abstract class or interface which all chains have in common (common parent)
 * - The 'common parent' should have at least one subclass (link)
 * - A 'link' should have a attribute of the 'common parent' type which is the next 'link' (next link)
 * -- The 'next link' may be inherited from a abstract parent
 * - The 'common parent' should at least have (abstract) one method
 * - A 'link' should call a function defined in the 'common parent' on the 'next link'
 *   from a the same function the the current 'link'
 * -- This call may also be from a function in a abstract 'common parent'
 */
public class ChainOfResponsibilityAnalyzer extends PatternAnalyzer {

    /**
     * A solver for data types.
     */
    private CombinedTypeSolver typeSolver;

    /**
     * A finder which searches for implementations of a interface.
     */
    private ImplementationOrSuperclassFinder implFinder;

    /**
     * A visitor which checks if a 'link' ever calls the next link in the chain.
     */
    private final LinkCallsNextLinkVisitor linkCallVisitor;

    /**
     * Finds eligible common parents.
     */
    private final EligibleCommonParentFinder parentFinder;

    /**
     * Constructor for ChainOfResponsibilityAnalyzer.
     */
    public ChainOfResponsibilityAnalyzer() {
        super();

        parentFinder    = new EligibleCommonParentFinder();
        linkCallVisitor = new LinkCallsNextLinkVisitor();
    }

    @Override
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {

        //Get the typesolver from the parent
        typeSolver = getParent().getTypeSolver();

        //Initialize the list of chain of responsibility patterns we will be returning
        ArrayList<IDesignPattern> chainList = new ArrayList<>();

        //Without a type solver the strategy analyzer can't function
        if (typeSolver == null) {
            return chainList;
        }

        //Initialize the finders
        implFinder = new ImplementationOrSuperclassFinder(typeSolver);

        //Get a list of classes which could be 'common parent' classes or interfaces
        List<ClassOrInterfaceDeclaration> eligibleParents
                = findEligibleParents(files);

        for (ClassOrInterfaceDeclaration parent : eligibleParents) {
            //Get all the links of a common parent
            List<ClassOrInterfaceDeclaration> linksOfParent = findLinksOfCommonParent(parent, files);

            //Remove all links which are not valid
            ArrayList<ClassOrInterfaceDeclaration> badLinks = new ArrayList<>();
            for (ClassOrInterfaceDeclaration link : linksOfParent) {
                //If a link doesn't have a reference of the next link
                if (!linkHasNextLinkReference(link, parent, files)) {
                    badLinks.add(link);
                    continue;
                }

                //If a link never calls a next link
                if (!linkCallsNextLink(link, parent)) {
                    badLinks.add(link);
                    continue;
                }
            }

            //Remove all bad links
            linksOfParent.removeAll(badLinks);

            //If there are no links there is no pattern
            if (linksOfParent.isEmpty()) {
                continue;
            }

            //Create the design pattern object
            ChainOfResponsibility pattern = makePattern(parent, linksOfParent);
            chainList.add(pattern);
        }

        return chainList;
    }

    /**
     * Finds eligible 'common parent' classes or interfaces.
     * @param files the files in which we want to find eligible parents
     * @return a list of eligible 'common parent' classes
     */
    private List<ClassOrInterfaceDeclaration>
        findEligibleParents(final List<CompilationUnit> files) {

        //For each file call the finder
        List<ClassOrInterfaceDeclaration> parents = new ArrayList<>();
        for (CompilationUnit compilationUnit : files) {
            parentFinder.reset();
            parentFinder.visit(compilationUnit, typeSolver);
            parents.addAll(parentFinder.getClasses());
        }

        return parents;
    }

    /**
     * Finds 'links' of a 'common parent'.
     * @param parent the 'common parent' for which we are searching 'links'
     * @param files the files in which we wan't to find the links
     * @return a list of found 'links'
     */
    private List<ClassOrInterfaceDeclaration> findLinksOfCommonParent(
            final ClassOrInterfaceDeclaration parent,
            final List<CompilationUnit> files
    ) {
        ArrayList<ClassOrInterfaceDeclaration> links = new ArrayList<>();

        for (CompilationUnit compilationUnit : files) {
            implFinder.reset();
            implFinder.visit(compilationUnit, parent);
            links.addAll(implFinder.getClasses());
        }

        return links;
    }

    /**
     * Checks if a 'link' class ever will call another 'link'.
     * @param link the 'link' to check
     * @param commonParent the 'common parent' of the 'link'
     * @param files the files in which the 'link' and 'common parent' were found
     * @return true/false
     */
    private boolean linkHasNextLinkReference(
            final ClassOrInterfaceDeclaration link,
            final ClassOrInterfaceDeclaration commonParent,
            final List<CompilationUnit> files
    ) {
        //Get a list of resolved field declarations
        List<ResolvedFieldDeclaration> resolvedFields = findResolvedFieldDeclarations(link, files);

        //for each passable reference field
        for (ResolvedFieldDeclaration resolve : resolvedFields) {
            if (!(resolve instanceof JavaParserFieldDeclaration)) {
                continue;
            }

            //if the field is not a reference to an other class it is not a reference to the 'common parent'
            ResolvedType type = resolve.getType();
            if (!(type instanceof ReferenceTypeImpl)) {
                continue;
            }

            ResolvedReferenceTypeDeclaration referenceType
                    = ((ReferenceTypeImpl) type).getTypeDeclaration();

            //Get the class or interface which the field references
            ClassOrInterfaceDeclaration declaration = null;
            if (referenceType instanceof JavaParserInterfaceDeclaration) {
                declaration = ((JavaParserInterfaceDeclaration) referenceType).getWrappedNode();
            } else if (referenceType instanceof JavaParserClassDeclaration) {
                declaration = ((JavaParserClassDeclaration) referenceType).getWrappedNode();
            }

            //if the field is the same type as the 'common parent' it is most likely the reference to the next link
            if (commonParent.equals(declaration)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds resolved field declarations in 'link' classes and it's superclasses.
     * @param link the link in which fields must be searched
     * @param files the files in which to search
     * @return a list of resolved field declarations
     */
    private List<ResolvedFieldDeclaration> findResolvedFieldDeclarations(
            final ClassOrInterfaceDeclaration link,
            final List<CompilationUnit> files
    ) {
        //Initialize a list of fields which can be a reference to the next link
        List<ResolvedFieldDeclaration> resolvedFields = new ArrayList<>();

        //For each superclass of the current class
        for (ClassOrInterfaceType extendedType : link.getExtendedTypes()) {

            //Resolve the name of the superclass
            ResolvedReferenceType resolved = extendedType.resolve();
            //It should be a reference and not a primitive type
            if (!(resolved instanceof ReferenceTypeImpl)) {
                continue;
            }

            ResolvedReferenceTypeDeclaration typeDeclaration = resolved.getTypeDeclaration();

            //Check if the resolved type is a class
            if (!(typeDeclaration instanceof JavaParserClassDeclaration)) {
                continue;
            }

            ClassOrInterfaceDeclaration typeClass = ((JavaParserClassDeclaration) typeDeclaration).getWrappedNode();

            //Search the resolved class in the list of files
            //We need to do this because resolving the type strips the SymbolSolver which we need
            CompilationUnit compilationUnit = FilePartResolver.findCompilationUnitOfNode(files, typeClass);

            //Get all classes from the file in which we found the superclass
            List<ClassOrInterfaceDeclaration> classes = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);

            //for each class in the file in which we found the superclass
            for (ClassOrInterfaceDeclaration cuTypeClass : classes) {
                //if it is not the superclass go the the next class
                if (!cuTypeClass.equals(typeClass)) {
                    continue;
                }

                //for each field if it is not private field add it to the fields since it can be used by the subclass
                for (FieldDeclaration field : cuTypeClass.getFields()) {
                    if (!field.getModifiers().contains(Modifier.PRIVATE)) {
                        resolvedFields.add(field.resolve());
                    }
                }

                break;
            }
        }

        //For each field in the 'link' class add it to the list of fields
        for (FieldDeclaration fieldDeclaration : link.getFields()) {
            ResolvedFieldDeclaration resolve = fieldDeclaration.resolve();
            resolvedFields.add(resolve);
        }

        return resolvedFields;
    }

    /**
     * Checks if the 'link' class ever calls the next link in the chain.
     * @param link the 'link' class which we are checking
     * @param parent the 'common parent' of the link
     * @return true/false
     */
    private boolean linkCallsNextLink(
            final ClassOrInterfaceDeclaration link,
            final ClassOrInterfaceDeclaration parent
    ) {
        //Check if the 'link' class ever calls the next link
        if (linkCallVisitor.visit(link, parent) != null) {
            return true;
        }

        //Check if one of the superclasses ever calls the next 'link'
        for (ClassOrInterfaceType extendTypes : link.getExtendedTypes()) {
            ResolvedReferenceType resolved = extendTypes.resolve();
            if (!(resolved instanceof ReferenceTypeImpl)) {
                continue;
            }

            ResolvedReferenceTypeDeclaration typeDeclaration = resolved.getTypeDeclaration();

            if (!(typeDeclaration instanceof JavaParserClassDeclaration)) {
                continue;
            }

            ClassOrInterfaceDeclaration typeClass = ((JavaParserClassDeclaration) typeDeclaration).getWrappedNode();

            if (linkCallVisitor.visit(typeClass, parent) != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates the pattern object which will be returned.
     * @param parent the 'common parent' which was found
     * @param links the 'links' which were found
     * @return a ChainOfResponsibility object
     */
    private ChainOfResponsibility makePattern(
            final ClassOrInterfaceDeclaration parent,
            final List<ClassOrInterfaceDeclaration> links
    ) {
        ChainOfResponsibility pattern = new ChainOfResponsibility();

        pattern.setCommonParent(
            new ClassOrInterface()
                .setDeclaration(parent)
                .setName(parent.getNameAsString())
                .setFilePart(FilePartResolver.getFilePartOfNode(parent))
        );

        ArrayList<ClassOrInterface> linkClasses = new ArrayList<>();
        for (ClassOrInterfaceDeclaration link : links) {
            linkClasses.add(
                new ClassOrInterface()
                    .setDeclaration(link)
                    .setName(link.getNameAsString())
                    .setFilePart(FilePartResolver.getFilePartOfNode(link))
            );
        }

        pattern.setChainLinks(linkClasses);

        return pattern;
    }
}
