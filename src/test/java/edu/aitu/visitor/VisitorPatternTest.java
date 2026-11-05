package edu.aitu.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Dependency-free tests. Every check throws on failure, even without -ea. */
public final class VisitorPatternTest {
    private static int passed;

    private VisitorPatternTest() { }

    public static void main(String[] args) {
        test("nested size", VisitorPatternTest::nestedSize);
        test("empty directory", () -> equal(0L, size(new Directory("empty", List.of()))));
        test("zero-byte file", () -> equal(0L, size(new File("empty.txt", 0))));
        test("direct file", () -> equal(42L, size(new File("data.bin", 42))));
        test("long size", () -> equal(3_000_000_001L, size(new Directory("big", List.of(
                new File("a.bin", 3_000_000_000L), new File("b.bin", 1))))));
        test("overflow is explicit", () -> expect(ArithmeticException.class, () -> size(
                new Directory("overflow", List.of(new File("a", Long.MAX_VALUE), new File("b", 1))))));
        test("double dispatch", VisitorPatternTest::doubleDispatch);
        test("defensive child copy", VisitorPatternTest::defensiveChildCopy);
        test("unmodifiable children", () -> expect(UnsupportedOperationException.class, () ->
                fixture().getChildren().add(new File("unexpected", 1))));
        test("negative size rejected", () -> expect(IllegalArgumentException.class, () -> new File("a", -1)));
        test("blank file name rejected", () -> expect(IllegalArgumentException.class, () -> new File(" ", 1)));
        test("blank directory name rejected", () -> expect(IllegalArgumentException.class, () -> new Directory(" ", List.of())));
        test("null file name rejected", () -> expect(NullPointerException.class, () -> new File(null, 1)));
        test("null directory name rejected", () -> expect(NullPointerException.class, () -> new Directory(null, List.of())));
        test("null children rejected", () -> expect(NullPointerException.class, () -> new Directory("root", null)));
        test("null child rejected", () -> {
            List<FileSystemElement> children = new ArrayList<>();
            children.add(null);
            expect(NullPointerException.class, () -> new Directory("root", children));
        });
        System.out.println("All " + passed + " tests passed.");
    }

    private static Directory fixture() {
        return new Directory("project", List.of(
                new File("readme.txt", 100),
                new Directory("docs", List.of(new File("notes.TXT", 200), new File("guide.pdf", 300))),
                new Directory("images", List.of(new File("logo.png", 5000))),
                new File("todo.txt", 500)));
    }

    private static long size(FileSystemElement element) {
        SizeCalculatorVisitor visitor = new SizeCalculatorVisitor();
        element.accept(visitor);
        return visitor.getTotalSizeBytes();
    }

    private static void nestedSize() {
        equal(6100L, size(fixture()));
    }

    private static void doubleDispatch() {
        List<String> visited = new ArrayList<>();
        FileSystemVisitor visitor = new FileSystemVisitor() {
            @Override
            public void visitFile(File file) { visited.add("file:" + file.getName()); }
            @Override
            public void visitDirectory(Directory directory) { visited.add("directory:" + directory.getName()); }
        };
        FileSystemElement file = new File("a", 1);
        FileSystemElement directory = new Directory("d", List.of(file));
        file.accept(visitor);
        directory.accept(visitor);
        equal(List.of("file:a", "directory:d"), visited);
    }

    private static void defensiveChildCopy() {
        List<FileSystemElement> original = new ArrayList<>();
        original.add(new File("a", 10));
        Directory directory = new Directory("root", original);
        original.add(new File("b", 20));
        equal(10L, size(directory));
    }

    private static void test(String name, Runnable body) {
        try {
            body.run();
            passed++;
            System.out.println("PASS " + name);
        } catch (AssertionError | RuntimeException failure) {
            throw new AssertionError("FAIL " + name, failure);
        }
    }

    private static void equal(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void expect(Class<? extends Throwable> expected, Runnable action) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (!expected.isInstance(actual)) {
                throw new AssertionError("Expected " + expected.getSimpleName() + " but got " + actual, actual);
            }
            return;
        }
        throw new AssertionError("Expected " + expected.getSimpleName());
    }
}
