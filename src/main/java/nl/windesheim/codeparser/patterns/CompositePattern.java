package nl.windesheim.codeparser.patterns;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;

/**
 * Composite pattern.
 */
public class CompositePattern implements IDesignPattern {
    /**
     * The component of the composite pattern.
     */
    private ClassOrInterfaceDeclaration component;

    /**
     * The composites in the composite pattern.
     */
    private List<ClassOrInterfaceDeclaration> composites;

    /**
     * The leafs in the composite pattern.
     */
    private List<ClassOrInterfaceDeclaration> leafs;

    /**
     * Default constructor.
     * @param component Components in composite pattern.
     * @param composites Composites in composite pattern.
     * @param leafs Leafs in composite pattern.
     */
    public CompositePattern(final ClassOrInterfaceDeclaration component, final List<ClassOrInterfaceDeclaration> composites, final List<ClassOrInterfaceDeclaration> leafs) {
        this.setComponent(component);
        this.setComposites(composites);
        this.setLeafs(leafs);
    }

    /**
     * Get component.
     * @return component
     */
    public ClassOrInterfaceDeclaration getComponent() {
        return component;
    }

    /**
     * Set component.
     * @param component Component in composite pattern.
     */
    public void setComponent(final ClassOrInterfaceDeclaration component) {
        this.component = component;
    }

    /**
     * Get composites.
     * @return
     */
    public List<ClassOrInterfaceDeclaration> getComposites() {
        return composites;
    }

    /**
     * Set composites.
     * @param composites Composites in composite pattern.
     */
    public void setComposites(final List<ClassOrInterfaceDeclaration> composites) {
        this.composites = composites;
    }

    /**
     * Get leafs
     * @return Leafs
     */
    public List<ClassOrInterfaceDeclaration> getLeafs() {
        return leafs;
    }

    /**
     * set Leafs
     * @param leafs Leafs in composite pattern.
     */
    public void setLeafs(final List<ClassOrInterfaceDeclaration> leafs) {
        this.leafs = leafs;
    }
}
