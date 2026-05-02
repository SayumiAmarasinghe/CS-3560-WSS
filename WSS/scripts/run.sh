#!/bin/bash
cd "$(dirname "$0")/.." || exit 1
javac -d bin $(find src -name "*.java")
java -cp bin wss.Main