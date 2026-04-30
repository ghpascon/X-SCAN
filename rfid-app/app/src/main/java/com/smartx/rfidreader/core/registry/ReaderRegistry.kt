package com.smartx.rfidreader.core.registry

import com.smartx.rfidreader.core.reader.IRfidReader
import com.smartx.rfidreader.readers.at907.AT907Reader
import com.smartx.rfidreader.readers.c72.C72Reader
import com.smartx.rfidreader.readers.ih25.IH25Reader
import com.smartx.rfidreader.readers.tsl1128.Tsl1128Reader
import com.smartx.rfidreader.readers.x714.X714Reader
import com.smartx.rfidreader.readers.xr2.XR2Reader

/**
 * Registro central de todos os leitores RFID suportados.
 *
 * Para adicionar um novo leitor:
 *   1. Implemente [IRfidReader] para o modelo
 *   2. Adicione a instância a [availableReaders]
 *   Pronto — a UI e o restante do app detectam automaticamente.
 */
object ReaderRegistry {

    val availableReaders: List<IRfidReader> by lazy {
        listOf(
            AT907Reader(),
            C72Reader(),
            IH25Reader(),
            Tsl1128Reader(),
            X714Reader(),
            XR2Reader()
        )
    }

    fun findById(readerId: String): IRfidReader? =
        availableReaders.firstOrNull { it.readerId == readerId }
}
