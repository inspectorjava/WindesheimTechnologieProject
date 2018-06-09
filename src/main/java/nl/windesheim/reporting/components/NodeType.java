package nl.windesheim.reporting.components;

/**
 * The type of a node.
 */
public enum NodeType {
    /**
     * The root of a tree.
     */
    ROOT,

    /**
     * A design pattern.
     */
    DESIGN_PATTERN,

    /**
     * A class of a pattern.
     */
    CLASS,

    /**
     * A interface of a pattern.
     */
    INTERFACE,

    /**
     * A list of classes of a pattern.
     */
    CLASS_LIST,

    /**
     * A fault or error in the design pattern.
     */
    PATTERN_ERROR
}
