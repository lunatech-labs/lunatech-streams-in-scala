package com.lunatech.stream

import scala.io.Source


/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 29/05/2023  20:40
 */

/**
 * We want to create a lazy stream of logs, and restrict some operations from our stream.
 * Typically, any operation that requires to compute the whole stream elements should be forbidden on our LogStream.
 * @tparam A
 */

// create a sealed trait LogStream[A] with the following methods:
sealed trait LogStream[A] {
  def take(n:Int):LogStream[A]
  def takeWhile(f: A => Boolean): LogStream[A]
  def filter(f: A => Boolean): LogStream[A] = {
    val lazyList = toLazyList.filter(f)
    LogStream(lazyList)
  }

  def map[B](f: A => B): LogStream[B]
  def ++(that: LogStream[A]): LogStream[A]
  def toLazyList: LazyList[A]

  def compute(n:Int = 20): List[A] = toLazyList.take(n).toList

  override def toString: String = {
    val lazyList = toLazyList
    val pattern = """LazyList\((.*)\)""".r
    val input = lazyList.toString()
    input match {
      case pattern(substring) =>
        val elements = substring.split(",")
        val someElements = if(elements.length > 100) elements.toList.takeWhile(_ != "<not computed>").take(100).mkString(",\n") else elements.toList.takeWhile(_ != "<not computed>").mkString(",\n")
        val lastElement = elements.toList.last
        val total  = if(elements.length> 100 ) {
          val middleElements = if(elements.length - 100 > 1) s"${elements.length - 100} more elements..." else ""
          s"$someElements,\n$middleElements,\n$lastElement"
        } else {
          elements.toList.mkString(",\n")
        }
        if(elements.length > 1){
          s"LogStream(\n$total\n)"
        } else s"LogStream($total)"

      case _ => input
    }

  }
}


object LogStream {
  def apply[A](values: LazyList[A]): LogStream[A] = new LogStream[A] {
    override def take(n: Int): LogStream[A] = {
      require(n >= 0, "n must be positive")
      val lazyList = values.take(n)
      LogStream(lazyList)
    }
    override def takeWhile(f: A => Boolean): LogStream[A] = {
      val lazyList = values.takeWhile(f)
      LogStream(lazyList)
    }
    override def map[B](f: A => B): LogStream[B] = {
      val lazyListTransform = values.map(f)
      LogStream(lazyListTransform)
    }
    override def ++(that: LogStream[A]): LogStream[A] = {
      val lazyList = values ++ that.toLazyList
      LogStream(lazyList)
    }
    override def toLazyList: LazyList[A] = values
  }

  def empty[A]: LogStream[A] = new LogStream[A] {
    override def take(n: Int): LogStream[A] = {
      require(n >= 0, "n must be positive")
      LogStream(LazyList.empty[A])
    }
    override def takeWhile(f: A => Boolean): LogStream[A] = {
      LogStream(LazyList.empty[A])
    }
    override def map[B](f: A => B): LogStream[B] = {
      LogStream(LazyList.empty[B])
    }
    override def ++(that: LogStream[A]): LogStream[A] = {
      that
    }
    override def toLazyList: LazyList[A] = LazyList.empty

  }

  /**
   * Read all the files from the given path and return the list of files
   * @param path: path of the directory in the resources
   * @param ext: extension of the file
   * @return
   */
  def readDirectories(path: String, ext: String): List[String] = {
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


  /**
   * Stream the logs from the given files
   * @param files : list of files from the resources folder
   * @return (List[Source],LogStream[String]), where List[Source] is the list of sources from the files and LogStream[String] is the stream of logs
   *         We need to return the list of sources so that we can close the sources after the stream is finished
   */
  def streamLogsFiles(files: List[String]): (List[Source],LogStream[String]) =
    files.map {
      case (file) =>
        val source = Source.fromFile(file) //, we can't close the source because we are streaming the logs and don't know when the stream will end
        (source,LogStream[String](source.getLines().to(LazyList)).map(it => s"${it}"))
    }.foldLeft(List.empty[Source],LogStream.empty[String])((accTuple2, tuple2) => {
      val (accSource, accLogStream) = accTuple2
      val (source, logStream) = tuple2
      (accSource ++ List(source), accLogStream ++ logStream)
    })

}
