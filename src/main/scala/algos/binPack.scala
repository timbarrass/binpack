package algos

import scala.collection.mutable
import scala.util.Random

class Bins(count : Int) {
  val binCount = count
  val bins = new Array[Double](binCount)
  val contents = Array.fill(binCount){List[Double]()}
  var binTotal = 0.0

  def max: Double = {
    contents map(_ sum) max
  }

  def average: Double = {
    binTotal / binCount
  }

  def placeInBin(bin : Int, value: Double) = {
    bins(bin) = bins(bin) + value
    contents(bin) = contents(bin) :+ value
    binTotal = binTotal + value
  }

  def drawBinContent(): Unit = {
    println("Bucket content")
    var index = 0

    for (content <- contents) {
      var sum : Double = (content foldLeft 0.0)((a, b) => a + b) // gah no clean sum func?
      print(f"[$sum%1.2f] ")
      for (item <- content) {
        print(f"$item%1.2f ")
      }
      print("\n")
      index = index + 1
    }
  }

  def draw(width: Double, draw: (Double, Double, Double, Double) => Unit): Unit = {
    var index = 0
    var max = bins max
    var scalar: Double = width / max

    var offset: Double = 4
    for (content <- contents) {
      var highwater = 0.0
      for (item <- content) {

        // this'd make a closure to pass in
        draw(scalar, offset, highwater, item)

        highwater = highwater + (scalar * item + 2.0)
      }

      offset = offset + 10
    }
  }

  def drawFinalLayout() : Unit = {
    println("Final bucket layout")
    var index = 0
    var width: Double = 100.0
    var scalar: Double = width / max
    for(i <- 1 to 100) {
      print(i % 10)
    }
    print("\n")

    for (content <- contents) {
      var flip = 1

      for (item <- content) {
        var i = 0

        while (i <= item * scalar) {
          print(flip % 2)
          i = i + 1
        }

        flip = flip + 1
      }
      print("\n")
    }
  }

  def chooseBin(deltaFunc: (Double) => Double): Int = {
    var chosenBin = 0
    var currentBin = 0
    var minDelta = Double.MaxValue
    for (bin <- bins) {

      var delta = deltaFunc(bin)

      if (delta < minDelta) {
        chosenBin = currentBin
        minDelta = delta
      }

      currentBin = currentBin + 1
    }

    chosenBin
  }
}

object packer {

  def placeUsingAverageBestFit(bins: Bins, value: Double) = {
    var average = bins average

    var deltaFunc = { (bin : Double) => math abs (average - bin - value) }

    var chosenBin = bins chooseBin deltaFunc

    bins placeInBin(chosenBin, value)
  }

  def deltaFunc(bin: Double, benchmark : Double, value : Double) : Double  = {
    math.abs(benchmark - bin - value)
  }

  def randomPopulation(): mutable.Queue[(String, Double)] = {
    var rnd = new Random()

    var items = mutable.Queue.fill(100) {
      new Tuple2[String, Double]("", rnd nextDouble)
    }

    items sortBy (i => -i._2)
  }
}