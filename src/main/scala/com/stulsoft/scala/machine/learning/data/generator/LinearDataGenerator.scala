/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.scala.machine.learning.data.generator

import com.stulsoft.scala.machine.learning.chart.XYLineChart
import org.jfree.ui.RefineryUtilities

import scala.util.Random

/** Linear Data Generator
  *
  * @author Yuriy Stul
  */
case class LinearDataGenerator(k: Double, offset: Double, deviation: Double, step: Double, n: Int) {

  def generateData(): Seq[(Double, Double)] = {
    (1 to n).map(i => {
      val x = step * i
      val theDeviation = (if (Random.nextBoolean()) 1 else -1) * deviation * Random.nextDouble()
      val y = offset + k * x + theDeviation
      (x, y)
    })
  }
}

/**
  * Test app for LinearDataGenerator
  */
object LinearDataGeneratorTest extends App {
  val data = LinearDataGenerator(2.0, 0.0, 10, 2.0, 100).generateData()
  val chart = new XYLineChart("Data generator", "Linear data generator", "simple line", data)
  chart.pack()
  RefineryUtilities.centerFrameOnScreen(chart)
  chart.setVisible(true)
}
