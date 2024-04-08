//#full-example
package com.example
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.event.ActionEvent

//#main-class
object ThreadGUI extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title = "Event Thread Demo"
    scene = new Scene(150, 75) {
      val button = new Button("Click Me")
      root = button
      button.onAction = (ae: ActionEvent) => Thread.sleep(10000)
    }
  }
}