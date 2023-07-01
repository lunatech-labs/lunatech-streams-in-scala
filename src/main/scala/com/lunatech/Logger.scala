package com.lunatech

import com.lunatech.stream.LogStream

import scala.io.StdIn
import scala.util.Try


/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 17/05/2023  00:27
 */



object Logger extends App {

  val path = getClass.getResource("/logs").getPath.split("target")(1)
  private val relativePath = s"target$path"
  private val allLogsPaths = LogStream.readDirectories(relativePath, ".log")
  private val logs = LogStream.streamLogsFiles("", allLogsPaths)
  var isReading = true
  while (isReading) {
    println(s"logs: $logs")
    println("How do you want to read the logs? by specifying the number of lines or by specifying a filter? (l/f)")
    val inputLineOrFilter = StdIn.readLine()
    if (inputLineOrFilter == "l") {
      println("How many lines do you want to read")
      val inputLine = StdIn.readLine()
      val inputToInt = Try(inputLine.toInt)
      if (inputToInt.isFailure) {
        println(s"Please enter a number, inputing ${inputToInt.get} is not a number")
        isReading = false
      }
      if (isReading) {
        val input = inputToInt.get
        println(s"Good, you want to read $input lines")
        println(s"Any specific filter you want to apply? (y/n)")
        val polar = StdIn.readLine()
        if (polar == "y") {
          println("Please enter the filter")
          val filter = StdIn.readLine()
          logs.takeWhile(_.contains(filter)).takeAndPrint(input)
        }
        else {
          logs.takeAndPrint(input)
        }
      }
      isReading = false
    }
    else if (inputLineOrFilter == "f") {
      println("Please enter the filter")
      val filter = StdIn.readLine()
      println(s"Good, you want to read the lines that contain $filter")
      println(s"Any specific number of lines you want to read? (y/n)")
      val polar = StdIn.readLine()
      if (polar == "y") {
        println("Please enter the number of lines")
        val inputLine = StdIn.readLine()
        val inputToInt = Try(inputLine.toInt)
        if (inputToInt.isFailure) {
          println(s"Please enter a number, inputing ${inputToInt.get} is not a number")
          isReading = false
        }
        if (isReading) {
          val input = inputToInt.get
          println(s"Good, you want to read $input lines")
          logs.takeWhile(_.contains(filter)).toLazyList.head
        }
      }
      else println(logs.takeWhile(_.contains(filter)).toLazyList.toList)
      isReading = false
    }
    else {
      println(s"Please enter l or f, inputing $inputLineOrFilter is not a valid option")
    }
    isReading = true

  }
//todo: see if there is a way to close the stream

}
