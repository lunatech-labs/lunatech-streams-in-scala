package com.lunatech

import com.lunatech.stream.{LogStream, StreamManager}

import scala.io.StdIn
import scala.util.Try


/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 17/05/2023  00:27
 */



object Logger extends App {
  val streamManager = new StreamManager()
  println("LogStream or AkkaStream? (l/a)")
  val input = StdIn.readLine()
  if(input == "l"){
    streamManager.runScalaStreamLog
  } else if(input == "a"){
    streamManager.runAkkaStreamLog
  } else println("Please enter a valid input")
}
