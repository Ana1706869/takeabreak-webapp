#!/bin/bash
# Build script for Take a Break Web Application
# This ensures JAVA_HOME is always set correctly before building

# Configure Java
export JAVA_HOME="C:/Program Files/Java/jdk-24"
export PATH="$JAVA_HOME/bin:$PATH"

# Navigate to webapp directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR/webapp" || exit 1

echo "=================================================="
echo "Take a Break Web - Build Script"
echo "=================================================="
echo "JAVA_HOME: $JAVA_HOME"
echo "Java Version:"
java -version
echo ""
echo "Building..."
echo "=================================================="
echo ""

# Run Maven
mvn -DskipTests clean package spring-boot:repackage

if [ $? -eq 0 ]; then
    echo ""
    echo "=================================================="
    echo "✓ BUILD SUCCESSFUL!"
    echo "JAR file: webapp/target/take-a-break-web-1.0.0.jar"
    echo "=================================================="
else
    echo ""
    echo "=================================================="
    echo "✗ BUILD FAILED!"
    echo "=================================================="
    exit 1
fi
