import scala.io.Source
import scala.util.Try

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
/**
 *  This would throw an out of memory exception
 val r =(1 to 10000)
  .map(_ => Seq.fill(10000)(scala.util.Random.nextDouble))
  .map(_.sum)
  .sum

 This won't because we are using streams
 val r =(1 to 10000).toStream
  .map(_ => Seq.fill(10000)(scala.util.Random.nextDouble))
  .map(_.sum)
  .sum
 */
///
val lazyList = (2 to 100_000_000).to(LazyList)
LazyList(1,2,3,4,5,6,7,8,9,10).size
lazyList.take(5)
lazyList.takeWhile(_ < 7).foreach(println(_))

val r =(1 to 10000).toStream
  .map(_ => Seq.fill(10000)(scala.util.Random.nextDouble))
  .map(_.sum)

(1 to 10000).toStream
(1 to 10000).to(LazyList)

LazyList.cons(1, LazyList.cons(2, LazyList.empty))
val result: Try[Int] = Try("r".toInt)
result match {
  case scala.util.Success(value) => println(value)
  case scala.util.Failure(exception) => println("exception")
}

"PacketResponder".substring(0, 2)

files.map {
 case (file) =>
 Source.fromFile(file).getLines().toList.filter(_.contains("Success")).take(10) //, we can't close the source because we are streaming the logs and don't know when the stream will end
}

files.map {
 case (file) =>
  Source.fromFile(file).getLines().to(LazyList).filter(_.contains("Success")).take(10) //, we can't close the source because we are streaming the logs and don't know when the stream will end
}

//  .sum
//r.headOption
//r.tail
//r.size // Be careful with this one, it will try to evaluate the whole stream
//println(r)




