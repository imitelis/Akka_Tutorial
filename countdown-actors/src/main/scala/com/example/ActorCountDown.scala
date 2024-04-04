//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._

//#main-class
object ActorCountDown extends App {
  case class StartCounting(n: Int, other: ActorRef)
  case class CountDown(n: Int)

  class CountDownActor extends Actor {
    def receive = {
      case StartCounting(n, other) =>
        println(n)
        other ! CountDown(n-1)

      case CountDown(n) =>
        println(self)
        if (n>0) {
          println(n)
          sender ! CountDown(n-1)
        } else {
          // system.terminate() // <- I can not
          context.system.terminate() // <- I can
        }
    }
  }
  // Create the "CountDown" actor system
  val system = ActorSystem("CountDown-System")

  // Create the "countdown" actors
  val actor1 = system.actorOf(Props[CountDownActor], "countdown-actor1")
  val actor2 = system.actorOf(Props[CountDownActor], "countdown-actor2")
  
  actor1 ! StartCounting(10, actor2)
}