//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._
import scala.collection.parallel.CollectionConverters._

//#main-class
object ParallelCollect extends App {
  def fib(n: Int): Int = if (n < 2) 1 else fib(n-1)+fib(n-2)

  println("Hello")
  println(fib(5))

  // this one works ordered
  for(i <- 30 to 15 by -1) {
    println(fib(i))
  }

  // this one does not
  // but uses multithreading paralellism
  for(i <- (30 to 15 by -1).par) {
    println(fib(i))
  }

  // race condition
  // it wont reach the total number
  /*
  var i = 0
  for(j <- (1 to 10000000).par) i+= 1
  println(i)
  */

  // parallelism makes immutability challenging
  var i = 0
  for(j <- (1 to 10000000).par) {
    // load i from memory
    // add 1 to register
    // store i to memory
    i+= 1
  }
  println(i)
}