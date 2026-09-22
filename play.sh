#!/usr/bin/env bash
# Compiles (if needed) and starts the game
set -e
cd "$(dirname "$0")"
if [ ! -d out ] || [ -n "$(find src -name '*.java' -newer out 2>/dev/null)" ]; then
  mkdir -p out
  javac -d out src/*.java
fi
java -cp out Main
