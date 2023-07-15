package com.lunatech.stream

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.io.file.{Files, Path}
import fs2.text

/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 12/07/2023  00:17
 */
class Fs2Stream(filePath: String ) extends CommonI {
  val directory: Path = Path(filePath)
  var currentStream = IO(readLogFilesFromDirectory)
  private def readLogFile(path: Path): fs2.Stream[IO, String] =
    Files[IO]
      .readAll(path)
      .through(text.utf8Decode)
      .through(text.lines)

  private def readLogFilesFromDirectory: fs2.Stream[IO, String] = {
    Files[IO]
      .walk(directory)
      .filter(_.extName.endsWith(".log"))
      .flatMap(readLogFile)
   }


  override def filter(filter: String): IO[Any] = {
    currentStream = IO(readLogFilesFromDirectory.filter(_.contains(filter)))
    currentStream
  }

  override def take(n: Int): IO[Any] = {
    currentStream = IO(readLogFilesFromDirectory.take(n))
    currentStream
  }

  override def print(): IO[Unit] = {
    val startTime = System.currentTimeMillis()

    val printStream = currentStream.unsafeRunSync()

    val printResult: IO[Unit] = printStream
      .take(50)
      .evalMap(str => IO(println(str)))
      .compile
      .drain

    val printAndMeasureTime: IO[Unit] = for {
      _ <- IO(println("Printing the logs from the fs2 stream"))
      _ <- printResult
      endTime <- IO(System.currentTimeMillis())
      _ <- IO(println(s"Time taken to process the stream is ${endTime - startTime} ms"))
    } yield ()

    printAndMeasureTime
  }

}
