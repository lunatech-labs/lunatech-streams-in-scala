package com.lunatech

import com.lunatech.stream.LogStream

import scala.io.StdIn
import scala.util.Try


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
    val inputLine = StdIn.readLine()
    val inputToInt = Try(inputLine.toInt)
    if (inputToInt.isFailure) {
      println("Please enter a number")
       System.exit(1)
    }
    val input = inputToInt.get
    println(s"Good, you want to read $input lines")
    println(s"Any specific filter you want to apply? (y/n)")
    val polar = StdIn.readLine()
    if (polar == "y") {
      println("Please enter the filter")
      val filter = StdIn.readLine()
      logs.take(input).takeWhile(_.contains(filter)).foreach(println)
    }
    else
    logs.take(input).foreach(println)
  }
//todo: see if there is a way to close the stream

}
