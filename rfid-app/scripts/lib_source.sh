#!/usr/bin/env bash
# Alterna de onde o app obtém a biblioteca smartx-rfid (rfid-app/config.json -> library).
#
#   ./scripts/lib_source.sh status                 mostra a configuração atual
#   ./scripts/lib_source.sh local   [caminho]      usa o código-fonte local (includeBuild)
#   ./scripts/lib_source.sh remote  [versão]       usa o GitHub Packages
#   ./scripts/lib_source.sh maven-local [versão]   usa ~/.m2 (rode antes: smartx-rfid-android/scripts/publish_local.sh)
#   ./scripts/lib_source.sh version <versão>       só troca a versão da lib
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
CONFIG="$PROJECT_ROOT/config.json"

command -v jq >/dev/null || { echo "Erro: jq não encontrado."; exit 1; }

update() { # update '<filtro jq>' [--arg k v ...]
  local filter="$1"; shift
  local tmp; tmp="$(mktemp)"
  jq "$@" "$filter" "$CONFIG" > "$tmp" && mv "$tmp" "$CONFIG"
}

show() {
  jq -r '.library | "source:    \(.source)\nversion:   \(.version)\nartifact:  \(.groupId):\(.artifactId)\nlocalPath: \(.localPath)\nremoteUrl: \(.remoteUrl)"' "$CONFIG"
}

cmd="${1:-status}"
case "$cmd" in
  status) show ;;
  local)
    update '.library.source = "local"'
    [[ -n "${2:-}" ]] && update '.library.localPath = $p' --arg p "$2"
    show ;;
  remote|maven-local)
    update '.library.source = $s' --arg s "$cmd"
    [[ -n "${2:-}" ]] && update '.library.version = $v' --arg v "$2"
    show
    if [[ "$cmd" == "remote" ]]; then
      echo
      echo "O Gradle tenta usar a sessão do gh CLI; o token precisa ter o escopo read:packages."
      echo "Alternativas: GITHUB_ACTOR/GITHUB_TOKEN ou gpr.user/gpr.key em ~/.gradle/gradle.properties."
    fi ;;
  version)
    [[ -n "${2:-}" ]] || { echo "Uso: $0 version <versão>"; exit 1; }
    update '.library.version = $v' --arg v "$2"
    show ;;
  *)
    sed -n '2,9p' "$0" | sed 's/^# \{0,1\}//'
    exit 1 ;;
esac
