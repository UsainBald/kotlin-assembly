package com.yandexbrouser.kotlinassembly.util

import com.yandexbrouser.kotlinassembly.model.LogEntry
import java.io.File

object FileUtils {

  fun writeBinaryFile(filePath: String, data: ByteArray) {
    File(filePath).writeBytes(data)
  }

  fun writeLogFile(logFile: String, logEntries: List<LogEntry>) {
    File(logFile).bufferedWriter().use { writer ->
      writer.write("command,value\n")
      logEntries.forEach { entry ->
        writer.write("${entry.command},${entry.value ?: ""}\n")
      }
    }
  }

  fun writeResultFile(resultFile: String, memory: IntArray, start: Int, end: Int) {
    File(resultFile).bufferedWriter().use { writer ->
      writer.write("address,value\n")
      for (i in start..end) {
        writer.write("$i,${memory[i]}\n")
      }
    }
  }
}
