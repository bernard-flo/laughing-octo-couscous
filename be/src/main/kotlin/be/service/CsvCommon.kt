package be.service

import com.opencsv.CSVReader
import java.nio.file.Files
import kotlin.io.path.Path

fun readCsv(filePath: String): CsvData {

    return Files.newBufferedReader(Path(filePath)).use { reader ->
        CSVReader(reader).use { csvReader ->
            csvReader.readAll()
        }
    }
}

typealias CsvData = List<CsvLine>
typealias CsvLine = Array<String>
