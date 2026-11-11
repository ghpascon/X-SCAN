#!/usr/bin/env bash
set -e

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$PROJECT_ROOT"

echo "▶ Biblioteca: usando remote (GitHub Packages)..."
./scripts/lib_source.sh remote

echo "▶ Build Android (debug only)..."
./gradlew assembleDebug --quiet

APK_DIR="app/build/outputs/apk/debug"
APK_METADATA="$APK_DIR/output-metadata.json"
APK_NAME="$(jq -r '.elements[0].outputFile // empty' "$APK_METADATA")"
APK="$APK_DIR/$APK_NAME"

if [ -n "$APK_NAME" ] && [ -f "$APK" ]; then
  echo "✓ Build concluído:"
  echo "$APK"
else
  echo "✗ APK não encontrado"
  exit 1
fi