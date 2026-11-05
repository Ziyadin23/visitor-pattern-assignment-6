#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p build/classes
find src/main/java -name '*.java' -print > build/main-sources.txt
javac --release 17 -Xlint:all -Werror -d build/classes @build/main-sources.txt
java -cp build/classes edu.aitu.visitor.Demo
