package com.stulsoft.scala.machine.learning.stats

import org.apache.log4j.Logger
import com.stulsoft.scala.machine.learning.core.Types.ScalaMl._
import com.stulsoft.scala.machine.learning.util.DisplayUtils
import com.stulsoft.scala.machine.learning.stats.Stats._

import scala.annotation.implicitNotFound
import scala.util.Try

/**
  * Parameterized class that computes the generic minimun and maximum of a time series. The class
  * implements:
  *
  * - Computation of minimum and maximum according to scaling factors
  *
  * - Normalization using the scaling factors
  *
  * @tparam T type of element of the time series view bounded to a double
  * @constructor Create MinMax class for a time series of type ''XSeries[T]''
  * @param values Time series of single element of type T
  * @param f      Implicit conversion from type T to Double
  * @throws IllegalArgumentException if the time series is empty
  * @author Patrick Nicolas
  * @author Yuriy Stul.
  * @see Scala for Machine Learning Chapter 1 ''Getting Started''
  */
@implicitNotFound(msg = "MinMax conversion to Double undefined")
@throws(classOf[IllegalArgumentException])
class MinMax[T <: AnyVal](val values: XSeries[T])(implicit f: T => Double) {
  require(values.nonEmpty, "MinMax: Cannot initialize stats with undefined values")


  def this(values: Array[T])(implicit f: T => Double) = this(values.toVector)

  /**
    * Defines the scaling factors for the computation of minimum and maximum
    *
    * @param low   lower value of the range target for the normalization
    * @param high  upper value of the range target for the normalization
    * @param ratio Scaling factor between the source range and target range.
    */
  case class ScaleFactors(low: Double, high: Double, ratio: Double)

  private val logger = Logger.getLogger("MinMax")

  private[this] val zero = (Double.MaxValue, -Double.MaxValue)
  private[this] var scaleFactors: Option[ScaleFactors] = None

  protected[this] val minMax: (Double, Double) = values./:(zero) { (mM, x) => {
    val min = mM._1
    val max = mM._2
    (if (x < min) x else min, if (x > max) x else max)
  }
  }

  /**
    * Computation of minimum values of a vector. This values is
    * computed during instantiation
    */
  final def min: Double = minMax._1

  /**
    * Computation of minimum values of a vector. This values is
    * computed during instantiation
    */
  final def max: Double = minMax._2


  @throws(classOf[IllegalStateException])
  final def normalize(low: Double = 0.0, high: Double = 1.0): DblVector =
    setScaleFactors(low, high).map(scale => {
      values.map(x => (x - min) * scale.ratio + scale.low)
    })
      .getOrElse(throw new IllegalStateException("MinMax.normalize normalization params undefined"))


  final def normalize(value: Double): Try[Double] = Try {
    scaleFactors.map(scale =>
      if (value <= min) scale.low
      else if (value >= max) scale.high
      else (value - min) * scale.ratio + scale.low
    ).get
  }

  /**
    * Normalize the data within a range [l, h]
    *
    * @param low  lower bound for the normalization
    * @param high higher bound for the normalization
    * @return vector of values normalized over the interval [0, 1]
    * @throws IllegalArgumentException of h <= l
    */
  private def setScaleFactors(low: Double, high: Double): Option[ScaleFactors] =
    if (high < low + STATS_EPS)
      DisplayUtils.none(s"MinMax.set found high - low = $high - $low <= 0 required > ", logger)

    else {
      val ratio = (high - low) / (max - min)

      if (ratio < STATS_EPS)
        DisplayUtils.none(s"MinMax.set found ratio $ratio required > EPS ", logger)
      else {
        scaleFactors = Some(ScaleFactors(low, high, ratio))
        scaleFactors
      }
    }
}


class MinMaxVector(series: Vector[DblArray]) {
  val minMaxVector: Vector[MinMax[Double]] = series.transpose.map(new MinMax[Double](_))

  @throws(classOf[IllegalStateException])
  final def normalize(low: Double = 0.0, high: Double = 1.0): Vector[DblArray] =
    minMaxVector.map(_.normalize(low, high)).transpose.map(_.toArray)


  final def normalize(x: DblArray): Try[DblArray] = {
    val normalized = minMaxVector.zip(x).map { case (from, to) => from.normalize(to) }

    if (normalized.isEmpty)
      throw new IllegalStateException("MinMax.normalize normalization params undefined")
    Try(normalized.map(_.get).toArray)
  }
}


object MinMax {
  def apply[T <: AnyVal](values: XSeries[T])(implicit f: T => Double): Try[MinMax[T]] =
    Try(new MinMax[T](values))
}
