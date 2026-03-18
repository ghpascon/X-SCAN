# X-SCAN

Aplicativo Flutter com integracao RFID para handheld C72, suporta fila local, sincronizacao webhook e GPS.

## Features Implementadas

### RFID

- Leitura continua via `rfid_c72_plugin` (Chainway C72)
- Exibe apenas **novas tags** (descarta antigas de sessoes anteriores)
- Mostra EPC, TID, RSSI, contagem e timestamp
- Reconexao/limpeza de tags

### Persistencia Local

- Fila de eventos em `inventory_events.json`
- Dados salvos: device ID, GPS, tags com timestamp
- Retorna para Home apos salvar localmente

### Sincronizacao Webhook

- Configuracao de URL webhook nas Configuracoes do app
- Tela dedicada de execucao com:
  - Progresso (processados / total)
  - Contador de sucessos e falhas
  - Lista de motivos de falha (HTTP status, erro de rede, SSL, etc.)
- Comportamento: sucesso remove da fila, falha mantém para retry
- Recarrega fila ao voltar

### Fila

- Visualizar eventos pendentes
- Deletar evento individual
- Ver JSON completo do evento

### APP

- Tela inicial com navegacao
- Configuracoes: URL webhook persistida
- GPS integrado
- Barcode test (teclado)

## Estrutura RFID

Camadas:

- `lib/core/rfid`: contratos (`RfidReader`, `RfidTag`, `RfidPlatformInfo`)
- `lib/services/rfid`: implementacao (`PlatformRfidReader`, `RfidController`)
- `lib/screens/rfid.dart`: UI (`RfidScreen`)

Android:

- `android/app/src/main/kotlin/com/example/x_scan/rfid/UhfSdkManager.kt`: gerenciador de tags e logs
  - Filtra para exibir **apenas novas tags** no console
  - Marca tags antigas ao iniciar leitura

## Setup

1. `flutter clean && flutter pub get`
2. `flutter run -d HC72BA240900235` (ou seu device ID)

Valide:

- RFID conectado e SDK detectado
- Tags aparecendo ao iniciar leitura
- App Settings mostra webhook URL persistido

## Arquivos-Chave

- `lib/screens/rfid.dart`: UI inventario
- `lib/screens/sync.dart`: entrada da sincronizacao
- `lib/screens/sync_run.dart`: execucao e resultado
- `lib/screens/sync_queue.dart`: fila de eventos
- `lib/screens/app_settings.dart`: config webhook
- `lib/services/app_prefs.dart`: persistencia webhook
- `lib/services/rfid/rfid_controller.dart`: controller com session tracking
- `android/.../UhfSdkManager.kt`: logs apenas de novas tags
