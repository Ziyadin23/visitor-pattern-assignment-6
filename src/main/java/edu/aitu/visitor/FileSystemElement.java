package edu.aitu.visitor;

/** A node that delegates operation selection to a visitor. */
public interface FileSystemElement {
    String getName();

    void accept(FileSystemVisitor visitor);
}
