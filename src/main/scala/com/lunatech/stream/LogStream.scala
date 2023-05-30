package com.lunatech.stream

import scala.io.Source


/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 29/05/2023  20:40
 */

/**
 * We want to create a lazy stream of logs, and restrict some operations from our stream.
 * Typically, any operation that requires to read the whole stream should be forbidden on our LogStream.
 * @tparam A
 */
trait LogStream[A] {
  def take(n:Int):LogStream[A]
  def takeWhile(f: A => Boolean): LogStream[A]
  def map[B](f: A => B): LogStream[B]
  def ++(that: LogStream[A]): LogStream[A]
  def toLazyList: LazyList[A]
  override def toString: String = toLazyList.toString();
  def foreach(f: A => Unit): Unit = toLazyList.foreach(f)
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
      val lazyList = values ++ that.takeWhile(_ => true).toLazyList
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
   * @param searchValue: search value, if empty then stream all the logs
   * @param files: list of files from the resources folder
   * @return LogStream[String], which is a lazy list of logs
   */
  def streamLogsFiles(searchValue: String = "", files: List[String]): LogStream[String] =
    files.map {
      case (file) =>
        val source = Source.fromFile(file) //, we can't close the source because we are using lazy list
        val logStream = LogStream[String](source.getLines().to(LazyList)).map(it => s"-- $file ---> ${it}")
        if (searchValue.nonEmpty) {
          logStream.takeWhile(_.contains(searchValue))
        }
        else
          logStream
    }.foldLeft(LogStream.empty[String])((acc, it) => acc ++ it)

}
