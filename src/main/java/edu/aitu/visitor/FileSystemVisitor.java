package edu.aitu.visitor;

/** One operation entry point for each concrete element type. */
public interface FileSystemVisitor {
    void visitFile(File file);

    void visitDirectory(Directory directory);
}
