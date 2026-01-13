#!/bin/bash
set -e

echo "Deep cleaning (removing all caches)..."

echo "  - Removing build directory..."
rm -rf build

echo "  - Removing .gradle cache..."
rm -rf .gradle

echo "  - Removing .intellijPlatform cache..."
rm -rf .intellijPlatform

echo "  - Removing .kotlin cache..."
rm -rf .kotlin

echo ""
echo "Deep clean complete! Run ./scripts/build.sh to rebuild."
