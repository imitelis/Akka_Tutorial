//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.MediaTypes._
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import scala.concurrent.duration._

//#main-class
object AkkaHttp extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher // "thread pool"

  val source = 
    """
    |object SimpleApp {
    | val aField = 2
    | def aMethod(x: Int) = x + 1
    | def main(args: Array[String]): Unit = println(aField)    |
    |}
    """.stripMargin

  
  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = "http://markup.su/api/highlighter",
    entity = HttpEntity(
      ContentTypes.`application/x-www-form-urlencoded`, // application/json
      s"source=$source&language=Scala&theme=Sunburst"
    )
  )

  def sendRequest(): Future[String] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    val entityFuture: Future[HttpEntity.Strict] = responseFuture.map(res => res.entity.toStrict(2.seconds))
    entityFuture.map(ent => ent.data.utf8String)
  }
                         
  def main(args: Array[String]): Unit = {
    sendRequest().foreach(println)
  }

}