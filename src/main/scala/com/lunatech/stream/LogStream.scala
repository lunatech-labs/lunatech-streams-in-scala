package com.lunatech.stream

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import scala.io.Source
import scala.collection.immutable.LazyList

/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 29/05/2023  20:40
 */


class LogStream(path: String) extends CommonI {
  var currentStream = IO(processLogStream._1)


  private def processLogStream = {
    val allLogsPaths = LogStream.readDirectories(path, ".log")
    val turple2 = LogStream.streamLogsFiles(allLogsPaths)
    val logs = turple2._2
    val sources = turple2._1
    (logs, sources)
  }
  override def filter(filter: String): IO[Any] = {
    currentStream = IO(processLogStream._1.filter(_.contains(filter)))
    currentStream
  }

  override def take(n: Int = 50): IO[Any] = {
    currentStream = IO(processLogStream._1.take(n))
    currentStream
  }

  override def print(): IO[Unit] = IO{
    // track the time taken to process the stream
    val startTime = System.currentTimeMillis()
    currentStream.unsafeRunSync().take(100).foreach(println)
    val endTime = System.currentTimeMillis()
    println(s"Time taken to process the stream is ${endTime - startTime} ms")
  }
}

object LogStream {
  /**
   * Read all the files from the given path and return the list of files
   *
   * @param path : path of the directory in the resources
   * @param ext  : extension of the file
   * @return
   */
  private def readDirectories(path: String, ext: String): List[String] = {
    val d = new java.io.File(path)
    if (d.exists && d.isDirectory) {
      d.listFiles
        .filter((item) => item.isFile && item.getName.endsWith(ext))
        .map(_.getAbsolutePath)
        .to(List) ++ d.listFiles.filter(_.isDirectory)
        .flatMap(dir => readDirectories(dir.getAbsolutePath, ext))
    } else {
      List.empty
    }
  }
   /**  * Stream the logs from the given files
     * @param files : list of files from the resources folder
     * @return (List[Source],LazyList[String]), where List[Source] is the list of sources from the files and LazyList[String] is the stream of logs
     *         We need to return the list of sources so that we can close the sources after the stream is finished
    **/
  private def streamLogsFiles(files: List[String]): (List[Source], LazyList[String]) =
    files.map {
      case (file) =>
        val source = Source.fromFile(file) //, we can't close the source because we are streaming the logs and don't know when the stream will end
        (source, source.getLines().to(LazyList).map(it => s"${it}"))
    }.foldLeft(List.empty[Source], LazyList.empty[String])((accTuple2, tuple2) => {
      val (accSource, accLogStream) = accTuple2
      val (source, logStream) = tuple2
      (accSource ++ List(source), accLogStream ++ logStream)
    })
}
