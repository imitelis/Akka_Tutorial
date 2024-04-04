//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._ // <- future
import akka.pattern._
import akka.utils.timeout // <- timeout
import scala.concurrent.ExecutionContext.Implicits.global

//#main-class
object AskPattern extends App {
  case object AskName // <- since it has no information inside of it
  case class NameResponse(name: String)

  class AskActor(val name: String) extends Actor {
    def receive = {
      case AskName => 
        Thread.sleep(10000)
        sender ! NameResponse(name)
    }
  }
  // Create the "Ask" actor system
  val system = ActorSystem("Ask-System")

  // Create the "ask" actor
  val actor = system.actorOf(Props(new AskActor("Pat")), "ask-actor") // <- since it requires a name

  implicit val timeout = Timeout(1.seconds)
  // actor ? AskName // <- requires akka.pattern as it uses implicit timeout
  val answer = actor ? AskName // <- answer is a future

  answer.foreach(n => println("Name is " + n))

  system.terminate()  
}