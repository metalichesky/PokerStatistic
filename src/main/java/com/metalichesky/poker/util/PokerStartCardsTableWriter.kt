package com.metalichesky.poker.util

import com.metalichesky.poker.model.PokerStatistic
import org.supercsv.cellprocessor.Optional
import org.supercsv.cellprocessor.constraint.NotNull
import org.supercsv.cellprocessor.ift.CellProcessor
import org.supercsv.io.CsvBeanWriter
import org.supercsv.io.ICsvBeanWriter
import org.supercsv.prefs.CsvPreference
import java.io.File
import java.io.FileWriter

class PokerStartCardsTableWriter() {
    companion object {
        private val processors: Array<CellProcessor> = arrayOf(
            Optional(),
            NotNull(),
            Optional()
        )
    }
    private var fileWriter: FileWriter? = null
    private var csvBeanWriter: ICsvBeanWriter? = null
    private val header = arrayOf("startCards", "winProbability", "loseProbability")

    class StartCardsTableRow() {
        var startCards: String = ""
        var winProbability: Double = 0.0
        var loseProbability: Double = 0.0
        constructor(startCards: String, winProbability: Double, loseProbability: Double): this() {
            this.startCards = startCards
            this.winProbability = winProbability
            this.loseProbability = loseProbability
        }
    }

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
        pokerStatistic: PokerStatistic
    ) {
        pokerStatistic.startCardsWinsProbability.keys.forEach {
            val startCards = it
            val winProbability = pokerStatistic.startCardsWinsProbability[it] ?: 0.0
            val loseProbability = pokerStatistic.startCardsLosesProbability[it] ?: 0.0
            val tableRow = StartCardsTableRow(startCards, winProbability, loseProbability)
            csvBeanWriter?.write(tableRow, header, processors)
        }
    }

    fun complete() {
        csvBeanWriter?.flush()
        csvBeanWriter?.close()
        csvBeanWriter = null
        fileWriter?.close()
        fileWriter = null
    }
}