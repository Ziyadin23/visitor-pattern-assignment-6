package edu.aitu.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/** Collects matching files in depth-first order. Create a new visitor per search. */
public final class SearchVisitor implements FileSystemVisitor {
    private final Predicate<File> criterion;
    private final List<File> matches = new ArrayList<>();

    public SearchVisitor(Predicate<File> criterion) {
        this.criterion = Objects.requireNonNull(criterion, "criterion");
    }

    @Override
    public void visitFile(File file) {
        if (criterion.test(file)) {
            matches.add(file);
        }
    }

    @Override
    public void visitDirectory(Directory directory) {
        for (FileSystemElement child : directory.getChildren()) {
            child.accept(this);
        }
    }

    public List<File> getMatches() {
        return List.copyOf(matches);
    }
}
