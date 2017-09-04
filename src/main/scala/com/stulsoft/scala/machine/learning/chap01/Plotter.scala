package com.stulsoft.scala.machine.learning.chap01

import com.stulsoft.scala.machine.learning.plots._
import com.stulsoft.scala.machine.learning.stats.MinMax
import com.stulsoft.scala.machine.learning.utils.PSparkUtil

import scala.io.Source

/**
  * @see Scala for Machine Learning Chapter 1 "Getting Started" Let's kick the tires / Plotting
  * @author Yuriy Stul.
  */
object Plotter extends App {
  test()

  def test(): Unit = {
    println("Evaluation of interface to JFreeChart library")

    val PRICE_COLUMN_INDEX = 6
    val OPEN_COLUMN_INDEX = 1
    val VOL_COLUMN_INDEX = 5
    val HIGH_INDEX = 2
    val LOW_INDEX = 3

    // Load data from the source
    val src = Source.fromFile(PSparkUtil.getResourceFilePath("data/chap1/CSCO.csv"))
    val fields = src.getLines().map(_.split(","))

    val cols = fields.drop(1)
    val volatilityVolume = cols.map(f =>
      (f(HIGH_INDEX).toDouble - f(LOW_INDEX).toDouble,
        f(VOL_COLUMN_INDEX).toDouble)).toVector.unzip

    val volatility = MinMax[Double](volatilityVolume._1).get.normalize(0.0, 1.0)
    val normVolume = MinMax[Double](volatilityVolume._2).get.normalize(0.0, 1.0)

    println("Line plot for CSCO stock normalized volume")
    val labels1 = Legend("Plotter", "Line plotting CSCO 2012-13 Stock volume", "Volume", "r")
    LinePlot.display(normVolume, labels1, new LightPlotTheme)

    println("Line plot for CSCO stock volatility")
    val labels2 = Legend(
      "Plotter", "Line plotting CSCO 2012-13 Stock Volatility", "Volatility", "r"
    )
    ScatterPlot.display(volatility.toArray, labels2, new BlackPlotTheme)

    println("Scatter plot CSCO stock volatility vs. volume")
    val labels3 = Legend(
      "Plotter", "Line plotting CSCO 2012-2013 volatility vs. volume", "Volatility vs. Volume", "r"
    )
    ScatterPlot.display(volatility.zip(normVolume.view), labels3, new LightPlotTheme)

    src.close()
  }
}
