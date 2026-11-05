# Assignment 6 — Visitor Pattern

Java 17 implementation of **Option A: File System Visitor** for ShP-2216, Astana IT University.

[Public repository](https://github.com/Ziyadin23/visitor-pattern-assignment-6) · [Report (Word)](docs/Visitor_Pattern_Report.docx) · [Report (PDF)](docs/Visitor_Pattern_Report.pdf) · [UML](docs/uml.png) · [Defense guide](docs/defense-guide.md)

## Run and test

Install a **JDK 17 or newer** and make `java` and `javac` available on PATH. No Maven, Gradle, libraries, or network access are needed to build.

```bash
git clone https://github.com/Ziyadin23/visitor-pattern-assignment-6.git
cd visitor-pattern-assignment-6
./run.sh
./test.sh
```

The scripts work with Bash on Linux/macOS or Git Bash on Windows and compile with `--release 17 -Xlint:all -Werror`.

**IntelliJ IDEA / Windows without Bash:** Open the folder, select JDK 17, mark `src/main/java` as Sources Root and `src/test/java` as Test Sources Root. Run `edu.aitu.visitor.Demo` for the demo or `edu.aitu.visitor.VisitorPatternTest` for all tests. The test runner throws an error on a failed check, even when JVM assertions are disabled.

## Structure

```text
src/main/java/edu/aitu/visitor/
  FileSystemElement.java     Element interface: getName, accept
  File.java                  Immutable file: name, size in bytes
  Directory.java             Immutable ordered child collection
  FileSystemVisitor.java     visitFile and visitDirectory
  SizeCalculatorVisitor.java Recursive byte total
  SearchVisitor.java         Predicate-based file search
  FileCountVisitor.java      Third independent operation
  Demo.java                  Client and shared sample structure
src/test/java/edu/aitu/visitor/
  VisitorPatternTest.java    30 dependency-free tests
docs/
  Visitor_Pattern_Report.docx / .pdf
  uml.png / .svg / .puml
  sample-output.txt
  defense-guide.md
  requirements.md
run.sh / test.sh             Reproducible build commands
```

## Design and double dispatch

`File` and `Directory` implement `FileSystemElement`. The client calls `root.accept(visitor)`. Java first dynamically selects the concrete element's `accept`. Inside `File.accept`, `this` has compile-time type `File`, so the call is `visitor.visitFile(this)`. The receiver's runtime type then selects the concrete visitor implementation. `Directory.accept` similarly calls `visitDirectory(this)`.

`accept` performs dispatch only. Each concrete visitor's `visitDirectory` explicitly iterates `getChildren()` and calls `child.accept(this)`, so the client initiates one depth-first traversal per operation. Children are visited in insertion order, once per occurrence. There are no `instanceof` checks, casts, reflection, or type-switch chains in production code.

The third visitor was added in a later commit without modifying `File`, `Directory`, or `FileSystemVisitor`.

## Sample output

```text
File System Visitor Demo
Structure: project/
  readme.txt (100 bytes)
  docs/
    notes.TXT (200 bytes)
    guide.pdf (300 bytes)
  images/
    logo.png (5000 bytes)
  todo.txt (500 bytes)

Total size: 6100 bytes
Text files (.txt, case-insensitive):
  readme.txt (100 bytes)
  notes.TXT (200 bytes)
  todo.txt (500 bytes)
File count: 5
```

See [the captured output](docs/sample-output.txt). All visitors operate on this same immutable root.

## Behavior and boundaries

- This is a small **in-memory model**, not a scan of the operating system. File sizes are supplied values; directory metadata occupies zero bytes in this model.
- Names must be non-null and nonblank. File sizes must be nonnegative. Null child lists, null child elements, and null search predicates are rejected.
- Directory copies its input list and exposes an unmodifiable list. Search returns an unmodifiable snapshot. `File` and `Directory` are immutable and final.
- `long` supports sizes above 2 GB; arithmetic overflow throws `ArithmeticException`.
- Search accepts `Predicate<File>`; the demo matches `.txt` case-insensitively using `Locale.ROOT`. Directory names are not search results. Matching file objects are returned; names need not be globally unique.
- **Use a new visitor per independent operation.** Visitors accumulate results when reused; they are not thread-safe. Predicates are supplied by trusted client code, and their exceptions propagate.
- Recursion is appropriate for this small model. An extremely deep hierarchy can exhaust the Java stack. A shared child object is processed for each occurrence; this is a logical hierarchy without hard-link deduplication or path identity.
- Adding a new operation needs only a new visitor. Adding a new element type needs a visitor-interface method and updates to every visitor, which is Visitor's main trade-off.

## Verification

`./test.sh` runs 30 checks: nested/empty/direct-file operations, double dispatch, case-insensitive and size-based searches, no matches, immutable collections and snapshots, invalid inputs, long totals, overflow, independent visitors, and the demo's results.

Each visitor takes O(N) traversal time when the search predicate is O(1). Size and count use O(H) stack space; search additionally stores O(K) matches, where H is depth and K is match count.

## Submission and defense

The report includes all six report deliverables and five annotated Clean Code examples. Use the [defense guide](docs/defense-guide.md) to explain dispatch, run a live demonstration, and prepare for questions. Submit the report and repository link through Moodle and attend the defense according to the course calendar. This repository does not perform LMS submission.
