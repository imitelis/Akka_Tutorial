//#full-example
package com.example
import akka.actor._
import akka.persistence._

// Define commands
case class AddItem(item: String)
case object GetItems

// Define events
case class ItemAdded(item: String)

// Define state
case class ShoppingCartState(items: List[String] = Nil) {
  def addItem(item: String): ShoppingCartState = copy(items = item :: items)
}

// Define the persistent actor
class ShoppingCartPersistenceActor extends PersistentActor {
  var state = ShoppingCartState()

  override def persistenceId: String = "shopping-cart"

  override def receiveRecover: Receive = {
    case ItemAdded(item) => state = state.addItem(item)
  }

  override def receiveCommand: Receive = {
    case AddItem(item) =>
      persist(ItemAdded(item)) { event =>
        state = state.addItem(item)
        println(s"Added item: $item")
      }
    case GetItems =>
      println(s"Items in the shopping cart: ${state.items}")
  }
}

object ShoppingCartApp extends App {
  val system = ActorSystem("ShoppingCartSystem")
  val shoppingCartActor = system.actorOf(Props[ShoppingCartPersistenceActor], "shoppingCart")

  shoppingCartActor ! AddItem("Apple")
  shoppingCartActor ! AddItem("Banana")
  shoppingCartActor ! GetItems

  Thread.sleep(1000)
  system.terminate()
}
