package com.stulsoft.scala.machine.learning.core

import org.scalatest.{FlatSpec, Matchers}
import com.stulsoft.scala.machine.learning.core.Types.ScalaMl

/**
  * @author Yuriy Stul.
  */
class TypesTest extends FlatSpec with Matchers {
  behavior of "Types"
  "DblPair" should "create Tuple(Double,Double)" in {
    val pair = ScalaMl.Pair(1.0, 2.0)
    pair.p._1 shouldBe a[java.lang.Double]
    pair.p._1 should equal(1.0)
    pair.p._2 shouldBe a[java.lang.Double]
    pair.p._2 should equal(2.0)
  }
}
