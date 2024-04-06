//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import akka.pattern._

//#main-class
object HierarchyExample extends App {
  case object CreateChild
  // moving into case classes for referencing children actors
  // case object SignalChildren
  // case object PrintSignal
  case class SignalChildren(order: Int)
  case class PrintSignal(order: Int)  

  class ParentActor extends Actor {
    private var number = 0
    // private val children = collection.mutable.Buffer[ActorRef]()
    def receive = {
      case CreateChild => 
        // children += context.actorOf(Props[ChildActor], "child" + number) // <- unique names otherwise error
        context.actorOf(Props[ChildActor], "child-" + number)
        number += 1
      
      case SignalChildren(n) =>
        context.children.foreach(_ ! PrintSignal(n)) // careful
    }
  }

  class ChildActor extends Actor {
    def receive = {
      case PrintSignal(n) => println(n + "" +self) // <- information about (children) actor
    }
  }

  val system = ActorSystem("Hierarchy-System")
  val actor = system.actorOf(Props[ParentActor], "parent-actor-1")
  val actor2 = system.actorOf(Props[ParentActor], "parent-actor-2")

  actor ! CreateChild
  // actor ! SignalChildren // <- child0 gets printed
  actor ! SignalChildren(2) // <- now I can reference children
  actor ! CreateChild
  actor ! CreateChild
  actor ! SignalChildren // <- all children get printed
  
  actor2 ! CreateChild
  val child0 = system.actorSelection("akka://Hierarchy-System/user/parent-actor-1/child-0") // <- hard coding actor URL

  child0 ! PrintSignal(3)
  
  // actors process messages in order, but before actors there is no order warranty
  // if changing the user context
  Thread.sleep(10000)
  system.terminate()
}