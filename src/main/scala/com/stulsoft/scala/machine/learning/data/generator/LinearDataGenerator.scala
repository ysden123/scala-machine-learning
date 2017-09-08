/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.scala.machine.learning.data.generator

import com.stulsoft.scala.machine.learning.chart.XYLineChart
import org.jfree.ui.RefineryUtilities

/** Linear Data Generator
  *
  * @author Yuriy Stul
  */
case class LinearDataGenerator(k: Double, offset: Double, deviation: Double, step: Double, n: Int) {
  def generateData(): Seq[(Double, Double)] = {
    for {i <- 1 to n}
      yield (step * i, offset + k * step * i)
  }
}

/**
  * Test app for LinearDataGenerator
  */
object LinearDataGeneratorTest extends App {
  val data = LinearDataGenerator(2.0, 0.0, 1.5, 2.0, 10).generateData()
  val chart = new XYLineChart("Data generator", "Linear data generator", "simple line", data)
  chart.pack()
  RefineryUtilities.centerFrameOnScreen(chart)
  chart.setVisible(true)
}
