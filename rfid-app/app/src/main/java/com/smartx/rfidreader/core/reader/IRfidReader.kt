package com.smartx.rfidreader.core.reader

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Contrato unificado que todos os leitores RFID devem implementar.
 *
 * Ao adicionar suporte a um novo leitor:
 *  1. Crie uma classe em readers/<modelo>/ implementando esta interface.
 *  2. Registre-a em ReaderRegistry.
 *  Pronto!
 */
interface IRfidReader {

    /** Identificador único do modelo (ex.: "AT907", "C72", "IH25") */
    val readerId: String

    /** Nome legível para exibição (ex.: "Chainway AT907") */
    val displayName: String

    /** Estado atual da conexão como StateFlow observável */
    val connectionState: StateFlow<ReaderConnectionState>

    /** Stream reativo de tags lidas durante o inventário */
    val tagFlow: Flow<RfidTag>

    /**
     * Inicializa e conecta ao leitor.
     * @param context contexto Android
     * @return true se a conexão foi bem-sucedida
     */
    suspend fun connect(context: Context): Boolean

    /** Desconecta e libera recursos */
    suspend fun disconnect()

    /** Inicia inventário contínuo; as tags chegam via [tagFlow] */
    fun startInventory(): Boolean

    /** Para o inventário */
    fun stopInventory(): Boolean

    /** Retorna true se o inventário está em execução */
    fun isInventorying(): Boolean

    /** Aplica um bloco de configurações ao leitor */
    suspend fun applyConfig(config: ReaderConfig): Boolean

    /** Lê a configuração atual do leitor */
    suspend fun readConfig(): ReaderConfig

    /**
     * Callback chamado quando o gatilho físico é pressionado.
     * Retorna true se o evento foi tratado.
     */
    fun onTriggerPressed(): Boolean

    /**
     * Callback chamado quando o gatilho físico é liberado.
     * Retorna true se o evento foi tratado.
     */
    fun onTriggerReleased(): Boolean
}
