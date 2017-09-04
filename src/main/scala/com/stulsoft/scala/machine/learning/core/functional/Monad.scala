package com.stulsoft.scala.machine.learning.core.functional

import scala.language.higherKinds

/** Generic definition of a Monad used as a template for creating implicit and explicit data
  * transformations
  *
  * @tparam M Type of the data transformation or container
  * @see Scala for Machine Learning Chapter 1 Getting started / Monads and higher kinds
  * @author Patrick Nicolas
  * @author Yuriy Stul.
  */
trait _Monad[M[_]] {
  def unit[T](t: T): M[T]

  def map[T, U](m: M[T])(f: T => U): M[U]

  def flatMap[T, U](m: M[T])(f: T => M[U]): M[U]
}