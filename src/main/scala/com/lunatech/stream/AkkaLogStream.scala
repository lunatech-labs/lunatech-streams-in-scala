package com.lunatech.stream

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.scaladsl.{FileIO, Framing, Source}
import akka.stream.Materializer
import akka.util.ByteString
import cats.effect.IO
import cats.effect.unsafe.implicits.global

import java.nio.file.{Files, Path, Paths}
import scala.concurrent.ExecutionContextExecutor
import scala.jdk.CollectionConverters.IteratorHasAsScala

/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 08/07/2023  16:37
 */
class AkkaLogStream(directory: String)(implicit system: ActorSystem) extends CommonI {
  var currentStream = IO(processLogFiles)
  implicit val logger: LoggingAdapter = system.log
  implicit val materializer: Materializer = Materializer.matFromSystem(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  // Specify the folder path
  private val folderPath: Path = Paths.get(directory)

  // Create a Source of file paths
  private val logFileSource: Source[Path, Any] = Source.fromIterator(() =>
    Files.walk(folderPath).iterator().asScala
      .filter(Files.isRegularFile(_))
      .filter(_.getFileName.toString.endsWith(".log"))
  )

  private def processLogFiles: Source[String, Any] = logFileSource
    .flatMapConcat(filePath => FileIO.fromPath(filePath))
    .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 1000, allowTruncation = true))
    .map(_.utf8String)
    .recover {
      case e: Exception =>
        logger.error(s"Error while processing log files: ${e.getMessage}")
        ""
    }

  override def filter(filter: String): IO[Any] = {
    currentStream = IO(processLogFiles.filter(_.contains(filter)))
    currentStream
  }

  override def take(n: Int): IO[Any] = {
    currentStream = IO(processLogFiles.take(n))
    currentStream
  }

  override def print(): IO[Unit] = IO{
    println("Printing the logs from the akka stream")
    val startTime = System.currentTimeMillis()
    // when the akka stream is done, print the time taken
    currentStream.unsafeRunSync().take(50).runForeach(println).onComplete(_ => {
      val endTime = System.currentTimeMillis()
      println(s"Time taken to process the stream is ${endTime - startTime} ms")
    })
  }
}
