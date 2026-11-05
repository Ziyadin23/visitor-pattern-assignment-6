package edu.aitu.visitor;

import java.util.List;
import java.util.Locale;

/** Runs all operations against the same deterministic object structure. */
public final class Demo {
    private Demo() { }

    public static void main(String[] args) {
        Directory root = new Directory("project", List.of(
                new File("readme.txt", 100),
                new Directory("docs", List.of(
                        new File("notes.TXT", 200), new File("guide.pdf", 300))),
                new Directory("images", List.of(new File("logo.png", 5000))),
                new File("todo.txt", 500)));

        System.out.println("File System Visitor Demo");
        System.out.println("Structure: project/");
        System.out.println("  readme.txt (100 bytes)");
        System.out.println("  docs/");
        System.out.println("    notes.TXT (200 bytes)");
        System.out.println("    guide.pdf (300 bytes)");
        System.out.println("  images/");
        System.out.println("    logo.png (5000 bytes)");
        System.out.println("  todo.txt (500 bytes)");
        System.out.println();

        SizeCalculatorVisitor sizeVisitor = new SizeCalculatorVisitor();
        root.accept(sizeVisitor);
        System.out.println("Total size: " + sizeVisitor.getTotalSizeBytes() + " bytes");

        SearchVisitor searchVisitor = new SearchVisitor(file ->
                file.getName().toLowerCase(Locale.ROOT).endsWith(".txt"));
        root.accept(searchVisitor);
        System.out.println("Text files (.txt, case-insensitive):");
        for (File file : searchVisitor.getMatches()) {
            System.out.println("  " + file.getName() + " (" + file.getSizeBytes() + " bytes)");
        }

        FileCountVisitor countVisitor = new FileCountVisitor();
        root.accept(countVisitor);
        System.out.println("File count: " + countVisitor.getFileCount());
    }
}
