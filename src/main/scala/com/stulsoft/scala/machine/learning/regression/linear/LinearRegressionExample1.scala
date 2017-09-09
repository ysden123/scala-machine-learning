/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.scala.machine.learning.regression.linear


import com.stulsoft.scala.machine.learning.data.generator.LinearDataGenerator
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.{SparkConf, SparkContext}

/** Linear regression
  *
  * @see [[http://www.cakesolutions.net/teamblogs/spark-mllib-linear-regression-example-and-vocabulary Spark MLlib linear regression example and vocabulary]]
  * @author Yuriy Stul
  */
object LinearRegressionExample1 extends App {
  val sc = new SparkContext(new SparkConf().setAppName("LinearRegressionExample1").setMaster("local[*]"))

  // Prepara data
  val data = sc.parallelize(prepareFeaturesWithLabels(prepareFeatures()))

  // Split data on training (80%) and test (20%)
  val splits = data randomSplit Array(0.8, 0.2)
  val training = splits(0) cache
  val test = splits(1) cache

  // Specify algorithm
  val algorithm = new LinearRegressionWithSGD()
  // Build model
  val model = algorithm run training
  // Calculate predicted values
  val prediction = model predict (test map (_ features))
  // Add labels to calculated predictions
  val predictionAndLabel = prediction zip (test map (_ features))

  // Output predicted label and actual labels
  predictionAndLabel.foreach((result) => println(s"predicted label: ${result._1}, actual label: ${result._2}"))

  /**
    * Prepares features
    *
    * @return collction with features
    */
  def prepareFeatures(): Seq[org.apache.spark.mllib.linalg.Vector] = {
    val data = LinearDataGenerator(2.0, 0.0, 10, 2.0, 100).generateData()
    //    val data = LinearDataGenerator(2.0, 0.0, 10, 2.0, 10).generateData()
    val maxX = data map (_ _1) max
    val maxY = data map (_ _2) max

    data.sortBy(_ _1).map { d => Vectors.dense(d._1 / maxX, d._2 / maxY) }
  }

  /**
    * Adds labels to features
    *
    * @param features the features
    * @return features with labels
    */
  def prepareFeaturesWithLabels(features: Seq[org.apache.spark.mllib.linalg.Vector]): Seq[LabeledPoint] =
    (0d to 1 by (1d / features.length)) zip features map (l => LabeledPoint(l._1, l._2))
}
