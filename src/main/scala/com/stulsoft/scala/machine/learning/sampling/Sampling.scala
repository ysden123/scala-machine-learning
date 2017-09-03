/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.scala.machine.learning.sampling

import java.io.{File, FileWriter}

import scala.io.Source
import scala.reflect.ClassTag
import scala.util.{Properties, Random}
import scala.util.hashing.MurmurHash3._

/**
  * See "Mastering Scala Machine Learning by Alez Kozlov", chaprt 1
  *
  * @author Yuriy Stul
  */
object Sampling extends App {

  sampling01()
  sampling02()
  sampling03()

  def sampling01(): Unit = {
    val threshold = 0.05
    val lines = Source.fromURL(getClass.getResource("/data/iris/in.txt")).getLines()
    val newLines = lines.filter(_ => Random.nextDouble() <= threshold)
    val w = new FileWriter(new File("out1.txt"))
    newLines.foreach { s => w.write(s + Properties.lineSeparator) }
    w.close()
  }

  def sampling02(): Unit = {
    def reservoirSample[T: ClassTag](input: Iterator[T], k: Int): Array[T] = {
      val reservoir = new Array[T](k)
      // Put the first k elements in the reservoir
      var i = 0
      while (i < k && input.hasNext) {
        reservoir(i) = input.next()
        i += 1
      }
      if (i < k) {
        // Input size < k, trim the array size
        reservoir.take(i)
      } else {
        // If input size > k, continue the sampling process.
        while (input.hasNext) {
          val item = input.next()
          val replacementIndex = Random.nextInt(i)
          if (replacementIndex < k)
            reservoir(replacementIndex) = item
        }
        i += 1
      }
      reservoir
    }

    val numLines = 15
    val w = new FileWriter(new File("out2.txt"))
    val lines = Source.fromURL(getClass.getResource("/data/iris/in.txt")).getLines()
    reservoirSample(lines, numLines).foreach(s => w.write(s + Properties.lineSeparator))
    w.close()
  }

  def sampling03(): Unit = {
    def consistentFilter(s: String, seed: Int, markLow: Int, markHigh: Int): Boolean = {
      val hash = stringHash(s.split(" ")(0), seed) >>> 16
      hash >= markLow && hash < markHigh
    }

    val w = new FileWriter(new File("out3.txt"))
    val lines = Source.fromURL(getClass.getResource("/data/iris/in.txt")).getLines()
    lines.filter(s => consistentFilter(s, 12345, 0, 4096))
      .foreach(s => w.write(s + Properties.lineSeparator))
    w.close()
  }
}
