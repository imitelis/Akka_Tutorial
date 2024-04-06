//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import akka.pattern._
import scala.concurrent.duration._ // <- for future
import akka.util.Timeout // <- akka and not scala
import scala.util.Failure
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global // <- global execution context
import scala.concurrent.Future

//#main-class
object AskPattern extends App {
  case object AskName // <- since it has no information inside of it
  case class NameResponse(name: String)
  case class AskNameOf(other: ActorRef)

  class AskActor(val name: String) extends Actor {
    // implicit val ec = context.system.dispatcher // <- akka dispatcher instead context inside actor

    def receive = {
      case AskName => 
        Thread.sleep(10) // <- if I set the sleep to 10 seconds (10000) the program will terminate before the response
        sender ! NameResponse(name)
      
      case AskNameOf(other) => // <- we never block, we ask a future
        val f = other ? AskName
        f.onComplete {
          case Success(NameResponse(n)) =>
            println("They said their name was" + n)
          case Success(s) =>
            println("They didn't tell us their name")
          case Failure(ex) =>
            println("Asking their name failed")
        }

        // you can use future for difficult computations
        // Future {}
        // but never pass sender method in it
        // Future{ sender ! "message" }
        // sender will change due multiple operations

        // use closures instead
        val currentSender = sender
        Future {
          currentSender ! "message"
        }

    }
  }
  // Create the "Ask" actor system
  val system = ActorSystem("Ask-System")

  // Create the "ask" actor
  val actor1 = system.actorOf(Props(new AskActor("Pat")), "ask-actor-1") // <- since it requires a name
  val actor2 = system.actorOf(Props(new AskActor("Val")), "ask-actor-2") // <- since it requires a name
  // implicit val ec = system.dispatcher

  // actor ? AskName // <- Asks an actor and uses implicitly timeout
  implicit val timeout: Timeout = Timeout(1.seconds)  
  val answer = actor1 ? AskName // <- answer is a future

  answer.foreach(n => println("Name is " + n)) // <- mapping for futures

  actor1 ! AskNameOf(actor2)

  system.terminate()  
}