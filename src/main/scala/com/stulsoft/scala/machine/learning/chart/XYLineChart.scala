/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.scala.machine.learning.chart

import java.awt.Dimension

import org.jfree.chart.plot.{PlotOrientation, XYPlot}
import org.jfree.chart.renderer.xy.XYShapeRenderer
import org.jfree.chart.{ChartFactory, ChartPanel, JFreeChart}
import org.jfree.data.xy.{XYDataset, XYSeries, XYSeriesCollection}
import org.jfree.ui.ApplicationFrame

/**
  * @author Yuriy Stul
  */
class XYLineChart(applicationTitle: String, chartTitle: String, seriesName: String, data: Seq[(Double, Double)]) extends ApplicationFrame(applicationTitle) {
  val xyLineChart: JFreeChart = ChartFactory.createXYLineChart(
    chartTitle,
    "X",
    "Y",
    createDataSet(),
    PlotOrientation.VERTICAL,
    true,
    true,
    false
  )

  val chartPanel = new ChartPanel(xyLineChart)
  chartPanel.setPreferredSize(new Dimension(560, 367))
  val plot: XYPlot = xyLineChart.getXYPlot

  val renderer = new XYShapeRenderer
  plot.setRenderer(renderer)
  setContentPane(chartPanel)

  def createDataSet(): XYDataset = {
    val dataSet: XYSeriesCollection = new XYSeriesCollection()
    val series: XYSeries = new XYSeries(seriesName)
    dataSet.addSeries(series)
    data.foreach(d => series.add(d._1, d._2))
    dataSet
  }
}
