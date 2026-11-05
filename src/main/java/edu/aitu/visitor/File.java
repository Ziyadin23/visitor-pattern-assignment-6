package edu.aitu.visitor;

import java.util.Objects;

/** An immutable in-memory file; no operating-system file is opened. */
public final class File implements FileSystemElement {
    private final String name;
    private final long sizeBytes;

    public File(String name, long sizeBytes) {
        this.name = Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("File name must not be blank");
        }
        if (sizeBytes < 0) {
            throw new IllegalArgumentException("File size must not be negative");
        }
        this.sizeBytes = sizeBytes;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visitFile(this);
    }
}
