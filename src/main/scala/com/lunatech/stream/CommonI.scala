package com.lunatech.stream

import cats.effect.IO

/**
 * Project working on stream-in-scala
 * New File created by ani in  stream-in-scala @ 15/07/2023  17:30
 */
trait CommonI {
  def filter(filter: String): IO[Any]
  def take(n: Int): IO[Any]
  def print() : IO[Unit]

}
