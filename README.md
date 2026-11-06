# X-SCAN

Aplicativo Android para leitura de tags RFID UHF em coletores/handhelds industriais.
Toda a integração com leitores (C72, CF-H301, IH25, Zebra, TSL 1128, X714, XR2...) vive na biblioteca
[`smartx-rfid-android`](https://github.com/ghpascon/smartx-rfid-android); o app apenas consome a API da lib.
Nomes, versões e a origem da lib ficam centralizados em [`rfid-app/config.json`](rfid-app/config.json).

---

## Sumário

1. [Visão Geral](#1-visão-geral)
2. [Arquitetura](#2-arquitetura)
3. [Estrutura de Pacotes](#3-estrutura-de-pacotes)
4. [Configuração central (config.json)](#4-configuração-central-configjson)
5. [Biblioteca smartx-rfid](#5-biblioteca-smartx-rfid)
6. [Como adicionar um novo leitor](#6-como-adicionar-um-novo-leitor)
7. [Camada de Persistência](#7-camada-de-persistência)
8. [ViewModel Principal](#8-viewmodel-principal)
9. [Telas (Fragments / Activities)](#9-telas-fragments--activities)
10. [Fluxo de Gatilho Físico](#10-fluxo-de-gatilho-físico)
11. [Sincronização via Webhook](#11-sincronização-via-webhook)
12. [Payload do Webhook](#12-payload-do-webhook)
13. [Dependências](#13-dependências)
14. [Build e Deploy](#14-build-e-deploy)
15. [Permissões Android](#15-permissões-android)

---

## 1. Visão Geral

Os valores abaixo vêm de `rfid-app/config.json` (não edite `build.gradle`/`strings.xml` para isso).

| Item                 | Origem                                       |
| -------------------- | -------------------------------------------- |
| Título do app        | `app.title` (resValue `app_name`)            |
| `applicationId`      | `app.applicationId`                          |
| Versão do app        | `app.versionName` / `app.versionCode`        |
| `minSdk`/`targetSdk` | `app.minSdk` / `app.targetSdk`               |
| Serviço webhook      | `service.*` (título, texto, canal)           |
| Biblioteca RFID      | `library.*` (origem, versão, caminho)        |
| Leitores ativos      | Todos os leitores do `ReaderRegistry` da lib |

Stack: Kotlin, ViewBinding + Fragments, Coroutines + StateFlow/SharedFlow, Room, DataStore, OkHttp.

O app permite:

- Conectar ao leitor RFID embutido, BLE ou serial do coletor
- Realizar inventário de tags UHF com filtro de prefixo EPC e filtro de RSSI
- Exibir em tempo real as tags lidas (EPC, TID, RSSI, contagem)
- Salvar inventários localmente com coordenadas GPS
- Sincronizar os inventários com um servidor remoto via HTTP POST (webhook)

---

## 2. Arquitetura

```
┌─────────────────────────────────────────────────────┐
│ UI: MainActivity ► HomeFragment                      │
│       ├─► ReaderSelectionFragment ► ReaderConnectFlow│
│       │        └─► BleScanDialogFragment             │
│       ├─► ReadingFragment / RadarFragment            │
│       └─► ConfigFragment / WebhookFragment           │
│     SyncActivity (histórico + envio)                 │
└───────────────────────┬─────────────────────────────┘
                        │ observe / call
┌───────────────────────▼─────────────────────────────┐
│ MainViewModel  (regras de tela: settings, buzzer,    │
│                 navegação)                           │
└───────────────────────┬─────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────┐
│ RfidApplication.readerManager  (ReaderManager)       │
│   biblioteca smartx-rfid: leitores, conexão,         │
│   inventário, TagCollector, BleScanner               │
└───────────────────────┬─────────────────────────────┘
                        │ SDKs dos fabricantes (dentro da lib)
┌───────────────────────▼─────────────────────────────┐
│ Dados do app: AppSettingsRepository (DataStore),     │
│ EventRepository (Room + OkHttp), GpsHelper           │
└─────────────────────────────────────────────────────┘
```

O `ReaderManager` é único por processo (`RfidApplication.readerManager`) e é compartilhado pela UI e
pelo `WebhookService`.

---

## 3. Estrutura de Pacotes

```
rfid-app/
├── config.json                      # Configuração central do app (ver seção 4)
├── settings.gradle                  # Lê o config.json e resolve a lib (local/remote/maven-local)
├── scripts/                         # build.sh, deploy.sh, lib_source.sh
└── app/src/main/java/com/smartx/rfidreader/   (package com.smartx.xscan)
    ├── RfidApplication.kt           # Cria o ReaderManager com os leitores registrados na lib
    ├── core/
    │   ├── settings/                # AppSettings + DataStore
    │   ├── db/                      # Room (AppDatabase, EventDao, EventEntity)
    │   ├── events/EventRepository   # CRUD de eventos + HTTP POST
    │   ├── location/GpsHelper       # GPS (FusedLocation, timeout 5s)
    │   └── webhook/                 # WebhookService + status
    └── ui/
        ├── base/BaseActivity        # Header/footer com estado do leitor (ReaderManager.state)
        ├── main/                    # MainActivity, MainViewModel, Home, Reading, Radar, Config, Webhook
        │   └── reader/              # ReaderSelectionFragment, ReaderConnectFlow, BleScanDialogFragment,
        │                            # ConnectionLogDialogFragment
        └── sync/                    # SyncActivity + SyncViewModel
```

Não há mais drivers de leitor nem SDKs/AARs no app: tudo isso está na lib.

---

## 4. Configuração central (config.json)

`rfid-app/config.json` é a única fonte para os valores que antes estavam espalhados:

```json
{
  "app": {
    "title": "X-SCAN",
    "applicationId": "com.smartx.xscan",
    "versionName": "6.5.3",
    "versionCode": 52,
    "minSdk": 29,
    "targetSdk": 34,
    "compileSdk": 34,
    "exportFilePrefix": "xscan_tags_",
    "headerLogo": "smtx.png",
    "launcherIcon": "logo.png"
  },
  "service": {
    "name": "...",
    "text": "...",
    "channelId": "webhook_channel",
    "channelName": "Webhook"
  },
  "library": {
    "source": "local",
    "version": "1.0.0",
    "groupId": "com.smartx.rfid",
    "artifactId": "smartx-rfid",
    "localPath": "../smartx-rfid-android",
    "remoteUrl": "https://maven.pkg.github.com/ghpascon/smartx-rfid-android"
  },
  "readers": ["C72", "CF-H301", "IH25", "ZEBRA", "TSL1128", "X714", "XR2"]
}
```

| Chave                  | Efeito                                                                             |
| ---------------------- | ---------------------------------------------------------------------------------- |
| `app.title`            | Nome do app (`@string/app_name`, launcher e header)                                |
| `app.headerLogo`       | PNG em `rfid-app/static/` usado no logo do header                                  |
| `app.launcherIcon`     | PNG em `rfid-app/static/` usado no ícone do app e nos itens que exibem a marca     |
| `app.versionName/Code` | Versão do APK (o `scripts/commit.sh` incrementa automaticamente)                   |
| `service.*`            | Título/texto da notificação e canal do `WebhookService`                            |
| `app.exportFilePrefix` | Prefixo dos CSVs exportados                                                        |
| `library.source`       | `local` (código-fonte em `localPath`), `remote` (GitHub Packages) ou `maven-local` |
| `library.version`      | Versão da lib usada em `remote`/`maven-local`                                      |
| `library.version`      | Também determina quais leitores estão registrados na lib                           |

No app, esses valores chegam via `BuildConfig` (`LIB_VERSION`, `WEBHOOK_CHANNEL_ID`,
`EXPORT_FILE_PREFIX`) e `resValue`.

### Trocar a origem da lib

```bash
cd rfid-app
./scripts/lib_source.sh status
./scripts/lib_source.sh local ../smartx-rfid-android   # desenvolvimento lado a lado
./scripts/lib_source.sh remote 1.0.1                   # GitHub Packages (GITHUB_ACTOR/GITHUB_TOKEN ou gpr.user/gpr.key)
./scripts/lib_source.sh maven-local 1.0.1              # após smartx-rfid-android/scripts/publish_local.sh
```

A pasta `smartx-rfid-android/` é um repositório git independente (ignorado por este repo via `.gitignore`).

---

## 5. Biblioteca smartx-rfid

Documentação completa em `smartx-rfid-android/README.md`. Resumo da API usada pelo app:

| Classe                                             | Papel                                                                                                                     |
| -------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| `ReaderManager`                                    | `readers`, `state`, `connect/disconnect`, `start/stop/toggleInventory`, `readConfig/applyConfig`, `connectionLog`, `tags` |
| `ReaderRegistry`                                   | Fonte dos leitores registrados na versão da lib; o app carrega todos com `createAll()`                                    |
| `IRfidReader`                                      | Contrato de um leitor (`capabilities`, `connectionOptions`, `tagFlow`, `connect`, ...)                                    |
| `ConnectionOption/Request`                         | Formas de conexão (`INTEGRATED`, `BLUETOOTH`, `SERIAL`) e endereço escolhido                                              |
| `TagCollector` / `TagFilter`                       | Deduplicação, throttle de ~200 ms, filtro por prefixo EPC e RSSI mínimo                                                   |
| `BleScanner` / `BlePermissions`                    | Scan BLE e permissões necessárias por versão do Android                                                                   |
| `RfidTag`, `ReaderConfig`, `ReaderConnectionState` | Modelos de dados                                                                                                          |

Publicação, versionamento e scripts (`commit.sh`, `get_remote_repo.sh`, `publish*.sh`) estão descritos no README da lib.

---

## 6. Como adicionar um novo leitor

Nada muda no app além do `config.json`:

1. Implemente o leitor na lib (`IRfidReader`) e registre-o em `ReaderRegistry` — veja o README da lib.
2. Publique uma nova versão da lib (`smartx-rfid-android/scripts/commit.sh`).
3. No app, ajuste `library.version` (ou use `local`). O app passa a carregar os leitores registrados nessa versão, sem editar uma lista duplicada.

A UI se adapta sozinha: `ReaderConnectFlow` usa `connectionOptions` para perguntar o transporte
(ex.: Zebra Bluetooth/Serial) e abre o scan BLE quando a opção exige dispositivo; `capabilities` define
auto-reconexão (`supportsAutoReconnect`) e gatilho por `KeyEvent` (`usesKeyEventTrigger`).

---

## 7. Camada de Persistência

### 7.1 `AppSettings` / DataStore

**Arquivo:** `core/settings/AppSettings.kt`

Preferências persistidas com Jetpack DataStore (chave-valor):

| Campo           | Tipo           | Padrão | Descrição                                                |
| --------------- | -------------- | ------ | -------------------------------------------------------- |
| `lastReaderId`  | `String`       | `""`   | ID do último leitor usado (para auto-connect)            |
| `buzzerEnabled` | `Boolean`      | `true` | Ativa/desativa bipe a cada nova tag                      |
| `rssiFilter`    | `Int`          | `-120` | Ignora tags com RSSI abaixo deste valor                  |
| `prefixes`      | `List<String>` | `[]`   | Filtra por prefixo EPC (separados por `\|` no DataStore) |
| `webhookUrl`    | `String`       | `""`   | URL do endpoint de sincronização                         |

---

### 7.2 Room Database

**Arquivo:** `core/db/AppDatabase.kt` — singleton, `rfid_reader.db`, versão 2

**Entidade `rfid_events` (`EventEntity`):**

| Campo               | Tipo      | Descrição                             |
| ------------------- | --------- | ------------------------------------- |
| `id`                | `Long`    | PK autogerado                         |
| `deviceId`          | `String`  | `Settings.Secure.ANDROID_ID`          |
| `eventType`         | `String`  | `"inventory"` (fixo)                  |
| `tagsJson`          | `String`  | Array JSON das tags lidas             |
| `savedAt`           | `String`  | ISO 8601 com fuso local               |
| `gpsLat` / `gpsLng` | `Double`  | Coordenadas GPS                       |
| `hasGps`            | `Boolean` | `false` se GPS não disponível         |
| `txPower`           | `Int`     | Potência em dBm no momento da captura |
| `session`           | `Int`     | Session Gen2 (0–3)                    |
| `rssiFilter`        | `Int`     | Filtro ativo                          |
| `prefixesJson`      | `String`  | Prefixos separados por `\|`           |
| `isSynced`          | `Boolean` | `false` até envio bem-sucedido        |
| `syncedAt`          | `String`  | ISO 8601 do momento do envio          |

---

### 7.3 `EventRepository`

**Arquivo:** `core/events/EventRepository.kt`

| Método                                   | Descrição                                               |
| ---------------------------------------- | ------------------------------------------------------- |
| `saveInventory(...)`                     | Serializa tags para JSON e insere `EventEntity`         |
| `sendPending(url)`                       | Envia todos os eventos `isSynced = false` via HTTP POST |
| `sendPendingWithProgress(url, callback)` | Idem, com callback por evento (usado na `SyncActivity`) |
| `deleteEvent(event)`                     | Apaga um evento do DB                                   |
| `deleteAllEvents()`                      | Limpa todo o histórico                                  |
| `allEventsFlow`                          | `Flow<List<EventEntity>>` — todos os eventos            |
| `pendingCountFlow`                       | `Flow<Int>` — quantidade de eventos pendentes           |

---

## 8. ViewModel Principal

**Arquivo:** `ui/main/MainViewModel.kt`

ViewModel compartilhado entre todos os Fragments via `activityViewModels()`.

### Flows expostos

| Flow                  | Tipo                       | Descrição                                                  |
| --------------------- | -------------------------- | ---------------------------------------------------------- |
| `uiState`             | `StateFlow<MainUiState>`   | Estado geral: conexão, inventário, config                  |
| `tags`                | `StateFlow<List<RfidTag>>` | Lista deduplicada, atualizada a cada **200 ms** (throttle) |
| `displayLimit`        | `StateFlow<Int?>`          | Limite de exibição: 50/100/200/null (todas)                |
| `buzzerEvent`         | `SharedFlow<Unit>`         | Emitido a cada leitura aceita, com cooldown de 100 ms      |
| `navigateToReading`   | `SharedFlow<Unit>`         | Emitido após conexão bem-sucedida                          |
| `saveInventoryResult` | `SharedFlow<Boolean>`      | `true` = sucesso, `false` = erro ao salvar                 |

### Responsabilidades

Conexão, inventário, config e tags são delegados ao `ReaderManager` (lib). O ViewModel mantém apenas:
`AppSettings` (DataStore), o buzzer (cooldown 100 ms por leitura), `displayLimit`, eventos de navegação e
o salvamento de inventário. O `TagFilter` da lib é atualizado a cada mudança em `AppSettings`.
`tags` já vem deduplicada e com throttle de ~200 ms (`TagCollector`).

---

## 9. Telas (Fragments / Activities)

### `MainActivity`

Host único (Single-Activity). Responsabilidades:

- Hospedar os Fragments em `fragmentContainer`
- Capturar `KeyEvent` do gatilho físico e delegar a `viewModel.onTriggerPressed/Released()`
- Gerenciar back stack

### `HomeFragment`

Tela inicial com 4 botões: **Conectar Leitor**, **Leitura**, **Configurações**, **Sincronização**.  
Auto-conecta ao último leitor ao iniciar (via `viewModel.autoConnectLastReader()`).

### `ReaderSelectionFragment`

Lista os leitores registrados na lib (`ReaderRegistry.createAll()`). Ao clicar, delega a `ReaderConnectFlow.start()`:

- mais de uma `connectionOptions` → diálogo para escolher a forma de conexão;
- opção com `requiresDevice` (Bluetooth) → `BleScanDialogFragment`, depois `viewModel.connect(reader, ConnectionRequest(option, address))`;
- demais casos → conecta direto e abre o log de conexão.

### `BleScanDialogFragment`

Usa `BleScanner`/`BlePermissions` da lib; lista dispositivos (pareados primeiro) e devolve o MAC via `onDeviceSelected`.

### `ReadingFragment`

Tela de inventário ativo:

- RecyclerView com `TagListAdapter` (DiffCallback por EPC)
- Botões: Iniciar/Parar Inventário, Limpar, Salvar
- Chips de limite: 50 / 100 / 200 / Todas
- `ToneGenerator` reutilizado (criado em `onStart`, liberado em `onStop`)

### `ConfigFragment`

Configurações do leitor conectado: potência (dBm), session Gen2, modo de inventário.

### `SyncActivity`

Histórico de inventários salvos com opção de:

- Enviar pendentes ao webhook
- Ver progresso por evento
- Excluir evento individual ou todo o histórico

---

## 10. Fluxo de Gatilho Físico

O gatilho físico pode chegar de **três formas** diferentes dependendo do hardware:

```
┌─────────────────────────────────────────────────────────────┐
│ 1. KeyEvent (padrão Android)                                 │
│    Keycodes: F1, FOCUS, 293, HEADSETHOOK, BUTTON_R1, 523    │
│    → MainActivity.dispatchKeyEvent()                         │
│       └─► viewModel.onTriggerPressed() / onTriggerReleased() │
│                                                              │
│ 2. Callback do SDK (IH25, Zebra, TSL, X714) — tratado        │
│    dentro da lib; chega como estado do ReaderManager         │
└─────────────────────────────────────────────────────────────┘
                          │
               viewModel.onTriggerPressed()
                          │
               ReaderManager.onTriggerPressed()
                          │
               reader.startInventory()  (ou stop)
```

---

## 11. Sincronização via Webhook

A sincronização é iniciada na `SyncActivity`. O fluxo:

1. `SyncViewModel.syncPending(url)` chama `EventRepository.sendPendingWithProgress()`
2. Para cada evento pendente: HTTP POST para `webhookUrl` com JSON no body
3. Se o servidor retornar `2xx`: evento é **deletado** do DB local
4. Se falhar: evento permanece pendente para a próxima tentativa
5. Progresso (current/total) é emitido como `Flow` e exibido no `SyncProgressAdapter`

---

## 12. Payload do Webhook

Cada inventário é enviado como um único HTTP POST `Content-Type: application/json`:

```json
{
  "device": "a1b2c3d4e5f6",
  "event_type": "inventory",
  "event_data": {
    "timestamp": "2024-04-22T14:30:00.000-03:00",
    "gps": {
      "lat": -23.5505,
      "lng": -46.6333
    },
    "reader_config": {
      "tx_power": 30,
      "session": 1,
      "rssi_filter": -70,
      "prefixes": ["E280"]
    },
    "tags": [
      {
        "epc": "E2801160600002064BEA4A11",
        "rssi": "-62.0",
        "tid": "E2003412012345678901234567",
        "read_count": 3
      }
    ]
  }
}
```

> - `gps` será `null` se o GPS não estiver disponível.
> - `tid` só aparece se o leitor suportar e o modo `EPC_TID` estiver ativo.
> - `device` = `Settings.Secure.ANDROID_ID` do aparelho.

---

## 13. Dependências

| Biblioteca                                         | Versão            | Uso                                         |
| -------------------------------------------------- | ----------------- | ------------------------------------------- |
| `androidx.core:core-ktx`                           | 1.13.1            | Extensões Kotlin                            |
| `androidx.appcompat:appcompat`                     | 1.7.0             | Compatibilidade                             |
| `com.google.android.material:material`             | 1.12.0            | Material Design (Chips, Snackbar)           |
| `androidx.constraintlayout:constraintlayout`       | 2.1.4             | Layouts                                     |
| `androidx.viewpager2:viewpager2`                   | 1.1.0             | ViewPager                                   |
| `androidx.recyclerview:recyclerview`               | 1.3.2             | Listas                                      |
| `androidx.datastore:datastore-preferences`         | 1.1.1             | Configurações persistentes                  |
| `androidx.room:room-runtime` + `room-ktx`          | 2.6.1             | Banco de dados local                        |
| `com.squareup.okhttp3:okhttp`                      | 4.12.0            | HTTP POST para webhook                      |
| `com.google.android.gms:play-services-location`    | 21.3.0            | GPS (FusedLocation)                         |
| `androidx.lifecycle:lifecycle-viewmodel-ktx`       | 2.8.5             | ViewModel + coroutines                      |
| `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.8.1             | Coroutines                                  |
| `com.smartx.rfid:smartx-rfid`                      | `library.version` | Leitores RFID (inclui SDKs dos fabricantes) |

---

## 14. Build e Deploy

### Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 17
- `adb` configurado e dispositivo conectado

### Build debug

```bash
cd rfid-app
./scripts/build.sh      # ou ./gradlew assembleDebug
./scripts/deploy.sh     # build + adb install + abre o app (usa app.applicationId do config.json)
```

Com `library.source = remote` são necessárias credenciais do GitHub Packages (`GITHUB_ACTOR`/`GITHUB_TOKEN`).

### Versão e commit

`./scripts/commit.sh` (raiz) incrementa `app.versionName`/`app.versionCode` no `rfid-app/config.json`, commita e faz push.

APK gerado: `app/build/outputs/apk/debug/app-debug.apk`

### Instalar no dispositivo

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Build + deploy em um comando

```bash
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Verificar erros de compilação

```bash
./gradlew assembleDebug --no-daemon 2>&1 | grep -E "error:|BUILD"
```

---

## 15. Permissões Android

Permissões Bluetooth (`BLUETOOTH*`, scan/connect) são declaradas pela lib e mescladas no manifest. Declaradas no `AndroidManifest.xml` do app:

| Permissão                                         | Motivo                                       |
| ------------------------------------------------- | -------------------------------------------- |
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | Obrigatória para BLE scan em API 23–30 + GPS |
| `INTERNET`                                        | HTTP POST para webhook                       |
| `FOREGROUND_SERVICE`                              | Serviços de fundo (SDKs internos)            |
| `VIBRATE`                                         | Feedback físico (SDKs internos)              |

> `android:usesCleartextTraffic="true"` está habilitado para suportar webhooks em `http://` (HTTP não criptografado). Em produção considere HTTPS.
