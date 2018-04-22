package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ParserWrapper {

    public void test () {
        try {
            String filename = "/Users/rickbos/WindesheimTechnologieProject/Marslanden/src/main/java/testdata/ReversePolishNotation.java";
            CompilationUnit cu = JavaParser.parse(new FileInputStream(filename));

            System.out.println("File parsed into a compilation unit");

            VoidVisitor<?> methodNameVisitor = new ParserWrapper.MethodNamePrinter();
            methodNameVisitor.visit(cu, null);
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException caught: " + ex.getMessage());
        }
    }

    private static class MethodNamePrinter extends VoidVisitorAdapter<Void> {
        @Override
        public void visit (MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            System.out.println("Method name: " + md.getName());
        }
    }

}
