package com.lunatech.stream

import akka.actor.ActorSystem

import scala.io.StdIn
import scala.util.Try
/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 08/07/2023  16:56
 */
class StreamManager {
  implicit val system: ActorSystem = ActorSystem("LogFileReadingSystem")

  val path = getClass.getResource("/logs").getPath.split("target")(1)
  private val relativePath = s"target$path"

  def runLogStream = {
     val allLogsPaths = LogStream.readDirectories(relativePath, ".log")
     val turple2 = LogStream.streamLogsFiles(allLogsPaths)
     val logs = turple2._2
     val sources = turple2._1
    (logs, sources)
  }



   def runScalaStreamLog = {
    val (logs, sources) = runLogStream

    var isReading = true

    while (isReading) {
      println(s"logs: ${logs}")
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
            logs.filter(_.contains(filter)).compute().foreach(println)
          }
          else {
            logs.compute(input).foreach(println)
          }
        }
        isReading = false
      } else if (inputLineOrFilter == "f") {
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
            logs.filter(_.contains(filter)).compute(input).foreach(println)
          }
        }
        else {
          logs.filter(_.contains(filter)).compute().foreach(println)
        }
        isReading = false
      }
      else {
        println(s"Please enter l or f, inputing $inputLineOrFilter is not a valid option")
      }
      isReading = true

    }
    sources.foreach(_.close())
  }

   def runAkkaStreamLog = {
    val akkaLogStream = new AkkaLogStream(relativePath)
    val logs = akkaLogStream.processLogFiles
    var isReading = true
     while (isReading) {
       println(s"logs: ${logs}")
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
             logs.filter(_.contains(filter)).take(input).runForeach(println)
           }
           else {
             logs.take(input).runForeach(println)
           }
         }
         isReading = false
       } else if (inputLineOrFilter == "f") {
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
             logs.filter(_.contains(filter)).take(input).runForeach(println)
           }
         }
         else {
           logs.filter(_.contains(filter)).take(50).runForeach(println)
         }
         isReading = false
       }
       else {
         println(s"Please enter l or f, inputing $inputLineOrFilter is not a valid option")
       }
       isReading = true

     }
  }


  /**
   *
   * Challenges of building streaming applications
   * Demand propagation: Managing the demand from downstream stages and propagating it correctly to upstream stages can be challenging. It requires understanding the flow of demand signals, handling various demand strategies (e.g., batching, aggregating), and ensuring appropriate buffering and error handling mechanisms are in place.
   *
   * Buffer sizing and tuning: Choosing the appropriate buffer sizes for stages and streams is crucial. It involves considering factors such as the rate of data production, the processing speed of consumers, and the available system resources. Finding the right balance is essential to avoid buffer overflows or excessive memory consumption.
   *
   * Error handling and recovery: Dealing with failures and errors in the consumer can be complex. When an error occurs, it is important to handle it gracefully, propagate it appropriately, and possibly implement recovery strategies. This might involve resuming the stream with default values, retrying failed elements, or logging and reporting errors for manual intervention.
   *
   * Resource management: Consumers may need to manage external resources, such as database connections or network connections, efficiently. Acquiring and releasing these resources in a timely manner while considering the backpressure dynamics can be challenging. It requires careful resource management strategies and using appropriate constructs like connection pools or resource pools.
   *
   * Integration with external systems: When consuming data from external systems, compatibility and integration challenges can arise. Ensuring proper serialization/deserialization, handling data format mismatches, dealing with rate limits or throttling mechanisms imposed by external systems, and managing the coordination between Akka Stream and external APIs can be complex.
   */
}
