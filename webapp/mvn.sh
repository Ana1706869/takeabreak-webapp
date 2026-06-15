#!/bin/bash
# Maven wrapper - configura JAVA_HOME antes de executar Maven
# Use: ./mvn.sh [argumentos do Maven]

export JAVA_HOME="C:/Program Files/Java/jdk-24"
export PATH="$JAVA_HOME/bin:$PATH"

# Executar Maven com os argumentos passados
mvn "$@"
