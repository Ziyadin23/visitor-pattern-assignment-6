package edu.aitu.visitor;

/** A third operation added without editing either concrete element. */
public final class FileCountVisitor implements FileSystemVisitor {
    private long fileCount;

    @Override
    public void visitFile(File file) {
        fileCount = Math.incrementExact(fileCount);
    }

    @Override
    public void visitDirectory(Directory directory) {
        for (FileSystemElement child : directory.getChildren()) {
            child.accept(this);
        }
    }

    public long getFileCount() {
        return fileCount;
    }
}
