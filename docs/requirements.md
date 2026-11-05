# Assignment #6 requirement mapping

Source: user-supplied Assignment_6_Visitor_Pattern_Instructions.docx, ShP-2216, Astana IT University.

Chosen topic: Option A, File System Visitor. The hierarchy is an in-memory model, not an operating-system scan.

| Requirement | Deliverable |
|---|---|
| Java 17, compiling code | src/main/java, run.sh, test.sh |
| Element interface with accept | FileSystemElement |
| Two elements with matching dispatch | File, Directory |
| Visitor interface with a method per element | FileSystemVisitor |
| Two different operations | SizeCalculatorVisitor, SearchVisitor |
| Third operation without element edits | FileCountVisitor, incremental Git history |
| Nested object structure | Directory containing immutable lists of FileSystemElement |
| Five Clean Code principles and annotated excerpts | Report section 4 |
| UML class diagram | docs/uml.png, docs/uml.svg, docs/uml.puml; report section 2 |
| Output from same structure | docs/sample-output.txt; report section 5 |
| Encapsulation conclusion | Report section 7 |
| Clear README and incremental history | README.md; Git commits |
| Public GitHub link | README.md; report section 8 |
| Double-dispatch defense and live demo | docs/defense-guide.md; report section 6 |

The student must personally upload the report and repository link to Moodle and complete the in-class defense. The assignment names relative Week 9/10 deadlines; exact course-calendar dates are not supplied.
