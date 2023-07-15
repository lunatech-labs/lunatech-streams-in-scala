package com.lunatech.stream

import com.lunatech.Logger.getClass
import munit.FunSuite

/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 15/07/2023  19:19
 */
class AkkaLogStreamTest extends FunSuite {
  val path = getClass.getResource("/logs").getPath.split("target")(1)
  private val relativePath = s"target$path"

  test("AkkaLogStream should filter the logs") {
    val akkaLogStream = new AkkaLogStream(relativePath)
    println(s"Printing the logs from the akka stream $relativePath")
    akkaLogStream.filter("LabSZ")
    akkaLogStream.print()
  }

}
