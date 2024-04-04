//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._

//#main-class
object SimpleActorExample extends App {
  class SimpleActor extends Actor {
    def receive = {
      case s:String => println("String: " + s)
      case i:String => println("String: " + i)
    }

    def foo = println("Normal method")
  }
  // Create the "Hello Akka" actor system
  val system = ActorSystem("Simple-System")

  // Create the "greeter" actor
  val actor = system.actorOf(Props[SimpleActor], "simple-actor")

  // actor.foo // <- problematic due race conditions

  // Send WhoToGreet message to actor
  println("Before messages")
  actor ! "Hi there"
  println("After string")  
  actor ! 42
  println("After int")
  actor ! 'a' // <- not sent due strong typed receive method
  println("After char")

  // Shutdown actorsystem
  // system.shutdown() // <- same thing but deprecated
  // system.terminate() // <- but if you don't ommit this, the program won't stop
}