package client

import algos._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import dom.{CanvasRenderingContext2D, document}
import org.scalajs.dom.html.Canvas
import org.scalajs.jquery.jQuery

@JSExport
object jsClient extends JSApp {

  @JSExport
  def main(): Unit = {
    jQuery(setupUI _)
  }

  def setupUI(): Unit = {
    document.createElement("canvas")

    jQuery("#click-me-button").click(addClickedMessage _)
    jQuery("body").append("<p>Packing with average best fit algorithm</p>")
  }

  def addClickedMessage(): Unit = {
    val items = packer.randomPopulation

    val bins = new Bins(10)

    while(items nonEmpty) packer.placeUsingAverageBestFit(bins, (items dequeue) _2)

    clearChart

    drawChart(bins)
  }


  def drawChart(bins: Bins): Unit = {
    // probably don't want to fetch this every time we draw anything ...
    var c = document.getElementById("theCanvas").asInstanceOf[Canvas].getContext("2d").asInstanceOf[CanvasRenderingContext2D]

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
    var c = document.getElementById("theCanvas").asInstanceOf[Canvas].getContext("2d").asInstanceOf[CanvasRenderingContext2D]
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