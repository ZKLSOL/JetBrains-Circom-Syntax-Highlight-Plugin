#!/bin/bash
set -e

echo "Starting sandbox IDE with plugin..."
echo "(Use the test-samples/ directory to test Circom files)"
echo ""

./gradlew runIde
