package com.lunatech

import akka.actor.ActorSystem

import java.nio.file.Paths
import com.lunatech.stream.{AkkaLogStream, Fs2Stream, LogStream, StreamManager}

import scala.io.StdIn


/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 17/05/2023  00:27
 */


object Logger extends App {

  val path = getClass.getResource("/logs").getPath.split("target")(1)
  private val relativePath = s"target$path"
  val streamManager = new StreamManager()

  implicit val actorSystem: ActorSystem = ActorSystem("Logger")

  println("Please specify which stream api you want to use, [(LogStream->l), (AkkaStream->a), (fs2Stream->f)]")
  val input = StdIn.readLine()
  if (input == "l") {
    streamManager.run(new LogStream(relativePath))
  } else if (input == "a") {
    streamManager.run(new AkkaLogStream(relativePath))
  } else if (input == "f") {
    streamManager.run(new Fs2Stream(relativePath))
  }
  else println("Please enter a valid input, allowed inputs are l, a, f")


}
