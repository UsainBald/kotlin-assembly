package com.yandexbrouser.kotlinassembly.assembly

import org.junit.jupiter.api.*
import java.io.File
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AssemblerTest {
  private lateinit var tempDir: File
  private lateinit var sourceFile: File
  private lateinit var binaryFile: File
  private lateinit var logFile: File
  private lateinit var assembler: Assembler

  @BeforeAll
  fun setup() {
    tempDir = Files.createTempDirectory("assemblyTest").toFile()
  }

  @BeforeEach
  fun setupEach() {
    sourceFile = File(tempDir, "source.txt")
    binaryFile = File(tempDir, "binary.bin")
    logFile = File(tempDir, "log.csv")
    assembler = Assembler(sourceFile.path, binaryFile.path, logFile.path)
  }

  @AfterAll
  fun cleanup() {
    tempDir.deleteRecursively()
  }

  @Test
  fun shouldAssembleCorrectBinaryFile() {
    val testSource = "load_const 5\nread_mem\nwrite_mem\nnegate"
    sourceFile.writeText(testSource)

    assembler.assemble()

    val binaryData = binaryFile.readBytes()
    Assertions.assertEquals(8, binaryData.size)
    Assertions.assertEquals(2.toByte(), binaryData[0])
    Assertions.assertEquals(6.toByte(), binaryData[5])
    Assertions.assertEquals(4.toByte(), binaryData[6])
    Assertions.assertEquals(12.toByte(), binaryData[7])
  }

  @Test
  fun shouldGenerateCorrectLogFile() {
    val testSource = "load_const 10\nnegate\nwrite_mem"
    sourceFile.writeText(testSource)

    assembler.assemble()

    val logEntries = logFile.readLines()
    Assertions.assertEquals(4, logEntries.size)
    Assertions.assertEquals("command,value", logEntries[0])
    Assertions.assertEquals("load_const,10", logEntries[1])
    Assertions.assertEquals("negate,", logEntries[2])
    Assertions.assertEquals("write_mem,", logEntries[3])
  }
}
