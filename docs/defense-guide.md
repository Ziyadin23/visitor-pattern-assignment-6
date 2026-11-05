# In-class defense guide

## Demonstration (about three minutes)

1. Open `Demo.java` and identify the one root shared by all operations: five files, two subdirectories, 6,100 bytes in total.
2. Run `./run.sh`. Show the size total, the three `.txt` matches (including `notes.TXT`), and file count 5.
3. Open `FileSystemElement` and `FileSystemVisitor`. Explain that elements accept an operation and visitors provide one entry point per element type.
4. Trace `root.accept(sizeVisitor)`: Directory.accept -> SizeCalculatorVisitor.visitDirectory -> child.accept(this). For a file, File.accept -> SizeCalculatorVisitor.visitFile -> add its bytes.
5. Repeat the trace with `searchVisitor`. The element code stays the same while the selected operation changes.
6. Show the commit adding `FileCountVisitor` using `git log --oneline` and `git show --stat 682e2b2`. Its file list contains no changes to File or Directory.
7. Run `./test.sh`: all 30 tests pass.

## Double-dispatch walkthrough

```java
FileSystemElement element = new File("readme.txt", 100);
FileSystemVisitor visitor = new SizeCalculatorVisitor();
element.accept(visitor);
```

First dispatch: Java uses the runtime element type, File, to choose File.accept.

```java
public void accept(FileSystemVisitor visitor) {
    visitor.visitFile(this);
}
```

Inside this method, `this` is statically a File. The method name/signature `visitFile(File)` is fixed at compile time. Second dispatch: Java uses the runtime visitor type, SizeCalculatorVisitor, to choose its implementation of visitFile. Thus the selected operation depends on both the element type and visitor type. Java is not dynamically resolving an overloaded parameter type.

Directory follows the corresponding `visitDirectory(Directory)` route. A visitor's directory handler then calls accept on each child; accept itself does not traverse.

## Likely questions

**Why Visitor?** It adds size calculation, search, and counting as independent operations without adding operation-specific methods to File and Directory. It fits a stable element hierarchy with growing operations.

**Where is the object structure?** Directory holds `List<FileSystemElement>`, allowing files and nested directories. The client starts traversal at root; visitors recurse through that list.

**Why not instanceof?** Element-specific accept methods select the correct visitor entry point. Virtual method dispatch selects the concrete operation. No type-check chain needs maintenance.

**How would a fourth visitor work?** Create a class implementing FileSystemVisitor, implement visitFile and visitDirectory, and pass an instance to root.accept. Neither element changes.

**What if we add SymbolicLink?** Add a SymbolicLink element and a visitSymbolicLink method to the interface and every visitor. That is the cost of changing the hierarchy.

**What internals are exposed?** Read-only names, file sizes, and an unmodifiable children list. A defensive list copy prevents external list edits. Visitors cannot change the supplied concrete elements.

**Does final List make the list immutable?** No. final prevents reassignment, while List.copyOf creates the unmodifiable snapshot and rejects null children. The concrete child classes are also immutable.

**Do visitors reset?** No. A new instance starts a fresh operation; reuse accumulates. This makes the lifecycle explicit without a reset protocol.

**What happens on overflow?** Math.addExact raises ArithmeticException instead of silently wrapping. Zero-size files are valid and still count as files.

**What are the complexity and limitations?** O(N) traversal for constant-time predicates and O(H) recursion stack; search stores O(K) matches. Very deep trees may overflow the stack. It models entries, not physical disk blocks, symlinks, hard links, or permissions.

**Why duplicate the small traversal loop?** Each visitor owns its traversal policy; two lines keep the teaching example explicit without adding a traversal abstraction. Extracting a shared walker could help if traversal rules become complex.

## Before the defense

Understand each method and adapt the work to your own understanding. Confirm the exact Moodle course dates, upload the report and public repository link, then rehearse the trace and live run. The defense itself must be completed in class.
