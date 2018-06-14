package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FilePart;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Singleton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This analyzer tries to detect a singleton pattern.
 * A class is seen as a singleton if the following conditions are true:
 * - The class only has private constructors
 * - The class has a static instance of itself
 * -- or contains a static inner class with an instance of itself
 * - The class has a static method which returns the static instance of itself
 *
 * Or a partial match if one of the following is true:
 * - the class has a non private constructor
 */
public class SingletonAnalyzer extends PatternAnalyzer {

    /**
     * @inheritDoc
     */
    @Override
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {
        ArrayList<IDesignPattern> singletons = new ArrayList<IDesignPattern>();

        //For each file
        for (CompilationUnit compilationUnit : files) {

            //For each class
            for (TypeDeclaration<?> type : compilationUnit.getTypes()) {
                if (type instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration classDeclaration = (ClassOrInterfaceDeclaration) type;
                    if (classDeclaration.isInterface()) {
                        continue;
                    }

                    // Determine if the class contains a private static property, with it's own
                    // classname as type. This property could be located in a member class
                    StaticInstancePropertyFinder instanceFinder
                            = new StaticInstancePropertyFinder(classDeclaration.getNameAsString());
                    instanceFinder.visit(classDeclaration, null);

                    if (!instanceFinder.isHasDeclaration()) {
                        continue;
                    }

                    StaticInstanceGetterFinder getterFinder =
                            new StaticInstanceGetterFinder(classDeclaration.getNameAsString());
                    getterFinder.visit(classDeclaration, null);

                    if (!getterFinder.isHasDeclaration()) {
                        continue;
                    }

                    singletons.add(generateSingleton(compilationUnit, classDeclaration));
                }
            }
        }

        return singletons;
    }

    /**
     * Checks if a class only has a private constructor.
     *
     * @param classDeclaration the class to check
     * @return boolean result
     */
    private boolean onlyHasPrivateConstructors(final ClassOrInterfaceDeclaration classDeclaration) {
        //There are no constructors defined, so there is a default non private constructor
        //So this is not a singleton class
        if (classDeclaration.getConstructors().size() == 0) {
            return false;
        } else {
            for (ConstructorDeclaration constructor : classDeclaration.getConstructors()) {
                if (!constructor.getModifiers().contains(Modifier.PRIVATE)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @param compilationUnit  the compilation unit which contains the class
     * @param classDeclaration the class declaration
     * @return the singleton
     */
    private Singleton generateSingleton(
            final CompilationUnit compilationUnit,
            final ClassOrInterfaceDeclaration classDeclaration) {

        Singleton singleton = new Singleton();

        if (compilationUnit.getStorage().isPresent() && classDeclaration.getRange().isPresent()) {
            File file = compilationUnit.getStorage().get().getPath().toFile();

            FilePart filePart = new FilePart().setFile(file);
            filePart.setRange(classDeclaration.getRange().get());

            ClassOrInterface classOrInterface = new ClassOrInterface()
                    .setFilePart(filePart)
                    .setDeclaration(classDeclaration)
                    .setName(classDeclaration.getName().asString());

            singleton.setPrivateConstructor(onlyHasPrivateConstructors(classDeclaration));

            singleton.setSingletonClass(classOrInterface);
        }

        return singleton;
    }
}
