//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._

//#main-class
object SchedulerExample extends App {
  case object Count

  class SchedulerActor extends Actor {
    var n = 0
    def receive = {
      case Count =>
        n += 1
        println(n)
    }

    def foo = println("Normal method")
  }
  
  val system = ActorSystem("Scheduler-System")
  val actor = system.actorOf(Props[SchedulerActor], "scheduler-actor")
  implicit val ec = system.dispatcher

  actor ! Count

  system.scheduler.scheduleOnce(1.second)(actor ! Count)
  val can = system.scheduler.schedule(0.second, 100.millis, actor, Count)

  Thread.sleep(2000)
  can.cancel // <- removes coordinated message
  system.terminate()
}