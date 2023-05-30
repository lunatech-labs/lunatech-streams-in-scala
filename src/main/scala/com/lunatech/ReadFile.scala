package com.lunatech

import com.lunatech.stream.LogStream
import scala.io.{ StdIn}


/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 17/05/2023  00:27
 */



object ReadFile extends App {

  val path = getClass.getResource("/logs").getPath.split("target")(1)
  private val relativePath = s"target$path"
  private val allLogsPaths = LogStream.readDirectories(relativePath, ".log")
  private val logs = LogStream.streamLogsFiles("", allLogsPaths)
  while (true) {
    println("How many lines do you want to read")
    println(s"logs: $logs")
    val input = StdIn.readLine()
    val inputToInt = input.toInt
    logs.take(inputToInt).foreach(println)
  }
//todo: see if there is a way to close the stream

}
