# X-SCAN

Aplicativo Flutter com modulo RFID estruturado para escalar para varios leitores.

## Estrutura RFID

O modulo foi separado em camadas para facilitar manutencao e crescimento:

- `lib/core/rfid`: contratos e modelos (`RfidReader`, `RfidTag`, `RfidPlatformInfo`)
- `lib/services/rfid`: implementacao de plataforma e estado (`PlatformRfidReader`, `RfidController`)
- `lib/screens/rfid.dart`: UI de inventario simples (`RfidScreen`)

Na Home existe o atalho `RFID`.

## C72 - O Que Ja Esta Pronto

- Integracao oficial via `rfid_c72_plugin`
- Leitura continua via `RfidC72Plugin.startContinuous`
- Stream de tags via `RfidC72Plugin.tagsStatusStream`
- Parse de tags JSON usando `TagEpc.parseTags(...)`
- Tela simples que mostra EPC, RSSI e contagem de leituras

Arquivo principal Android da integracao:

- `android/app/src/main/kotlin/com/example/x_scan/MainActivity.kt`

## Setup Android Para Plugin C72

1. Dependencia Flutter adicionada:
   - `rfid_c72_plugin` via path local `plugins/rfid_c72_plugin` (patchado para AGP 8)
2. Modulo Android `:libs` configurado:
   - `android/settings.gradle.kts` com `include(":app", ":libs")`
   - `android/libs/build.gradle`
3. SDK nativo Chainway ja baixado em:
   - `android/libs/DeviceAPI_ver20220518_release.aar`
4. App ligado ao modulo `:libs`:
   - `android/app/build.gradle.kts` com `implementation(project(":libs"))`
5. Rode:
   - `flutter clean`
   - `flutter pub get`
   - `flutter run`
6. Abra a tela RFID e valide:
   - `SDK detectado: Sim`
   - botao `Iniciar Leitura`
   - tags aparecendo na lista

## Arquivos-Chave Da Integracao

- `lib/services/rfid/platform_rfid_reader.dart`
- `lib/services/rfid/rfid_controller.dart`
- `lib/screens/rfid.dart`
- `android/libs/build.gradle`

## Proximo Passo Recomendado

Se o seu firmware C72 tiver comportamentos diferentes (ex.: nao publicar RSSI ou erro de permissao),
me envie o log do `flutter run` que eu ajusto o parser/fluxo de leitura para o seu device.
