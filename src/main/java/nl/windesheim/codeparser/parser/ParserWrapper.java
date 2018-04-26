package nl.windesheim.codeparser.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class ParserWrapper {

    public void test() {
        try {
            URL filename = this.getClass()
                    .getClassLoader().getResource("ReversePolishNotation.java");

            System.out.println("Filename: " + filename.getFile());

            CompilationUnit cu = JavaParser.parse(new FileInputStream(filename.getFile()));

            System.out.println("File parsed into a compilation unit");

            VoidVisitor<?> methodNameVisitor = new ParserWrapper.MethodNamePrinter();
            methodNameVisitor.visit(cu, null);
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException caught: " + ex.getMessage());
        }
    }

    public void inheritenceTest() {
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            Path p = FileSystems.getDefault().getPath(cl.getResource("inheritance_test").getPath());

            System.out.println("Setting source root to: " + p);

            SourceRoot sourceRoot = new SourceRoot(p);

            List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
            List<CompilationUnit> units = sourceRoot.getCompilationUnits();

            for (CompilationUnit cu : units) {
                System.out.println("File: " + cu.getStorage().get().getPath());
                System.out.println("Package: " + cu.getPackageDeclaration().get().getNameAsString());

                System.out.println("Classes: ");
                for (TypeDeclaration<?> type: cu.getTypes()) {
                    if (type instanceof ClassOrInterfaceDeclaration) {
                        ClassOrInterfaceDeclaration classType = (ClassOrInterfaceDeclaration) type;

                        if (!classType.isInterface()) {
                            System.out.println("\t" + classType.getNameAsString());
                            System.out.println("\t\tModifiers: " + classType.getModifiers());
                          //  System.out.println("\t\tConstructors: " + classType.getConstructors());
                            System.out.println("\t\tExtends: " + classType.getExtendedTypes());
                            System.out.println("\t\tImplements: " + classType.getImplementedTypes());
                        }
                    }
                   // System.out.println("\t" + type.);
                }

                VoidVisitor<?> methodNameVisitor = new ParserWrapper.MethodNamePrinter();
                methodNameVisitor.visit(cu, null);
            }
        } catch (IOException ex) {
            System.out.println("IOException caught: " + ex.getMessage());
        } catch (ParseProblemException ex) {
            System.out.println("ParseProblemException caught: " + ex.getMessage());
        }
    }

    private static class MethodNamePrinter extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            System.out.println("Method name: " + md.getName());
        }
    }

}
