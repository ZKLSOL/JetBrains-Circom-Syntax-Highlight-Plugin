#!/bin/bash
set -e

echo "Running tests..."
./gradlew test

echo "Tests complete!"
