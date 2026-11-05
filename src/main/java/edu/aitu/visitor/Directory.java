package edu.aitu.visitor;

import java.util.List;
import java.util.Objects;

/** An immutable ordered collection of file-system elements. */
public final class Directory implements FileSystemElement {
    private final String name;
    private final List<FileSystemElement> children;

    public Directory(String name, List<FileSystemElement> children) {
        this.name = Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Directory name must not be blank");
        }
        this.children = List.copyOf(children);
    }

    @Override
    public String getName() {
        return name;
    }

    public List<FileSystemElement> getChildren() {
        return children;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visitDirectory(this);
    }
}
