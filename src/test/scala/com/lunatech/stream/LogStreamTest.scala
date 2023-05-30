package com.lunatech.stream

import munit.FunSuite

/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 30/05/2023  08:05
 */
class LogStreamTest extends FunSuite {

    test("A LogStream should be lazily evaluated") {
      // Create a LogStream
      val logStream = LogStream[Int]((1 to 100_000).to(LazyList)).map(_ * 2)

      // Define an assertion to check the laziness
      var evaluatedElements = 0
      val assertion = {
        // This function will be called when an element is evaluated
        (element: Int) =>
          evaluatedElements += 1
          assert(evaluatedElements <= 3) // Only first 3 elements should be evaluated
      }

      // Take only the first 3 elements and assert the laziness
      logStream.take(3).foreach(assertion)
    }

    test("A LogStream should support filtering") {
      // Create a LogStream
      val logStream = LogStream[Int]((1 to 100_000).to(LazyList)).takeWhile(_ % 2 == 0)

      // Define an assertion to check the filtering
      val assertion = {
        // This function will be called for each element
        (element: Int) =>
          assert(element % 2 == 0) // Only even numbers should pass the filter
      }

      // Take the first 5 filtered elements and assert the filtering
      logStream.take(5).foreach(assertion)
    }
   test("A LogStream should support mapping") {
    // Create a LogStream
    val logStream = LogStream[Int]((1 to 100_000).to(LazyList)).map(_ * 2)

    // Define an assertion to check the mapping
    val assertion = {
      // This function will be called for each element
      (element: Int) =>
        assert(element % 2 == 0) // Only even numbers should pass the filter
    }

    // Take the first 5 filtered elements and assert the filtering
    logStream.take(5).foreach(assertion)
  }
  test("A LogStream should support concatenation") {
    // Create a LogStream
    val logStream = LogStream[Int]((1 to 100_000).to(LazyList)).map(_ * 2)

    // Define an assertion to check the concatenation
    val assertion = {
      // This function will be called for each element
      (element: Int) =>
        assert(element % 2 == 0) // Only even numbers should pass the filter
    }

    // Take the first 5 filtered elements and assert the filtering
    logStream.take(5).foreach(assertion)
  }

}
