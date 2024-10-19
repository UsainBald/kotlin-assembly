package com.yandexbrouser.kotlinassembly

import com.yandexbrouser.kotlinassembly.assembly.Assembler
import com.yandexbrouser.kotlinassembly.interpreter.Interpreter

fun main(args: Array<String>) {
  if (args.size < 5) {
    println("Usage: <sourceFile> <binaryFile> <logFile> <resultFile> <memoryRange>")
    return
  }

  val sourceFile = args[0]
  val binaryFile = args[1]
  val logFile = args[2]
  val resultFile = args[3]
  val memoryRange = args[4]

  val assembler = Assembler(sourceFile, binaryFile, logFile)
  assembler.assemble()

  val interpreter = Interpreter(binaryFile, resultFile, memoryRange)
  interpreter.run()
}
