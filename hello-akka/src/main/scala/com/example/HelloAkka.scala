//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._

// Define Actor Messages
case class WhoToGreet(who: String)

// Define Greeter Actor
class Greeter extends Actor {
  def receive = {
    case WhoToGreet(who) => println(s"Hello $who")
  }
}

//#main-class
object HelloAkka extends App {
  // Create the "Hello Akka" actor system
  val system = ActorSystem("Hello-Akka")

  // Create the "greeter" actor
  val greeter = system.actorOf(Props[Greeter], "greeter")

  // Send WhoToGreet message to actor
  greeter ! WhoToGreet("Akka")

  // Shutdown actorsystem
  system.terminate()
}