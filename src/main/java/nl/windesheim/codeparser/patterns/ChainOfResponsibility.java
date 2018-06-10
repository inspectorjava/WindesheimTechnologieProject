package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * The chain of responsibility pattern.
 */
public class ChainOfResponsibility implements IDesignPattern {

    /**
     * The abstract class or interface all chain links have in common.
     */
    private ClassOrInterface commonParent;

    /**
     * True if the common parent has any methods declared.
     */
    private boolean parentHasMethods;

    /**
     * The list of chain links in the cain of responsibility.
     */
    private List<ClassOrInterface> chainLinks = new ArrayList<>();

    /**
     * A list of links which don't call the next link.
     */
    private List<ClassOrInterface> nonChainedLinks = new ArrayList<>();

    /**
     * @return the common parent of the cain links
     */
    public ClassOrInterface getCommonParent() {
        return commonParent;
    }

    /**
     * @param commonParent the common parent to set
     * @return this
     */
    public ChainOfResponsibility setCommonParent(final ClassOrInterface commonParent) {
        this.commonParent = commonParent;
        return this;
    }

    /**
     * @return the list of chains in this design pattern
     */
    public List<ClassOrInterface> getChainLinks() {
        return chainLinks;
    }

    /**
     * @param chainLinks the list of chain to be set for this pattern
     * @return this
     */
    public ChainOfResponsibility setChainLinks(final List<ClassOrInterface> chainLinks) {
        this.chainLinks = chainLinks;
        return this;
    }

    /**
     * @return true/false
     */
    public boolean parentHasMethods() {
        return parentHasMethods;
    }

    /**
     * @param parentHasMethods true/false
     * @return this
     */
    public ChainOfResponsibility setParentHasMethods(final boolean parentHasMethods) {
        this.parentHasMethods = parentHasMethods;
        return this;
    }

    /**
     * @return non chained links
     */
    public List<ClassOrInterface> getNonChainedLinks() {
        return nonChainedLinks;
    }

    /**
     * @param nonChainedLinks non chained links
     * @return this
     */
    public ChainOfResponsibility setNonChainedLinks(
            final List<ClassOrInterface> nonChainedLinks
    ) {
        this.nonChainedLinks = nonChainedLinks;
        return this;
    }
}
