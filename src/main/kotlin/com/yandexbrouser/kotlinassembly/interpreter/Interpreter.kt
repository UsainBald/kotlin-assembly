package com.yandexbrouser.kotlinassembly.interpreter

import com.yandexbrouser.kotlinassembly.util.FileUtils
import java.io.File
import java.nio.ByteBuffer

class Interpreter(
  private val binaryFile: String,
  private val resultFile: String,
  private val memoryRange: String
) {
  private val stack = mutableListOf<Int>()
  private val memory = IntArray(1024)

  fun run() {
    val binaryData = File(binaryFile).readBytes()
    var index = 0

    while (index < binaryData.size) {
      val command = binaryData[index].toInt()
      index++

      when (command) {
        2 -> {
          handleLoadConst(binaryData, index)
          index += 4
        }
        6 -> handleReadMem()
        4 -> handleWriteMem()
        12 -> handleNegate()
      }
    }

    saveMemory()
  }

  private fun handleLoadConst(binaryData: ByteArray, index: Int) {
    val value = ByteBuffer.wrap(binaryData, index, 4).int
    stack.add(value)
  }

  private fun handleReadMem() {
    if (stack.isEmpty()) {
      println("Error: Stack is empty. Cannot perform read_mem.")
      return
    }
    val address = stack.removeLast()
    stack.add(memory[address])
  }

  private fun handleWriteMem() {
    if (stack.size < 2) {
      println("Error: Stack does not contain enough values for write_mem.")
      return
    }
    val value = stack.removeLast()
    val address = stack.removeLast()
    memory[address] = value
  }

  private fun handleNegate() {
    if (stack.isEmpty()) {
      println("Error: Stack is empty. Cannot perform negate.")
      return
    }
    val value = stack.removeLast()
    stack.add(-value)
  }

  private fun saveMemory() {
    val (start, end) = memoryRange.split(":").map { it.toInt() }
    FileUtils.writeResultFile(resultFile, memory, start, end)
  }
}
