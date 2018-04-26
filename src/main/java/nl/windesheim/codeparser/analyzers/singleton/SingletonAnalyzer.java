package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import nl.windesheim.codeparser.ClassPart;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Singleton;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 * This analyzer tries to detect a singleton pattern.
 * A class is seen as a singleton if the following conditions are true:
 * - The class only has private constructors
 * - The class has a static instance of itself
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
                    }

                    for (ConstructorDeclaration constructor : classDeclaration.getConstructors()) {
                        if (!constructor.getModifiers().contains(Modifier.PRIVATE)) {
                            onlyHasPrivateConstructors = false;
                        }
                    }

                    boolean hasStaticInstance = false;
                    boolean hasGetInstanceFunction = false;

                    //Foreach AST nodes in children
                    for (Node childNode : classDeclaration.getChildNodes()) {
                        //If node is a field
                        if (childNode instanceof FieldDeclaration) {

                            FieldDeclaration field = (FieldDeclaration) childNode;
                            EnumSet<Modifier> modifiers = field.getModifiers();

                            //If field is static private
                            if (modifiers.contains(Modifier.PRIVATE) && modifiers.contains(Modifier.STATIC)) {

                                //If the variable type is equal to the name of the class it is a instance
                                // of this class type
                                String fieldType = field.getVariable(0).getType().asString();
                                if (fieldType.equals(classDeclaration.getName().asString())) {
                                    hasStaticInstance = true;
                                }
                            }
                        }

                        //If node is a method
                        if (childNode instanceof MethodDeclaration) {

                            MethodDeclaration methodDeclaration = (MethodDeclaration) childNode;
                            EnumSet<Modifier> modifiers = methodDeclaration.getModifiers();

                            //If method is static and not private
                            if (!modifiers.contains(Modifier.PRIVATE) && modifiers.contains(Modifier.STATIC)) {
                                //If the method return type is equal to the name of the class it is a instance
                                // of this class type
                                String methodType = methodDeclaration.getType().asString();
                                if (methodType.equals(classDeclaration.getName().asString())) {
                                    hasGetInstanceFunction = true;
                                }
                            }
                        }
                    }

                    if (onlyHasPrivateConstructors
                            && hasStaticInstance
                            && hasGetInstanceFunction) {

                        Singleton singleton = new Singleton();

                        if (cu.getStorage().isPresent() && classDeclaration.getRange().isPresent()) {
                            String fileName = cu.getStorage().get().getFileName();
                            File file = new File(fileName);

                            ClassPart classPart = new ClassPart().setFile(file);
                            classPart.setRange(classDeclaration.getRange().get());

                            singleton.setClassPart(classPart);
                        }

                        singletons.add(singleton);
                    }
                }
            }
        }

        return singletons;
    }
}
