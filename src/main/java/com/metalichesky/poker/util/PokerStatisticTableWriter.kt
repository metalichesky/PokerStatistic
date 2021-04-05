package com.metalichesky.poker.util

import com.metalichesky.poker.model.Player
import com.metalichesky.poker.model.PokerStatistic
import org.supercsv.cellprocessor.Optional
import org.supercsv.cellprocessor.constraint.NotNull
import org.supercsv.cellprocessor.constraint.UniqueHashCode
import org.supercsv.cellprocessor.ift.CellProcessor
import org.supercsv.io.CsvBeanWriter
import org.supercsv.io.ICsvBeanWriter
import org.supercsv.prefs.CsvPreference
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.StringWriter


class PokerStatisticTableWriter() {
    companion object {
        private val processors: Array<CellProcessor> = arrayOf(
            Optional(),
            NotNull(),
            Optional()
        )
    }
    private var fileWriter: FileWriter? = null
    private var csvBeanWriter: ICsvBeanWriter? = null
    private val header = arrayOf("gamesNumber", "winProbability", "loseProbability")


    data class PokerStatisticTableRow(
        val gamesNumber: Int,
        val winProbability: Double,
        val loseProbability: Double
    )

    fun prepare(filePath: String) {
        val file = File(filePath)
        val directory = File(file.parent)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        if (!file.exists()) {
            file.createNewFile()
        } else {
            file.delete()
            file.createNewFile()
        }
        fileWriter = FileWriter(file)
        CsvPreference.TAB_PREFERENCE
        csvBeanWriter = CsvBeanWriter(fileWriter, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)
        csvBeanWriter?.writeHeader(*header)
    }

    fun writeIntoFile(
        player: Player,
        pokerStatistic: PokerStatistic
    ) {
        val gamesNumber = pokerStatistic.gamesNumber
        val winProbability = pokerStatistic.getWinProbability(player)
        val loseProbability = pokerStatistic.getLoseProbability(player)
        val tableRow = PokerStatisticTableRow(gamesNumber, winProbability, loseProbability)
        csvBeanWriter?.write(tableRow, header, processors)
    }

    fun complete() {
        csvBeanWriter?.flush()
        csvBeanWriter?.close()
        csvBeanWriter = null
        fileWriter?.close()
        fileWriter = null
    }

}