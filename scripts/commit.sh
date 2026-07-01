#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
BUILD_GRADLE="$ROOT_DIR/rfid-app/app/build.gradle"

if [[ ! -f "$BUILD_GRADLE" ]]; then
  echo "Erro: nao encontrou $BUILD_GRADLE"
  exit 1
fi

echo "Tipo de versao:"
echo "  1) patch"
echo "  2) minor"
echo "  3) major"
read -r -p "Escolha (major/minor/patch): " VERSION_TYPE

case "${VERSION_TYPE,,}" in
  1) VERSION_TYPE="patch" ;;
  2) VERSION_TYPE="minor" ;;
  3) VERSION_TYPE="major" ;;
  major|minor|patch) ;;
  *)
    echo "Opcao invalida. Use major, minor ou patch."
    exit 1
    ;;
esac

read -r -p "Nome do commit: " COMMIT_MESSAGE
if [[ -z "${COMMIT_MESSAGE// }" ]]; then
  echo "Erro: o nome do commit nao pode ser vazio."
  exit 1
fi

# Le versionName e versionCode do build.gradle
CURRENT_VERSION="$(grep -E 'versionName\s+"[0-9]+\.[0-9]+\.[0-9]+"' "$BUILD_GRADLE" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')"
CURRENT_CODE="$(grep -E 'versionCode\s+[0-9]+' "$BUILD_GRADLE" | grep -oE '[0-9]+')"

if [[ -z "$CURRENT_VERSION" ]]; then
  echo "Erro: nao foi possivel encontrar versionName no build.gradle"
  exit 1
fi

IFS='.' read -r MAJOR MINOR PATCH <<< "$CURRENT_VERSION"

case "$VERSION_TYPE" in
  major)
    MAJOR=$((MAJOR + 1))
    MINOR=0
    PATCH=0
    ;;
  minor)
    MINOR=$((MINOR + 1))
    PATCH=0
    ;;
  patch)
    PATCH=$((PATCH + 1))
    ;;
esac

NEW_VERSION="$MAJOR.$MINOR.$PATCH"
NEW_CODE=$((CURRENT_CODE + 1))

# Atualiza versionName e versionCode no build.gradle
sed -i "s/versionName \"$CURRENT_VERSION\"/versionName \"$NEW_VERSION\"/" "$BUILD_GRADLE"
sed -i "s/versionCode $CURRENT_CODE/versionCode $NEW_CODE/" "$BUILD_GRADLE"

echo "Versao atual: $CURRENT_VERSION (code $CURRENT_CODE)"
echo "Nova versao:  $NEW_VERSION (code $NEW_CODE)"

git -C "$ROOT_DIR" add -A

if git -C "$ROOT_DIR" diff --cached --quiet; then
  echo "Nenhuma alteracao para commitar."
  exit 0
fi

git -C "$ROOT_DIR" commit -m "$COMMIT_MESSAGE"

echo "Commit criado com sucesso."

git -C "$ROOT_DIR" push
