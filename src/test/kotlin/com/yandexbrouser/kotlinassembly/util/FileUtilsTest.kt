package com.yandexbrouser.kotlinassembly.util

import com.yandexbrouser.kotlinassembly.model.LogEntry
import org.junit.jupiter.api.*
import java.io.File
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileUtilsTest {
  private lateinit var tempDir: File
  private lateinit var testBinaryFile: File
  private lateinit var testLogFile: File
  private lateinit var testResultFile: File

  @BeforeAll
  fun setup() {
    tempDir = Files.createTempDirectory("fileUtilsTest").toFile()
  }

  @BeforeEach
  fun setupEach() {
    testBinaryFile = File(tempDir, "testBinaryFile.bin")
    testLogFile = File(tempDir, "testLog.csv")
    testResultFile = File(tempDir, "testResult.csv")
  }

  @AfterAll
  fun cleanup() {
    tempDir.deleteRecursively()
  }

  @Test
  fun shouldWriteAndReadBinaryFile() {
    val data = byteArrayOf(1, 2, 3, 4, 5)
    FileUtils.writeBinaryFile(testBinaryFile.path, data)

    val readData = testBinaryFile.readBytes()
    Assertions.assertArrayEquals(data, readData)
  }

  @Test
  fun shouldWriteLogFileCorrectly() {
    val logEntries = listOf(
      LogEntry("load_const", 5),
      LogEntry("negate", null),
      LogEntry("write_mem", null)
    )

    FileUtils.writeLogFile(testLogFile.path, logEntries)

    val logLines = testLogFile.readLines()
    Assertions.assertEquals(4, logLines.size)
    Assertions.assertEquals("command,value", logLines[0])
    Assertions.assertEquals("load_const,5", logLines[1])
    Assertions.assertEquals("negate,", logLines[2])
    Assertions.assertEquals("write_mem,", logLines[3])
  }

  @Test
  fun shouldWriteResultFileCorrectly() {
    val memory = IntArray(10) { it * 2 }
    FileUtils.writeResultFile(testResultFile.path, memory, 0, 9)

    val resultLines = testResultFile.readLines()
    Assertions.assertEquals(11, resultLines.size)
    Assertions.assertEquals("address,value", resultLines[0])
    Assertions.assertEquals("0,0", resultLines[1])
    Assertions.assertEquals("1,2", resultLines[2])
    Assertions.assertEquals("9,18", resultLines[10])
  }
}
