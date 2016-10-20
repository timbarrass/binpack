package client

import algos._
import japgolly.scalajs.react.{ReactDOM, _}
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.{CanvasRenderingContext2D, document}

import scala.scalajs.js.annotation.JSExport

@JSExport
object jsClient {

  @JSExport
  def main: Unit = {

    val theApp = ReactComponentB[Unit]("TheApp")
      .render($ => {
        <.div(
          <.div(
            <.canvas(
              ^.id := "packedBinCanvas"
            )
          ),
          <.div(
            <.button(
              "Repopulate and Pack",
              ^.id := "repopulateAndPackButton",
              ^.onClick --> addClickedMessage
            )
          )
        )
      })
      .build

    ReactDOM.render(theApp(), document.getElementById("main"))
  }

  def addClickedMessage(): Callback = {
    val items = packer.randomPopulation

    val bins = new Bins(10)

    while(items nonEmpty) packer.placeUsingAverageBestFit(bins, (items dequeue) _2)

    clearChart

    drawChart(bins)

    Callback.empty
  }


  def drawChart(bins: Bins): Unit = {
    // probably don't want to fetch this every time we draw anything ...
    var c = document.getElementById("packedBinCanvas").asInstanceOf[Canvas].getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    var draw = { (scalar: Double, offset: Double, highwater: Double, item: Double) =>
      if (item > 0.75) c.fillStyle = "red"
      else if (item > 0.50) c.fillStyle = "orange"
      else if (item > 0.25) c.fillStyle = "yellow"
      else c.fillStyle = "green"
      c.fillRect(4 + highwater, offset, scalar * item, 8)
    }

    bins draw(200, draw)
  }

  def clearChart: Unit = {
    var c = document.getElementById("packedBinCanvas").asInstanceOf[Canvas].getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    c.fillStyle = "black"
    c.fillRect(2, 2, 296, 102)
    c.strokeRect(0, 0, 300, 106)
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }
}