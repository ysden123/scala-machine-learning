package com.stulsoft.scala.machine.learning.core

import com.stulsoft.scala.machine.learning.core.Types.ScalaMl.DblArray

/** Enumerator that describes the fields used in the extraction of price related data from
  * the Yahoo finances historical data. The data is loaded from a CSV file.
  *
  * @see Scala for Machine Learning by Patrick Nikolas
  * @author Yuriy Stul.
  */
object YahooFinancials extends Enumeration {
  type YahooFinancials = Value
  val DATE, OPEN, HIGH, LOW, CLOSE, VOLUME, ADJ_CLOSE = Value

  import com.stulsoft.scala.machine.learning.core.data.DataSource.Fields

  private final val EPS = 1e-6

  /**
    * Convert an field to a double value
    */
  def toDouble(v: Value): Fields => Double = (s: Fields) => s(v.id).toDouble

  def toDblArray(vs: Array[Value]): Fields => DblArray =
    (s: Fields) => vs.map(v => s(v.id).toDouble)


  /**
    * Divide to fields as the ratio of their converted values.
    */
  def divide(v1: Value, v2: Value): Fields => Double =
    (s: Fields) => s(v1.id).toDouble / s(v2.id).toDouble

  def ratio(v1: Value, v2: Value): Fields => Double = (s: Fields) => {
    val den = s(v2.id).toDouble
    if (den < EPS) -1.0
    else s(v1.id).toDouble / den - 1.0
  }

  def plus(v1: Value, v2: Value): Fields => Double =
    (s: Fields) => s(v1.id).toDouble + s(v2.id).toDouble

  def minus(v1: Value, v2: Value): Fields => Double =
    (s: Fields) => s(v1.id).toDouble - s(v2.id).toDouble

  def times(v1: Value, v2: Value): Fields => Double =
    (s: Fields) => s(v1.id).toDouble * s(v2.id).toDouble

  /**
    * Extract value of the ADJ_CLOSE field
    */
  val adjClose: (Fields) => Double = (fs: Fields) => fs(ADJ_CLOSE.id).toDouble

  /**
    * Extract value of the VOLUME field
    */
  val volume: (Fields) => Double = (s: Fields) => s(VOLUME.id).toDouble


  /**
    * Computes the relative volatility as (HIGH -LOW)/LOW
    */
  val volatility: (Fields) => Double = ratio(HIGH, LOW)

  /**
    * Computes the ratio of volatility over volume
    */
  val volatilityVol: (Fields) => (Double, Double) = (s: Fields) =>
    (s(HIGH.id).toDouble - s(LOW.id).toDouble, s(VOLUME.id).toDouble)

  /**
    * Computes the difference between ADJ_CLOSE and OPEN
    */
  val closeOpen: (Fields) => Double = minus(ADJ_CLOSE, OPEN)

  /**
    * Computes the relative difference between ADJ_CLOSE and OPEN
    */
  val vPrice: (Fields) => Double = ratio(ADJ_CLOSE, OPEN)

  /**
    * Computes the ratio of relative volatility over volume as (1 - LOW/HIGH)/VOLUME
    */
  val volatilityByVol: (Fields) => Double = (s: Fields) =>
    (1.0 - s(LOW.id).toDouble / s(HIGH.id).toDouble) / s(VOLUME.id).toDouble

  val volatilityAndVol: (Fields) => (Double, Double) = (s: Fields) =>
    (1.0 - s(LOW.id).toDouble / s(HIGH.id).toDouble, s(VOLUME.id).toDouble)

}
