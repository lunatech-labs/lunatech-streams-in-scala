import scala.io.Source
import scala.util.Try

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




