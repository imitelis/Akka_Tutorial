//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._

//#main-class
object SimpleActorExample extends App {
  class MyActor extends Actor {
    def receive: Receive = {
      case msg => println(s"Received: $msg")
    }
  }
  
  val system = ActorSystem("Selection-System")
  
  // Creating an actor with a reference
  val actor = system.actorOf(Props[MyActor], "my-actor")
  
  // Sending a message to the actor using its reference
  actor ! "Hello, Actor!"

  // Creating an actor selection
  val selection = system.actorSelection("/user/my-actor")
  
  // Sending a message to the actor using selection
  selection ! "Hello, Actor Selection!"

  // Shutting down the system
  system.terminate()
}