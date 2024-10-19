package com.yandexbrouser.kotlinassembly.interpreter

import com.yandexbrouser.kotlinassembly.assembly.Assembler
import org.junit.jupiter.api.*
import java.io.File
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InterpreterTest {
  private lateinit var tempDir: File
  private lateinit var sourceFile: File
  private lateinit var binaryFile: File
  private lateinit var resultFile: File
  private lateinit var interpreter: Interpreter

  @BeforeAll
  fun setup() {
    tempDir = Files.createTempDirectory("interpreterTest").toFile()
  }

  @BeforeEach
  fun setupEach() {
    sourceFile = File(tempDir, "source.txt")
    binaryFile = File(tempDir, "binary.bin")
    resultFile = File(tempDir, "result.csv")
    interpreter = Interpreter(binaryFile.path, resultFile.path, "0:10")
  }

  @AfterAll
  fun cleanup() {
    tempDir.deleteRecursively()
  }

  @Test
  fun shouldExecuteLoadConstAndNegate() {
    val testSource = "load_const 5\nnegate"
    sourceFile.writeText(testSource)

    val assembler = Assembler(sourceFile.path, binaryFile.path, File(tempDir, "log.csv").path)
    assembler.assemble()
    interpreter.run()

    val memoryValues = resultFile.readLines()
    Assertions.assertEquals(12, memoryValues.size)
    Assertions.assertEquals("address,value", memoryValues[0])
  }

  @Test
  fun shouldHandleEmptyStackInReadMem() {
    val testSource = "read_mem"
    sourceFile.writeText(testSource)

    val assembler = Assembler(sourceFile.path, binaryFile.path, File(tempDir, "log.csv").path)
    assembler.assemble()

    Assertions.assertDoesNotThrow { interpreter.run() }
  }

  @Test
  fun shouldHandleEmptyStackInNegate() {
    val testSource = "negate"
    sourceFile.writeText(testSource)

    val assembler = Assembler(sourceFile.path, binaryFile.path, File(tempDir, "log.csv").path)
    assembler.assemble()

    Assertions.assertDoesNotThrow { interpreter.run() }
  }

  @Test
  fun shouldCorrectlyWriteAndReadMemory() {
    val testSource = "load_const 0\nload_const 99\nwrite_mem\nload_const 0\nread_mem"
    sourceFile.writeText(testSource)

    val assembler = Assembler(sourceFile.path, binaryFile.path, File(tempDir, "log.csv").path)
    assembler.assemble()
    interpreter.run()

    val memoryValues = resultFile.readLines()
    Assertions.assertEquals(12, memoryValues.size)
    Assertions.assertEquals("address,value", memoryValues[0])
    Assertions.assertEquals("0,99", memoryValues[1])
  }
}
