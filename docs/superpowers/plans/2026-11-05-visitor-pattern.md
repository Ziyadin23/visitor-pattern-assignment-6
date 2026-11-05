# Visitor Pattern Implementation Plan

> **For agentic workers:** Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Deliver Assignment #6, Option A, with tested Java 17 code, report, UML, and a public repository.

**Architecture:** Immutable File and Directory elements implement FileSystemElement.accept(FileSystemVisitor). Each concrete visitor owns its recursive traversal and one operation. The client starts each operation with root.accept(visitor).

**Tech Stack:** Java 17 standard library, javac, a dependency-free assertion-based test runner; Word report authored with python-docx.

**Spec:** Assignment_6_Visitor_Pattern_Instructions.docx, supplied by the user. Requirements summarized in docs/requirements.md.

## Global Constraints
- Language: Java (JDK 17).
- At least two concrete elements and two concrete visitors, with explicit visitFile/visitDirectory dispatch.
- No runtime type-check chains. Demonstrate a third visitor without changing elements.
- Report: introduction, UML, five justified Clean Code principles with excerpts, real sample output, conclusion, repository link.
- Black Times New Roman report. Incremental commits and public GitHub repository.

## Review Focus
- Empty trees and zero-byte files yield zero size/count, empty search.
- Nested directories are visited exactly once per occurrence, preserving insertion order.
- Nulls, blank names and negative sizes are rejected at construction boundaries.
- Caller-owned collections cannot mutate a constructed tree; returned collections cannot be changed.
- Sizes above int range work; long overflow fails explicitly rather than returning a negative total.

### Task 1: Elements and size visitor
**Files:** src/main/java/edu/aitu/visitor/{FileSystemElement,FileSystemVisitor,File,Directory,SizeCalculatorVisitor}.java; src/test/java/edu/aitu/visitor/VisitorPatternTest.java; test.sh.
**Interfaces:** accept(FileSystemVisitor), visitFile(File), visitDirectory(Directory), getName(), getSizeBytes(), getChildren(), getTotalSizeBytes().
- [x] Write tests for dispatch, nested total 6100, empty/zero, invalid data, immutable children, long sizes and overflow; run and confirm missing implementation.
- [x] Implement the interfaces, immutable elements and recursive size operation; run tests and commit.

### Task 2: Search visitor
**Files:** SearchVisitor.java and VisitorPatternTest.java.
**Interfaces:** SearchVisitor(Predicate<File>), getMatches(): List<File>.
- [x] Add tests for recursive ordered search, no match, arbitrary predicate, immutable results and null criterion; observe failure.
- [x] Implement predicate search with immutable result snapshots; verify and commit.

### Task 3: Extensibility and demo
**Files:** FileCountVisitor.java, Demo.java, run.sh; update tests.
**Interfaces:** FileCountVisitor.getFileCount(): long; Demo.main(String[]).
- [x] Add tests for recursive count, empty tree, direct-file visitation and demo's numeric results; observe failure.
- [x] Add the third visitor without changing File/Directory, implement a deterministic demo, verify and commit.

### Task 4: Documentation and publication
**Files:** README.md, docs/{uml.svg,uml.png,uml.puml,sample-output.txt,defense-guide.md,requirements.md,Visitor_Pattern_Report.docx}.
- [x] Capture actual demo output; create UML matching final code and explain double dispatch and five Clean Code principles with excerpts.
- [x] Render report with bundled LibreOffice; inspect all pages and fix layout issues.
- [ ] Run whole-project checks, obtain independent review and fix material findings.
- [ ] Commit documentation, create public repository, push all commits, and verify anonymous access.
