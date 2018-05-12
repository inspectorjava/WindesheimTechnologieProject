package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import nl.windesheim.codeparser.FilePart;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Singleton;

import java.io.File;
import java.util.ArrayList;

/**
 * This analyzer tries to detect a singleton pattern.
 * A class is seen as a singleton if the following conditions are true:
 * - The class only has private constructors
 * - The class has a static instance of itself
 * -- or contains a static inner class with an instance of itself
 * - The class has a static method which returns the static instance of itself
 */
public class SingletonAnalyzer extends PatternAnalyzer {

    /**
     * @inheritDoc
     */
    @Override
    public ArrayList<IDesignPattern> analyze(final ArrayList<CompilationUnit> files) {
        ArrayList<IDesignPattern> singletons = new ArrayList<IDesignPattern>();

        //For each file
        for (CompilationUnit cu : files) {

            //For each class
            for (TypeDeclaration<?> type : cu.getTypes()) {
                if (type instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration classDeclaration = (ClassOrInterfaceDeclaration) type;
                    if (classDeclaration.isInterface()) {
                        continue;
                    }

                    boolean onlyHasPrivateConstructors = true;

                    //There are no constructors defined, so there is a default non private constructor
                    //So this is not a singleton class
                    if (classDeclaration.getConstructors().size() == 0) {
                        onlyHasPrivateConstructors = false;
                    } else {
                        for (ConstructorDeclaration constructor : classDeclaration.getConstructors()) {
                            if (!constructor.getModifiers().contains(Modifier.PRIVATE)) {
                                onlyHasPrivateConstructors = false;
                            }
                        }
                    }

                    if (!onlyHasPrivateConstructors) {
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

                    Singleton singleton = new Singleton();


                    if (cu.getStorage().isPresent() && classDeclaration.getRange().isPresent()) {
                        String fileName = cu.getStorage().get().getFileName();
                        File file = new File(fileName);

                        FilePart filePart = new FilePart().setFile(file);
                        filePart.setRange(classDeclaration.getRange().get());

                        singleton.setFilePart(filePart);
                    }


                    singletons.add(singleton);
                }
            }
        }

        return singletons;
    }
}
