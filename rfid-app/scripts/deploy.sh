#!/usr/bin/env bash
set -e

# Garante que os comandos rodam sempre a partir da raiz do projeto
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$PROJECT_ROOT"

PACKAGE="com.smartx.rfidreader"
APK="app/build/outputs/apk/debug/app-debug.apk"

# ── 1. Verifica ADB ──────────────────────────────────────────────────────────
if ! command -v adb &>/dev/null; then
  echo "Erro: adb não encontrado. Adicione o Android SDK/platform-tools ao PATH."
  exit 1
fi

# ── 2. Lista devices conectados ──────────────────────────────────────────────
mapfile -t DEVICES < <(adb devices | awk 'NR>1 && $2=="device" {print $1}')

if [ ${#DEVICES[@]} -eq 0 ]; then
  echo "Nenhum dispositivo conectado. Verifique o cabo e o USB Debugging."
  exit 1
fi

# ── 3. Seleciona device ───────────────────────────────────────────────────────
if [ ${#DEVICES[@]} -eq 1 ]; then
  SERIAL="${DEVICES[0]}"
  echo "Dispositivo: $SERIAL"
else
  echo "Múltiplos dispositivos encontrados:"
  for i in "${!DEVICES[@]}"; do
    MODEL=$(adb -s "${DEVICES[$i]}" shell getprop ro.product.model 2>/dev/null | tr -d '\r')
    printf "  [%d] %s  (%s)\n" "$((i+1))" "${DEVICES[$i]}" "$MODEL"
  done
  echo ""
  read -rp "Escolha o número do dispositivo: " CHOICE
  INDEX=$((CHOICE - 1))
  if [[ $INDEX -lt 0 || $INDEX -ge ${#DEVICES[@]} ]]; then
    echo "Opção inválida."
    exit 1
  fi
  SERIAL="${DEVICES[$INDEX]}"
  echo "Usando: $SERIAL"
fi

# ── 4. Build ──────────────────────────────────────────────────────────────────
echo ""
echo "▶ Build..."
./gradlew assembleDebug --quiet

if [ ! -f "$APK" ]; then
  echo "Erro: APK não encontrado em $APK"
  exit 1
fi

# ── 5. Instalar ───────────────────────────────────────────────────────────────
echo "▶ Instalando no dispositivo $SERIAL..."
adb -s "$SERIAL" install -r "$APK"

# ── 6. Iniciar app ────────────────────────────────────────────────────────────
echo "▶ Iniciando $PACKAGE..."
adb -s "$SERIAL" shell monkey -p "$PACKAGE" -c android.intent.category.LAUNCHER 1 &>/dev/null

echo ""
echo "✓ Pronto! App aberto no dispositivo $SERIAL."
