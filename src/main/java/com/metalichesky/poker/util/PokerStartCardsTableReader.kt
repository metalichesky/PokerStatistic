package com.metalichesky.poker.util

import org.supercsv.cellprocessor.Optional
import org.supercsv.cellprocessor.ParseDouble
import org.supercsv.cellprocessor.ift.CellProcessor
import org.supercsv.io.CsvBeanReader
import org.supercsv.io.ICsvBeanReader
import org.supercsv.prefs.CsvPreference
import java.io.File
import java.io.FileReader
import java.util.stream.Collectors.mapping




class PokerStartCardsTableReader() {
    companion object {
        private val processors: Array<CellProcessor> = arrayOf(
            Optional(),
            ParseDouble(),
            ParseDouble()
        )
        private val mappings = arrayOf("startCards", "winProbability", "loseProbability")
    }
    private var fileReader: FileReader? = null
    private var csvBeanReader: ICsvBeanReader? = null

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
        fileReader = FileReader(file)
        csvBeanReader = CsvBeanReader(fileReader, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)
    }

    fun readFromFile(): List<PokerStartCardsTableWriter.StartCardsTableRow> {
        var row: PokerStartCardsTableWriter.StartCardsTableRow? = null
        val rows: MutableList<PokerStartCardsTableWriter.StartCardsTableRow> = mutableListOf()
        val header = csvBeanReader?.getHeader(true)
        while (csvBeanReader?.read(
                    PokerStartCardsTableWriter.StartCardsTableRow::class.java,
                    mappings,
                    *processors)?.also{ row = it } != null) {
            row?.let{
                rows.add(it)
            }
        }
        return rows
    }

    fun complete() {
        csvBeanReader?.close()
        csvBeanReader = null
        fileReader?.close()
        fileReader = null
    }
}