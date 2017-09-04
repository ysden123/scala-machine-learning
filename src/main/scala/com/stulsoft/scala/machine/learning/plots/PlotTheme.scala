package com.stulsoft.scala.machine.learning.plots

import java.awt.{Color, GradientPaint, Paint}
import com.stulsoft.scala.machine.learning.core.Types._

/**
  * Generic trait for visual display of a plotting graph using jFreeChart library
  *
  * @author Patrick Nicolas
  * @since 0.97 November 20, 2013
  * @version 0.97
  * @see Scala for Machine Learning "Appendix" Visualization
  * @see http://www.jfree.org
  */
private[learning] trait PlotTheme {
  /**
    * Select the color from an existing palette or list compatible
    * with the background of the plot.
    *
    * @param index Index of the color from the palette
    * @return color for a specific component of the plot
    */
  def color(index: Int): Color

  /**
    * Define the background color of the plot
    *
    * @param width  Width of the chart
    * @param height Height of the chart
    * @return Background color
    */
  def paint(width: Int, height: Int): Paint
}


/**
  * Class that define the visual display of a plotting graph using jFreeChart library
  * with a black background. The color of the data points, graphs, labels.. are set accordingly.
  *
  * @see org.jfree
  * @author Patrick Nicolas
  * @since 0.97 November 20, 2013
  * @version 0.97
  * @see Scala for Machine Learning Chapter 2 Hello World!
  */
final class BlackPlotTheme extends PlotTheme {
  private[this] val colorList = Array[Color](Color.white, Color.cyan, Color.yellow)

  /**
    * Select the color from an existing palette or list compatible
    * with the background of the plot.
    *
    * @param index Index of the color from the palette
    * @return color for a specific component of the plot
    */
  override def color(index: Int): Color = colorList(index % colorList.length)

  /**
    * Define the background color of the plot at black
    *
    * @param width  Width of the chart
    * @param height Height of the chart
    * @throws IllegalArgumentException if the width or height is out of range
    * @return Background color
    */
  override def paint(width: Int, height: Int): Paint = {
    Plot.validateDisplaySize(width, height, emptyString)
    Color.black
  }
}

/**
  * Class that define the visual display of a plotting graph using jFreeChart library
  * with a light grey background with gradient. The color of the data points,
  * graphs, labels.. are set accordingly.
  *
  * @author Patrick Nicolas
  * @since 0.97 November 20, 2013
  * @version 0.97
  * @see Scala for Machine Learning "Appendix" Visualization
  * @see http://www.jfree.org
  */
final class LightPlotTheme extends PlotTheme {
  private[this] val colorList = Array[Color](Color.black, Color.red, Color.green)

  /**
    * Select the color from an existing palette or list compatible
    * with the background of the plot.
    *
    * @param index Index of the color from the palette
    * @return color for a specific component of the plot
    */
  override def color(index: Int): Color = colorList(index % colorList.length)

  /**
    * Define the background color of the plot as a gradient of light gray
    *
    * @param width  Width of the chart
    * @param height Height of the chart
    * @throws IllegalArgumentException if the width or height is out of range
    * @return Background color paletter
    */
  override def paint(width: Int, height: Int): Paint = {
    Plot.validateDisplaySize(width, height, emptyString)
    new GradientPaint(0, 0, Color.white, width, height, Color.lightGray, false)
  }
}