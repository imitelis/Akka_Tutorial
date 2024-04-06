//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor, OneForOneStrategy }
import akka.pattern._
import akka.actor.SupervisorStrategy._

//#main-class
object SupervisorExample extends App {
  case object CreateChild
  case class SignalChildren(order: Int)
  case class PrintSignal(order: Int)
  case class DivideNumbers(n: Int, d: Int)
  case object BadStuff

  class ParentActor extends Actor {
    private var number = 0
    def receive = {
      case CreateChild => 
        context.actorOf(Props[ChildActor], "child-" + number)
        number += 1
      
      case SignalChildren(n) =>
        context.children.foreach(_ ! PrintSignal(n))
    }

    override val SupervisorStrategy = OneForOneStrategy(loggingEnable = false) {
      case ae:ArithmeticException => Resume // <- several types of strategy operations
      case _:Exception => Restart
      case BadStuff => throw new RuntimeException("Stuff happened")
    }
  }

  class ChildActor extends Actor {
    def receive = {
      case PrintSignal(n) => println(n + "" +self)
      case DivideNumbers(n, d) => println(n/d)
    }
  }

  val system = ActorSystem("Hierarchy-System")
  val actor = system.actorOf(Props[ParentActor], "supervisor-actor")

  actor ! CreateChild
  actor ! CreateChild

  val child0 = system.actorSelection("akka://Hierarchy-System/user/supervisor-actor/child-0")

  child0 ! DivideNumbers(4, 0) // <- zero division error
  child0 ! DivideNumbers(4, 2) // <- but this one shows
  child0 ! BadStuff

  // one for one strategy
  // all for one strategy
  
  Thread.sleep(1000)
  system.terminate()
}