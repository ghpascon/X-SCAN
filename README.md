# X-SCAN

Aplicativo Android para leitura de tags RFID UHF em coletores/handhelds industriais.  
Suporta múltiplos modelos de leitor por meio de uma interface unificada; adicionar um novo modelo exige implementar **uma única interface** e registrá-la em **um único lugar**.

---

## Sumário

1. [Visão Geral](#1-visão-geral)
2. [Arquitetura](#2-arquitetura)
3. [Estrutura de Pacotes](#3-estrutura-de-pacotes)
4. [Camada Core — Contratos e Dados](#4-camada-core--contratos-e-dados)
   - 4.1 [IRfidReader](#41-irfidreader)
   - 4.2 [RfidTag](#42-rfidtag)
   - 4.3 [ReaderConfig](#43-readerconfig)
   - 4.4 [ReaderConnectionState](#44-readerconnectionstate)
   - 4.5 [ReaderRegistry](#45-readerregistry)
5. [Leitores Implementados](#5-leitores-implementados)
   - 5.1 [AT907 (Chainway)](#51-at907-chainway)
   - 5.2 [C72 (Chainway)](#52-c72-chainway)
   - 5.3 [IH25 (Honeywell) — BLE](#53-ih25-honeywell--ble)
6. [Como Adicionar um Novo Leitor](#6-como-adicionar-um-novo-leitor)
7. [Camada de Persistência](#7-camada-de-persistência)
   - 7.1 [AppSettings / DataStore](#71-appsettings--datastore)
   - 7.2 [Room Database](#72-room-database)
   - 7.3 [EventRepository](#73-eventrepository)
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

| Item            | Valor                             |
| --------------- | --------------------------------- |
| Package         | `com.smartx.rfidreader`           |
| `applicationId` | `com.smartx.rfidreader`           |
| `versionName`   | `2.0.0`                           |
| `minSdk`        | 24 (Android 7.0)                  |
| `targetSdk`     | 34 (Android 14)                   |
| Linguagem       | Kotlin                            |
| UI              | ViewBinding + Fragments           |
| Concorrência    | Coroutines + StateFlow/SharedFlow |

O app permite:

- Conectar ao leitor RFID embutido ou BLE do coletor
- Realizar inventário de tags UHF com filtro de prefixo EPC e filtro de RSSI
- Exibir em tempo real as tags lidas (EPC, TID, RSSI, contagem)
- Salvar inventários localmente com coordenadas GPS
- Sincronizar os inventários com um servidor remoto via HTTP POST (webhook)

---

## 2. Arquitetura

```
┌─────────────────────────────────────────────────────┐
│                    UI Layer                          │
│  MainActivity  ──►  HomeFragment                    │
│                        ├─► ReaderSelectionFragment  │
│                        │       └─► BleScanDialog     │
│                        ├─► ReadingFragment           │
│                        └─► ConfigFragment            │
│  SyncActivity  ──►  (histórico + progresso)         │
└────────────────────┬────────────────────────────────┘
                     │  observe / call
┌────────────────────▼────────────────────────────────┐
│               MainViewModel                          │
│  - uiState: StateFlow<MainUiState>                  │
│  - tags: StateFlow<List<RfidTag>>        (throttled) │
│  - buzzerEvent: SharedFlow<Unit>         (throttled) │
│  - displayLimit: StateFlow<Int?>                     │
│  - navigateToReading / saveInventoryResult           │
└────────────────────┬────────────────────────────────┘
                     │  IRfidReader.tagFlow / connect / etc.
┌────────────────────▼────────────────────────────────┐
│              Reader Layer (readers/*)                │
│  AT907Reader  │  C72Reader  │  IH25Reader            │
│        implements IRfidReader                        │
└────────────────────┬────────────────────────────────┘
                     │
        Hardware SDK (AAR locais em /libs)
┌────────────────────▼────────────────────────────────┐
│              Core / Data Layer                       │
│  AppSettingsRepository (DataStore)                   │
│  EventRepository → Room DB + OkHttp webhook          │
│  GpsHelper (FusedLocationProvider)                   │
└─────────────────────────────────────────────────────┘
```

---

## 3. Estrutura de Pacotes

```
app/src/main/java/com/smartx/rfidreader/
│
├── RfidApplication.kt               # Application: instancia AppDatabase / EventRepository
│
├── core/
│   ├── reader/
│   │   ├── IRfidReader.kt           # Interface unificada — CONTRATO PRINCIPAL
│   │   ├── RfidTag.kt               # Data class de uma tag lida
│   │   ├── ReaderConfig.kt          # Configurações do leitor (potência, session, modo)
│   │   └── ReaderConnectionState.kt # Enum: DISCONNECTED | CONNECTING | CONNECTED | ERROR
│   │
│   ├── registry/
│   │   └── ReaderRegistry.kt        # Lista de leitores disponíveis — REGISTRAR AQUI
│   │
│   ├── settings/
│   │   └── AppSettings.kt           # Data class de configurações + DataStore repository
│   │
│   ├── db/
│   │   ├── AppDatabase.kt           # Room: singleton, version 2
│   │   ├── EventDao.kt              # Queries Room
│   │   └── EventEntity.kt           # Entidade `rfid_events` + toWebhookJson()
│   │
│   ├── events/
│   │   └── EventRepository.kt       # CRUD de eventos + HTTP POST para webhook
│   │
│   └── location/
│       └── GpsHelper.kt             # Captura localização GPS (FusedLocation, timeout 5s)
│
├── readers/
│   ├── at907/
│   │   └── AT907Reader.kt           # SDK ATID (AAR)
│   ├── c72/
│   │   └── C72Reader.kt             # SDK Chainway DeviceAPI (AAR)
│   └── ih25/
│       └── IH25Reader.kt            # SDK Honeywell RFID (AAR) — BLE
│
└── ui/
    ├── main/
    │   ├── MainActivity.kt          # Single-Activity host; captura KeyEvent de gatilho
    │   ├── MainViewModel.kt         # ViewModel compartilhado (activityViewModels)
    │   ├── MainPagerAdapter.kt      # ViewPager2: abas Home
    │   ├── HomeFragment.kt          # Tela inicial (botões: Leitura, Config, Sync, Sair)
    │   ├── reader/
    │   │   ├── ReaderSelectionFragment.kt  # Lista de leitores disponíveis
    │   │   ├── BleScanDialogFragment.kt    # Dialog de scan BLE (para IH25)
    │   │   └── BleDeviceAdapter.kt         # Adapter da lista BLE
    │   ├── reading/
    │   │   ├── ReadingFragment.kt   # UI de inventário + RecyclerView de tags
    │   │   └── TagListAdapter.kt    # ListAdapter com DiffCallback (EPC, TID, RSSI, contagem)
    │   └── config/
    │       └── ConfigFragment.kt    # Configurações do leitor (potência, session, modo)
    │
    ├── selection/ (legado)          # ReaderSelectionActivity — não usado como launcher
    │
    └── sync/
        ├── SyncActivity.kt          # Tela de histórico de inventários + envio
        ├── SyncViewModel.kt         # Lógica de progresso de envio
        ├── EventListAdapter.kt      # Lista de EventEntity
        └── SyncProgressAdapter.kt  # Progresso por evento durante sync
```

---

## 4. Camada Core — Contratos e Dados

### 4.1 `IRfidReader`

**Arquivo:** `core/reader/IRfidReader.kt`

Interface que deve ser implementada por **todo leitor RFID**. O ViewModel e o restante do app só interagem com esta interface — nunca com as classes concretas.

```kotlin
interface IRfidReader {
    val readerId: String           // ID único como "AT907", "C72", "IH25"
    val displayName: String        // Nome de exibição como "Chainway AT907"
    val isBle: Boolean             // true → fluxo de seleção BLE é ativado

    val connectionState: StateFlow<ReaderConnectionState>
    val tagFlow: Flow<RfidTag>     // Tags chegam aqui durante o inventário

    suspend fun connect(context: Context): Boolean
    suspend fun disconnect()
    fun startInventory(): Boolean
    fun stopInventory(): Boolean
    fun isInventorying(): Boolean
    suspend fun applyConfig(config: ReaderConfig): Boolean
    suspend fun readConfig(): ReaderConfig
    fun onTriggerPressed(): Boolean
    fun onTriggerReleased(): Boolean
}
```

**Regras importantes:**

- `connect()` deve atualizar `connectionState` para `CONNECTING` imediatamente, e então para `CONNECTED` ou `ERROR`.
- `tagFlow` deve ser um `SharedFlow` com `extraBufferCapacity ≥ 64` para não bloquear o SDK.
- `onTriggerPressed()` / `onTriggerReleased()` são chamados pelo `MainActivity` quando o botão físico é pressionado. O comportamento padrão é iniciar/parar inventário.
- O `readerId` deve ser único e estável (usado para re-conexão automática via DataStore).

---

### 4.2 `RfidTag`

**Arquivo:** `core/reader/RfidTag.kt`

```kotlin
data class RfidTag(
    val epc: String,
    val rssi: String,           // String para compatibilidade entre SDKs (ex: "-65.0", "-65")
    val tid: String = "",       // Vazio se o SDK não suportar ou o modo não estiver ativo
    val readCount: Int = 1,
    val timestamp: Date = Date()
)
```

O `epc` é a chave de deduplicação no `LinkedHashMap` do ViewModel.

---

### 4.3 `ReaderConfig`

**Arquivo:** `core/reader/ReaderConfig.kt`

Representa a configuração que pode ser lida/escrita no leitor:

```kotlin
data class ReaderConfig(
    val txPower: Int = 30,                          // dBm (5–33)
    val session: Int = 1,                           // Gen2 session 0–3
    val region: Int = 0x02,                         // Frequência (varia por SDK)
    val rssiFilter: Int = -120,                     // RSSI mínimo (dBm); -120 = sem filtro
    val inventoryMode: InventoryMode = EPC_TID
) {
    enum class InventoryMode { EPC_ONLY, EPC_TID, EPC_TID_USER }
}
```

> **Atenção às unidades de potência por SDK:**
>
> | Leitor | Unidade interna do SDK     | Conversão                                          |
> | ------ | -------------------------- | -------------------------------------------------- |
> | AT907  | Décimos de dBm             | `txPower × 10` → `setPower(300)` = 30 dBm          |
> | C72    | dBm                        | direto `setPower(30)`                              |
> | IH25   | Centidécimos de dBm (cdBm) | `txPower × 100` → `setAntennaPower(3000)` = 30 dBm |

Ao implementar um novo leitor, **sempre converta** para/de `dBm` (unidade padrão do app) nas funções `applyConfig()` e `readConfig()`.

---

### 4.4 `ReaderConnectionState`

```kotlin
enum class ReaderConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}
```

---

### 4.5 `ReaderRegistry`

**Arquivo:** `core/registry/ReaderRegistry.kt`

Lista centrale de todos os leitores disponíveis. **Este é o único lugar onde um novo leitor precisa ser registrado.**

```kotlin
object ReaderRegistry {
    val availableReaders: List<IRfidReader> by lazy {
        listOf(
            AT907Reader(),
            C72Reader(),
            IH25Reader()
            // ← adicione NovoLeitorReader() aqui
        )
    }

    fun findById(readerId: String): IRfidReader? =
        availableReaders.firstOrNull { it.readerId == readerId }
}
```

---

## 5. Leitores Implementados

### 5.1 AT907 (Chainway)

**Arquivo:** `readers/at907/AT907Reader.kt`  
**SDK:** `com.atid.lib.dev` (AAR: `atid.dev.rfid_v2.32.*` + auxiliares)

| Característica      | Detalhe                                                                                       |
| ------------------- | --------------------------------------------------------------------------------------------- |
| `readerId`          | `"AT907"`                                                                                     |
| `isBle`             | `false` — módulo RFID embarcado                                                               |
| Conexão             | `ATRfidManager.getInstance().connect()`                                                       |
| Inventário          | `r.inventory6cTag()`                                                                          |
| Unidade de potência | Décimos de dBm (`30 dBm → 300`)                                                               |
| TID                 | Não suportado neste SDK — `tid` sempre vazio                                                  |
| Thread obrigatória  | **Main thread** — SDK cria Handlers internamente                                              |
| Gatilho físico      | Broadcast `android.rfid.FUN_KEY` / `android.intent.action.FUN_KEY` com keycodes 133, 134, 135 |
| EPC strip           | SDK prefixa 2 bytes de PC word na string EPC → remove os 4 primeiros chars hex                |

---

### 5.2 C72 (Chainway)

**Arquivo:** `readers/c72/C72Reader.kt`  
**SDK:** `com.rscja.deviceapi` (AAR: `DeviceAPI_ver20251103_release`)

| Característica      | Detalhe                                                                     |
| ------------------- | --------------------------------------------------------------------------- |
| `readerId`          | `"C72"`                                                                     |
| `isBle`             | `false` — módulo RFID UART embarcado                                        |
| Conexão             | `RFIDWithUHFA8.getInstance().init(context)`                                 |
| Inventário          | `r.startInventoryTag()` com `IUHFInventoryCallback`                         |
| Unidade de potência | dBm direto (`setPower(30)`)                                                 |
| TID                 | Suportado: `setEPCAndTIDMode()` habilita antes de `startInventory()`        |
| Thread              | IO seguro                                                                   |
| Gatilho físico      | `KeyEvent.KEYCODE_F1` ou `KEYCODE_CAMERA` — capturado em `dispatchKeyEvent` |

---

### 5.3 IH25 (Honeywell) — BLE

**Arquivo:** `readers/ih25/IH25Reader.kt`  
**SDK:** `com.honeywell.rfidservice` (AAR: `honeywell_rfid_sdk`)

| Característica      | Detalhe                                                                                                                               |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| `readerId`          | `"IH25"`                                                                                                                              |
| `isBle`             | `true` — connecta via Bluetooth LE                                                                                                    |
| Conexão             | `RfidManager.connect(targetMacAddress)` assíncrono; resultado via `EventListener.onReaderCreated()`                                   |
| Inventário          | `reader.read(TagAdditionData.TID_BANK, option)`                                                                                       |
| Unidade de potência | cdBm (`30 dBm → 3000`)                                                                                                                |
| TID                 | Lido via `TagReadData.getAdditionData()` → bytes → hex string                                                                         |
| Gatilho físico      | `EventListener.onRfidTriggered(pressed)` — gerenciado pelo próprio SDK                                                                |
| Fluxo BLE           | Antes de `connect()`, o campo `targetMacAddress` deve ser preenchido com o MAC do dispositivo selecionado via `BleScanDialogFragment` |

#### Fluxo de conexão IH25

```
connect(context)
    └─► RfidManager.connect(mac)
            └─► EventListener.onDeviceConnected()
                    └─► rfidManager.createReader()
                            └─► EventListener.onReaderCreated(success, reader)
                                    └─► reader.setOnTagReadListener(...)
                                    └─► rfidManager.setTriggerMode(RFID)
                                    └─► connectionState = CONNECTED
```

---

## 6. Como Adicionar um Novo Leitor

Siga os passos abaixo. Nenhuma outra alteração na UI ou no ViewModel é necessária.

### Passo 1 — Criar o arquivo AAR da SDK

Copie o `.aar` da SDK do novo leitor para:

```
app/libs/nome_do_sdk.aar
```

Adicione em `app/build.gradle`:

```groovy
implementation(name: 'nome_do_sdk', ext: 'aar')
```

---

### Passo 2 — Criar a classe Reader

Crie `readers/novomodelo/NovoModeloReader.kt`:

```kotlin
package com.smartx.rfidreader.readers.novomodelo

import android.content.Context
import com.smartx.rfidreader.core.reader.*
import kotlinx.coroutines.flow.*

class NovoModeloReader : IRfidReader {

    override val readerId = "NOVO_MODELO"       // ID único
    override val displayName = "Marca NovoModelo"
    override val isBle = false                  // true se for BLE

    private val _connectionState = MutableStateFlow(ReaderConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<ReaderConnectionState> = _connectionState.asStateFlow()

    private val _tagChannel = MutableSharedFlow<RfidTag>(extraBufferCapacity = 64)
    override val tagFlow: Flow<RfidTag> = _tagChannel.asSharedFlow()

    override suspend fun connect(context: Context): Boolean {
        _connectionState.value = ReaderConnectionState.CONNECTING
        // TODO: inicializar SDK
        // Se ok:
        _connectionState.value = ReaderConnectionState.CONNECTED
        return true
    }

    override suspend fun disconnect() {
        // TODO: liberar SDK
        _connectionState.value = ReaderConnectionState.DISCONNECTED
    }

    override fun startInventory(): Boolean {
        // TODO: iniciar leitura
        // Para cada tag: _tagChannel.tryEmit(RfidTag(epc = "...", rssi = "..."))
        return true
    }

    override fun stopInventory(): Boolean {
        // TODO: parar leitura
        return true
    }

    override fun isInventorying(): Boolean = false // retorne o estado real

    override suspend fun applyConfig(config: ReaderConfig): Boolean {
        // IMPORTANTE: converter config.txPower (dBm) para a unidade da SDK
        return true
    }

    override suspend fun readConfig(): ReaderConfig {
        // IMPORTANTE: converter unidade da SDK de volta para dBm
        return ReaderConfig()
    }

    override fun onTriggerPressed(): Boolean {
        if (!isInventorying()) startInventory()
        return true
    }

    override fun onTriggerReleased(): Boolean {
        if (isInventorying()) stopInventory()
        return true
    }
}
```

**Atenção a conversões de potência** — veja a tabela em [4.3 ReaderConfig](#43-readerconfig).

---

### Passo 3 — Registrar no ReaderRegistry

Em `core/registry/ReaderRegistry.kt`:

```kotlin
val availableReaders: List<IRfidReader> by lazy {
    listOf(
        AT907Reader(),
        C72Reader(),
        IH25Reader(),
        NovoModeloReader()   // ← adicionar aqui
    )
}
```

✅ Pronto. A UI vai exibir o novo leitor automaticamente na tela de seleção.

---

### Passo 4 — Gatilho físico (se necessário)

Se o novo leitor usa um keycode diferente para o botão físico, adicione-o em `MainActivity`:

```kotlin
// Para keycodes KeyEvent padrão:
private val TRIGGER_KEYCODES = intArrayOf(
    KeyEvent.KEYCODE_F1,
    KeyEvent.KEYCODE_FOCUS,
    293,
    // ← adicionar novo keycode aqui
)

// Para leitores que usam BroadcastReceiver (como AT907):
val filter = IntentFilter().apply {
    addAction("android.rfid.FUN_KEY")
    addAction("seu.custom.ACTION")   // ← adicionar aqui
}
```

---

### Passo 5 — BLE (se `isBle = true`)

Se o leitor usa BLE, o campo `targetMacAddress` (ou equivalente) deve ser populado antes de `connect()`. O `ReaderSelectionFragment` detecta `isBle == true` e abre automaticamente o `BleScanDialogFragment` para o usuário selecionar o dispositivo. O resultado (MAC selecionado) é passado ao `IH25Reader` antes de chamar `connect()`.

Para um novo leitor BLE, adicione a propriedade de MAC na classe e certifique-se que `ReaderSelectionFragment.onReaderSelected()` a popule da mesma forma que faz com `IH25Reader`.

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
| `buzzerEvent`         | `SharedFlow<Unit>`         | Emitido apenas para **novas** tags, com throttle de 300 ms |
| `navigateToReading`   | `SharedFlow<Unit>`         | Emitido após conexão bem-sucedida                          |
| `saveInventoryResult` | `SharedFlow<Boolean>`      | `true` = sucesso, `false` = erro ao salvar                 |

### Throttle de UI (performance)

O SDK do IH25 pode entregar dezenas de tags por segundo. Para evitar que o RecyclerView e o DiffUtil travem a UI:

- Tags chegam via `tagFlow`, são inseridas/atualizadas em `_tagMap` (LinkedHashMap — O(1))
- Um flag `_tagsDirty` é setado a cada tag recebida
- Um ticker coroutine roda a cada **200 ms** e só emite `_tags.value = ...` se `_tagsDirty == true`
- O buzzer só é emitido para **novas** tags (não re-leituras) e no máximo **1x a cada 300 ms**

---

## 9. Telas (Fragments / Activities)

### `MainActivity`

Host único (Single-Activity). Responsabilidades:

- Hospedar os Fragments em `fragmentContainer`
- Capturar `KeyEvent` do gatilho físico e delegar a `viewModel.onTriggerPressed/Released()`
- Registrar `BroadcastReceiver` para o AT907 (broadcast de tecla especial)
- Gerenciar back stack

### `HomeFragment`

Tela inicial com 4 botões: **Conectar Leitor**, **Leitura**, **Configurações**, **Sincronização**.  
Auto-conecta ao último leitor ao iniciar (via `viewModel.autoConnectLastReader()`).

### `ReaderSelectionFragment`

Lista todos os leitores do `ReaderRegistry`. Ao clicar:

- Se `isBle == true` → abre `BleScanDialogFragment` primeiro
- Caso contrário → chama `viewModel.connect(reader)` diretamente

### `BleScanDialogFragment`

Dialog de scan BLE para o IH25:

- Lista dispositivos BLE próximos
- Ao selecionar: popula `IH25Reader.targetMacAddress` e inicia `viewModel.connect()`

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
│    Keycodes: F1, FOCUS, 293, HEADSETHOOK, BUTTON_R1          │
│    → MainActivity.dispatchKeyEvent()                         │
│       └─► viewModel.onTriggerPressed() / onTriggerReleased() │
│                                                              │
│ 2. BroadcastReceiver (AT907)                                 │
│    Actions: "android.rfid.FUN_KEY" / "android.intent.action.FUN_KEY" │
│    Keycodes: 133, 134, 135                                   │
│    → at907TriggerReceiver.onReceive()                        │
│       └─► viewModel.onTriggerPressed() / onTriggerReleased() │
│                                                              │
│ 3. Callback do SDK (IH25)                                    │
│    → EventListener.onRfidTriggered(pressed)                  │
│       └─► IH25Reader.onTriggerPressed() / onTriggerReleased()│
└─────────────────────────────────────────────────────────────┘
                          │
               viewModel.onTriggerPressed()
                          │
               reader.onTriggerPressed()
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

| Biblioteca                                         | Versão   | Uso                               |
| -------------------------------------------------- | -------- | --------------------------------- |
| `androidx.core:core-ktx`                           | 1.13.1   | Extensões Kotlin                  |
| `androidx.appcompat:appcompat`                     | 1.7.0    | Compatibilidade                   |
| `com.google.android.material:material`             | 1.12.0   | Material Design (Chips, Snackbar) |
| `androidx.constraintlayout:constraintlayout`       | 2.1.4    | Layouts                           |
| `androidx.viewpager2:viewpager2`                   | 1.1.0    | ViewPager                         |
| `androidx.recyclerview:recyclerview`               | 1.3.2    | Listas                            |
| `androidx.datastore:datastore-preferences`         | 1.1.1    | Configurações persistentes        |
| `androidx.room:room-runtime` + `room-ktx`          | 2.6.1    | Banco de dados local              |
| `com.squareup.okhttp3:okhttp`                      | 4.12.0   | HTTP POST para webhook            |
| `com.google.android.gms:play-services-location`    | 21.3.0   | GPS (FusedLocation)               |
| `androidx.lifecycle:lifecycle-viewmodel-ktx`       | 2.8.5    | ViewModel + coroutines            |
| `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.8.1    | Coroutines                        |
| SDK AT907 (AAR)                                    | v2.32    | `com.atid.lib.dev`                |
| SDK C72 (AAR)                                      | 20251103 | `com.rscja.deviceapi`             |
| SDK IH25 (AAR)                                     | —        | `com.honeywell.rfidservice`       |

---

## 14. Build e Deploy

### Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 17
- `adb` configurado e dispositivo conectado

### Build debug

```bash
cd rfid-app
./gradlew assembleDebug
```

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

Declaradas no `AndroidManifest.xml`:

| Permissão                                         | Motivo                                       |
| ------------------------------------------------- | -------------------------------------------- |
| `BLUETOOTH` / `BLUETOOTH_ADMIN`                   | Bluetooth clássico (API ≤ 30)                |
| `BLUETOOTH_CONNECT` (API 31+)                     | Conectar ao IH25 via BLE                     |
| `BLUETOOTH_SCAN` (API 31+)                        | Scan de dispositivos BLE                     |
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | Obrigatória para BLE scan em API 23–30 + GPS |
| `INTERNET`                                        | HTTP POST para webhook                       |
| `FOREGROUND_SERVICE`                              | Serviços de fundo (SDKs internos)            |
| `VIBRATE`                                         | Feedback físico (SDKs internos)              |

> `android:usesCleartextTraffic="true"` está habilitado para suportar webhooks em `http://` (HTTP não criptografado). Em produção considere HTTPS.
