package edu.aitu.visitor;

/** Accumulates file bytes. Use a fresh visitor for each independent calculation. */
public final class SizeCalculatorVisitor implements FileSystemVisitor {
    private long totalSizeBytes;

    @Override
    public void visitFile(File file) {
        totalSizeBytes = Math.addExact(totalSizeBytes, file.getSizeBytes());
    }

    @Override
    public void visitDirectory(Directory directory) {
        for (FileSystemElement child : directory.getChildren()) {
            child.accept(this);
        }
    }

    public long getTotalSizeBytes() {
        return totalSizeBytes;
    }
}
