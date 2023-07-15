package com.lunatech.stream

import cats.effect.unsafe.implicits.global

import scala.io.StdIn
import scala.util.Try

/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 08/07/2023  16:56
 */
class StreamManager {

  def run(logs: CommonI): Unit = {

    println(s"log: ${logs.getClass.getSimpleName}")
    println("How do you want to read the logs? by specifying the number of lines or by specifying a filter? (l/f)")
    val inputLineOrFilter = StdIn.readLine()
    if (inputLineOrFilter == "l") {
      println("How many lines do you want to read")
      val inputLine = StdIn.readLine()
      val inputToInt = Try(inputLine.toInt)
      if (inputToInt.isFailure) {
        println(s"Please enter a number, inputing ${inputToInt.get} is not a number")
      }

      val input = inputToInt.get
      println(s"Good, you want to read $input lines")
      println(s"Any specific filter you want to apply? (y/n)")
      val polar = StdIn.readLine()
      if (polar == "y") {
        println("Please enter the filter")
        val filter = StdIn.readLine()
        logs.filter(filter)
      }
      else {
        logs.take(input)
      }

    } else if (inputLineOrFilter == "f") {
      println("Please enter the filter")
      val filter = StdIn.readLine()
      println(s"Good, you want to read the lines that contain $filter")
      logs.filter(filter)

    }
    else {
      println(s"Please enter l or f, inputing $inputLineOrFilter is not a valid option")
    }
    logs.print().unsafeRunSync()
  }

}
