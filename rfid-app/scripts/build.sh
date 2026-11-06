#!/usr/bin/env bash
set -e

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$PROJECT_ROOT"

echo "▶ Biblioteca: usando remote (GitHub Packages)..."
./scripts/lib_source.sh remote

echo "▶ Build Android (debug only)..."
./gradlew assembleDebug --quiet

APK="app/build/outputs/apk/debug/app-debug.apk"

if [ -f "$APK" ]; then
  echo "✓ Build concluído:"
  echo "$APK"
else
  echo "✗ APK não encontrado"
  exit 1
fi