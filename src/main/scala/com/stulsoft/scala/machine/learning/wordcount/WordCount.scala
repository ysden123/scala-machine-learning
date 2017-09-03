package com.stulsoft.scala.machine.learning.wordcount


import com.stulsoft.scala.machine.learning.utils.PSparkUtil
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source
import scala.util.Properties

/**
  * @author Yuriy Stul.
  */
object WordCount extends App {
  calculateWordCount()
  calculateWordCountWithSpark()

  def calculateWordCount(): Unit = {
    val lines = Source.fromURL(getClass.getResource("/data/wordcount/words.txt")).getLines().toSeq
    val counts = lines.flatMap(line => line.split("\\W+")).sorted
      .foldLeft(List[(String, Int)]()) { (r, c) =>
        r match {
          case (key, count) :: tail =>
            if (key == c) (c, count + 1) :: tail
            else (c, 1) :: r
          case Nil =>
            List((c, 1))
        }
      }
    println(s"calculateWordCount- counts: ${counts.sortBy(t => t._1)}")
  }

  def calculateWordCountWithSpark(): Unit = {
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("error")

    val linesRdd = sc.textFile(PSparkUtil.getResourceFilePath("data/wordcount/words.txt"))
    val counts = linesRdd.flatMap(line => line.split("\\W+"))
      .map(_.toLowerCase)
      .map(word => (word,1))
      .reduceByKey(_ + _)
      .collect()

    println(Properties.lineSeparator)
    println(s"calculateWordCountWithSpark- counts: ${counts.toList.sortBy(t => t._1)}")

    sc.stop
  }
}
