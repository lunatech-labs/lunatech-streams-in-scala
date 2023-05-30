val lazyList = (2 to 100_000_000).to(LazyList)
lazyList.take(5)
lazyList.takeWhile(_ < 7).foreach(println(_) )
println(lazyList)


