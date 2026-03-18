#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PUBSPEC_PATH="$ROOT_DIR/pubspec.yaml"

if [[ ! -f "$PUBSPEC_PATH" ]]; then
  echo "Erro: pubspec.yaml nao encontrado em $PUBSPEC_PATH"
  exit 1
fi

if ! git -C "$ROOT_DIR" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "Erro: este script precisa ser executado dentro de um repositorio git."
  exit 1
fi

echo "Tipo de versao:"
echo "  1) major"
echo "  2) minor"
echo "  3) patch"
read -r -p "Escolha (major/minor/patch): " VERSION_TYPE

case "${VERSION_TYPE,,}" in
  1) VERSION_TYPE="major" ;;
  2) VERSION_TYPE="minor" ;;
  3) VERSION_TYPE="patch" ;;
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

CURRENT_VERSION_LINE="$(grep -E '^version:[[:space:]]*[0-9]+\.[0-9]+\.[0-9]+(\+[0-9]+)?$' "$PUBSPEC_PATH" | head -n1 || true)"
if [[ -z "$CURRENT_VERSION_LINE" ]]; then
  echo "Erro: nao foi possivel encontrar uma linha de versao valida no pubspec.yaml"
  exit 1
fi

CURRENT_VERSION="${CURRENT_VERSION_LINE#version: }"
BASE_VERSION="$CURRENT_VERSION"
BUILD_NUMBER=""

if [[ "$CURRENT_VERSION" == *"+"* ]]; then
  BASE_VERSION="${CURRENT_VERSION%%+*}"
  BUILD_NUMBER="${CURRENT_VERSION##*+}"
fi

IFS='.' read -r MAJOR MINOR PATCH <<< "$BASE_VERSION"

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

if [[ -n "$BUILD_NUMBER" && "$BUILD_NUMBER" =~ ^[0-9]+$ ]]; then
  BUILD_NUMBER=$((BUILD_NUMBER + 1))
  NEW_VERSION="$MAJOR.$MINOR.$PATCH+$BUILD_NUMBER"
else
  NEW_VERSION="$MAJOR.$MINOR.$PATCH"
fi

# Atualiza apenas a primeira ocorrencia da linha de versao.
sed -i "0,/^version:[[:space:]]*[0-9]\+\.[0-9]\+\.[0-9]\+\(+[0-9]\+\)\?$/s//version: $NEW_VERSION/" "$PUBSPEC_PATH"

echo "Versao atual: $CURRENT_VERSION"
echo "Nova versao:  $NEW_VERSION"

git -C "$ROOT_DIR" add -A

if git -C "$ROOT_DIR" diff --cached --quiet; then
  echo "Nenhuma alteracao para commitar."
  exit 0
fi

git -C "$ROOT_DIR" commit -m "$COMMIT_MESSAGE"

echo "Commit criado com sucesso."
