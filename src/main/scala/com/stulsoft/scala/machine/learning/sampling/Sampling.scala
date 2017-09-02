/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.scala.machine.learning.sampling

import java.io.{File, FileWriter}

import scala.io.Source
import scala.util.{Properties, Random}

/**
  * See "Mastering Scala Machine Learning by Alez Kozlov", chaprt 1
  *
  * @author Yuriy Stul
  */
object Sampling extends App {

  sampling01()

  def sampling01(): Unit = {
    val threshold = 0.05
//    val lines = Source.fromFile("data/iris/in.txt").getLines()
    val lines = Source.fromURL(getClass.getResource("/data/iris/in.txt")).getLines()
    val newLines = lines.filter(_ => Random.nextDouble() <= threshold)
    val w = new FileWriter(new File("out.txt"))
    newLines.foreach { s => w.write(s + Properties.lineSeparator) }
    w.close()
  }
}
