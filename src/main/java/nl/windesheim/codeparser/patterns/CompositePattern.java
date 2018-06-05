package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.List;

/**
 * Composite pattern.
 */
public class CompositePattern implements IDesignPattern {
    /**
     * The component of the composite pattern.
     */
    private ClassOrInterface component;

    /**
     * The composites in the composite pattern.
     */
    private List<ClassOrInterface> composites;

    /**
     * The leafs in the composite pattern.
     */
    private List<ClassOrInterface> leafs;

    /**
     * Default constructor.
     * @param component Components in composite pattern.
     * @param composites Composites in composite pattern.
     * @param leafs Leafs in composite pattern.
     */
    public CompositePattern(
            final ClassOrInterface component,
            final List<ClassOrInterface> composites,
            final List<ClassOrInterface> leafs) {
        this.setComponent(component);
        this.setComposites(composites);
        this.setLeafs(leafs);
    }

    /**
     * Get component.
     * @return component
     */
    public ClassOrInterface getComponent() {
        return component;
    }

    /**
     * Set component.
     * @param component Component in composite pattern.
     * @return CompositePattern the pattern
     */
    public CompositePattern setComponent(final ClassOrInterface component) {
        this.component = component;
        return this;
    }

    /**
     * Get composites.
     * @return composites
     */
    public List<ClassOrInterface> getComposites() {
        return composites;
    }

    /**
     * Set composites.
     * @param composites Composites in composite pattern.
     * @return CompositePattern the pattern
     */
    public CompositePattern setComposites(final List<ClassOrInterface> composites) {
        this.composites = composites;
        return this;
    }

    /**
     * Get leafs.
     * @return Leafs
     */
    public List<ClassOrInterface> getLeafs() {
        return leafs;
    }

    /**
     * set Leafs.
     * @param leafs Leafs in composite pattern.
     * @return CompositePattern
     */
    public CompositePattern setLeafs(final List<ClassOrInterface> leafs) {
        this.leafs = leafs;
        return this;
    }
}
