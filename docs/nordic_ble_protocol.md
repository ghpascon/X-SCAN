# Especificação: Leitor BLE — Nordic UART

Objetivo

- Definir comportamento e protocolo para implementar um novo leitor BLE que comunica via Nordic UART (NUS).
- Fornecer regras de parsing, fluxo de setup e mapeamento de eventos para integrar com o app existente.

Resumo das regras importantes

- O dispositivo usa BLE (advertising + GATT). O comportamento de scan/connect deve se assemelhar ao do IH25 (campo `targetMacAddress`, mostrar disponível para conexão, etc.).
- Serviço/Característica (Padrão Nordic UART Service) — confirmar com seu leitor; valores padrão a usar se compatível:
  - Service UUID: `6E400001-B5A3-F393-E0A9-E50E24DCCA9E`
  - Characteristic (TX write from central -> peripheral): `6E400002-B5A3-F393-E0A9-E50E24DCCA9E` (escrever comandos)
  - Characteristic (RX notify from peripheral -> central): `6E400003-B5A3-F393-E0A9-E50E24DCCA9E` (receber mensagens)

Observações: confirme se seu leitor usa esses UUIDs; se não, me informe os UUIDs corretos.

Formato das mensagens vindas do leitor

- Mensagens chegam via characteristic RX (notificações). Receber e tratar como texto UTF-8.
- Fragmentação: notificações podem conter fragmentos ou múltiplas linhas. Implementar buffer por conexão e dividir por newline (`\n` ou `\r\n`).

Mensagens relevantes e ações

- #setup_done
  - Indica término bem-sucedido do setup inicial.
  - Durante a sequência de setup podemos receber várias respostas; apenas `#setup_done` importa para marcar _setup concluído_.
- `copilot_getNotebookSummary: on` / `copilot_getNotebookSummary: off`
  - Estes aparecem como parte das mensagens recebidas.
  - Interpretar `on` → captura/inventário iniciado; `off` → captura parada.
  - Atualizar estado do leitor (ex.: `isInventorying` = true/false) e exibir o badge apropriado.
- Linhas de tag (nova tag):
  - Formato: `#t+@epc|tid|ant|rssi|protect`
  - Campos:
    - `epc` (hex string, necessário)
    - `tid` (hex string, pode estar vazio)
    - `ant` (antenna id, inteiro)
    - `rssi` (valor de sinal, pode ter sufixo " dBm" ou vírgula decimal)
    - `protect` (flag de proteção, opcional)
  - Implementação: parsear os campos, criar `RfidTag(epc=..., tid=..., rssi=...)` e emitir via `tagFlow`.
  - Nota: o app atualmente usa `rssi` como `String` no `RfidTag`; manter isso para compatibilidade com o restante do stack.
- Comandos de leitura/controle (pode aparecer):
  - `#read:on` / `#read:off` — alternativa para indicar início/parada de leitura; suportar como fallback.

Setup inicial (ao conectar)

- Ao conectar e descobrir o serviço NUS, executar um `initialSetup(commands: List<String>)` na ordem indicada por você.
- Bulk send permitido: enviar todos os comandos em sequência (cada comando seguido por `\n`) é aceitável; device responderá com várias mensagens.
- Tempo limite: aguardar `#setup_done` por um timeout configurável (sugestão: 10–20s). Se não chegar, tentar reenvio N vezes ou reportar erro de setup.
- Não é necessário processar todas as respostas individuais do setup; apenas aguardar `#setup_done` para considerar sucesso.

Parsing e robustez

- Receber bytes da characteristic → acumular em buffer por conexão.
- Cada vez que aparecer `\n` (ou `\r\n`) separar em linhas e processar cada linha:
  - Trim espaços; ignorar linhas vazias.
  - Se começar com `#t+` → parse tag.
  - Se contiver `copilot_getNotebookSummary:` → extrair `on`/`off` e atualizar `isInventorying`.
  - Se for `#setup_done` → concluir setup.
  - Caso contrário: log/debug (opcional) e descartar.

Roteiro de implementação (passos)

1. Documentar protocolo (este arquivo) — feito.
2. Confirmar UUIDs NUS (ou me enviar UUIDs do seu leitor).
3. Criar `readers/nordic/NordicBleReader.kt` (skeleton que implementa `IRfidReader`).
   - Campos: `targetMacAddress`, `_connectionState: MutableStateFlow`, `_tagChannel: MutableSharedFlow`.
   - Métodos: `connect(context)`, `disconnect()`, `startInventory()`, `stopInventory()`, `applyConfig()`, `readConfig()`.
4. Implementar BLE GATT connect/discover flow (semelhante ao IH25).
   - Ao conectar: descobrir serviço/characteristics, habilitar notificações na RX char.
5. Implementar handler de notificações (buffer + parse por linhas) com as regras acima.
6. Implementar `initialSetup(commands)` — enviar os comandos em série; aguardar `#setup_done` com timeout.
7. Parse de `#t+` → emitir `RfidTag` no `tagFlow`.
8. Atualizar `connectionState` / `isInventorying` quando receber `copilot_getNotebookSummary` ou `#read:on/off`.
9. Integrar o reader no `ReaderRegistry` e ajustar `MainViewModel` para reconhecer o novo leitor (se necessário mostrar o badge BLE).
10. Adicionar logs/timers para instrumentação (connect/start setup/first-tag recepción).
11. Testes manuais + script QA (rotina para validar setup e leitura de tags com exemplos fornecidos por você).

Critérios de aceite

- Ao conectar, o app habilita notificações e envia o setup; o app marca `setup` como concluído somente ao receber `#setup_done`.
- Tags do formato `#t+...` são convertidas em `RfidTag` e entregues ao `tagFlow`.
- Mensagens `copilot_getNotebookSummary: on/off` atualizam `isInventorying` e a UI reflete o estado.
- Comportamento BLE (scan/connect/disconnect) segue o padrão do IH25 (mostra disponível, permite conectar por MAC)

O que eu preciso de você antes de começar a implementação

- Confirmar (ou corrigir) os UUIDs NUS do seu leitor.
- Enviar a sequência exata de comandos de setup (uma lista na ordem), e exemplos reais de respostas (se possível).

---

Versão: r1 — criada automaticamente a partir da especificação fornecida pelo usuário.
