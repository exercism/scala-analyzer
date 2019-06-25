#!/usr/bin/env sh

# Usage:
# ./bin/analyze.sh two_fer ~/test/

java -classpath "lib/*" io.exercism.analyzer.Analyzer "$@"