package com.yandexbrouser.kotlinassembly.assembly

import com.yandexbrouser.kotlinassembly.model.LogEntry
import com.yandexbrouser.kotlinassembly.util.FileUtils
import java.io.File
import java.nio.ByteBuffer

class Assembler(
  private val sourceFile: String,
  private val binaryFile: String,
  private val logFile: String
) {
  private val commands = mapOf(
    "load_const" to 2,
    "read_mem" to 6,
    "write_mem" to 4,
    "negate" to 12
  )

  fun assemble() {
    val binaryData = mutableListOf<Byte>()
    val logEntries = mutableListOf<LogEntry>()

    File(sourceFile).forEachLine { line ->
      val parts = line.trim().split(" ")
      val command = parts[0]
      when (command) {
        "load_const" -> {
          val value = parts[1].toInt()
          binaryData.addAll(encodeLoadConst(value))
          logEntries.add(LogEntry("load_const", value))
        }
        "read_mem" -> {
          binaryData.add(commands.getValue("read_mem").toByte())
          logEntries.add(LogEntry("read_mem", null))
        }
        "write_mem" -> {
          binaryData.add(commands.getValue("write_mem").toByte())
          logEntries.add(LogEntry("write_mem", null))
        }
        "negate" -> {
          binaryData.add(commands.getValue("negate").toByte())
          logEntries.add(LogEntry("negate", null))
        }
      }
    }

    FileUtils.writeBinaryFile(binaryFile, binaryData.toByteArray())
    FileUtils.writeLogFile(logFile, logEntries)
  }

  private fun encodeLoadConst(value: Int): List<Byte> {
    val commandCode = commands.getValue("load_const")
    val buffer = ByteBuffer.allocate(5)
    buffer.put(commandCode.toByte())
    buffer.putInt(value)
    return buffer.array().toList()
  }
}
