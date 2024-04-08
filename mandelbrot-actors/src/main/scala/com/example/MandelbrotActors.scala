//#full-example
package com.example
import akka.actor.{ ActorRef, ActorSystem, Props, Actor, OneForOneStrategy }
import akka.pattern._
import akka.actor.SupervisorStrategy._
import akka.routing.BalancingPool
import scalafx.scene.image.PixelWriter
import scalafx.scene.paint.Color

//#main-class
object MandelbrotActors extends JFXApp {
  val MaxCount = 10000
  val ImageSize = 600
  val XMin = -1.5
  val XMax = 0.5
  val YMin = -1.0
  val YMax = 1.0

  case class Complex(real: Double, imag: Double) {
    def +(that: Complex) = Complex(real + that.real, imag + that.imag)
    def *(that: Complex) = Complex(real * that.real - imag * that.imag,
    real * that.imag + imag * that.real)
    def mag = math.sqrt(real * real + imag * imag)
  }

  def mandelCount(c: Complex): Int = {
    var cnt = 0
    var z = Complex(0, 0)
    while (cnt < MaxCount && z.mag < 4) {
      z = z * z + c
      cnt += 1
    }
    cnt
  }

  case class Line(row: Int, y: Double)
  class LineActor(pw: PixelWriter) extends Actor {
    def receive = {
      case Line(row, y) =>
        for(j <- 0 until ImageSize) {
          val x = XMin + j*(XMax-XMin)/ImageSize
          val cnt = mandelCount(Complex(x,y))

        }
    }
  }

  val system = ActorSystem("Mandelbrot-System")

  stage = new JFXApp.PrimaryStage {
    title = "Actor Mandelbrot"
    scene = new Scene(ImageSize, ImageSize) {
      val image = new WritableImage(ImageSize, ImageSize)
      content = new ImageView(image)
    }
  }
}
