package client

import algos._
import japgolly.scalajs.react.{ReactDOM, _}
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.{CanvasRenderingContext2D, UIEvent, document}

import scala.collection.mutable
import scala.scalajs.js.annotation.JSExport
import scala.util.Try

@JSExport
object jsClient {

  @JSExport
  def main(target: org.scalajs.dom.raw.Element): Unit = {

    val theApp = ReactComponentB[Unit]("TheApp")
      .render($ => {
        <.div(
          <.div(
            ^.padding := 2,
            <.canvas(
              ^.id := "packedBinCanvas",
              ^.height := 110
            )
          ),
          <.div(
            ^.padding := 2,
            <.button(
              "Repopulate and Pack",
              ^.id := "repopulateAndPackButton",
              ^.onClick --> addClickedMessage
            )
          ),
          <.div(
            ^.padding := 2,
            <.input(
              ^.`type` := "file",
              ^.onChange ==> onFileChosen
            )
          )
        )
      })
      .build

    ReactDOM.render(theApp(), target)
  }

  def onFileChosen(e: ReactEventI): Callback = {

    var items = new scala.collection.mutable.Queue[(String, Double)]()

    var reader = new dom.FileReader();

    reader.readAsText(e.currentTarget.files.item(0))
    reader.onload = (e: UIEvent) => {
      val lines = reader.result.asInstanceOf[String].split("\r\n")

      for (line <- lines) {
        println("line: " + line)

        val contents = line.split(",")

        val value = parseDouble(contents(1))

        println("data: " + contents(0) + "," + contents(1) + "," + value)

        items.enqueue(new Tuple2[String, Double](contents(0), value match { case Some(value) => value case None => 0.0 }))
      }

      println("Parsed " + items.length + " items")

      packAndDraw(items)
    }

    Callback.empty
  }

  def parseDouble(s: String): Option[Double] = Try { s.toDouble }.toOption

  def addClickedMessage(): Callback = {
    val items = packer.randomPopulation

    packAndDraw(items)

    Callback.empty
  }

  def packAndDraw(items: mutable.Queue[(String, Double)]): Unit = {
    val bins = new Bins(10)

    while (items nonEmpty) packer.placeUsingAverageBestFit(bins, (items dequeue) _2)

    clearChart

    drawChart(bins)
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