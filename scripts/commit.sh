#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
CONFIG_JSON="$ROOT_DIR/rfid-app/config.json"

if [[ ! -f "$CONFIG_JSON" ]]; then
  echo "Erro: nao encontrou $CONFIG_JSON"
  exit 1
fi
command -v jq >/dev/null || { echo "Erro: jq nao encontrado."; exit 1; }

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

# Le versionName e versionCode do rfid-app/config.json
CURRENT_VERSION="$(jq -r '.app.versionName' "$CONFIG_JSON")"
CURRENT_CODE="$(jq -r '.app.versionCode' "$CONFIG_JSON")"

if [[ -z "$CURRENT_VERSION" ]]; then
  echo "Erro: nao foi possivel encontrar versionName no config.json"
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

# Atualiza versionName e versionCode no config.json
TMP_JSON="$(mktemp)"
jq --arg v "$NEW_VERSION" --argjson c "$NEW_CODE" '.app.versionName = $v | .app.versionCode = $c' "$CONFIG_JSON" > "$TMP_JSON"
mv "$TMP_JSON" "$CONFIG_JSON"

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
