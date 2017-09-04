package com.stulsoft.scala.machine.learning.core

import com.stulsoft.scala.machine.learning.core.functional._Monad
import scala.language.higherKinds
import scala.util.Try

/**
  * Data transformation using an explicit model or configuration parameter
  * {{{
  *   |>[U,V]     u: U -> | _config: T | -> v: V
  * }}}
  *
  * @tparam T type parameter of the model or configuration parameter
  * @param config configuration or model used to implement the }> (partial function) operator
  * @note This data transformation is different from ''ITransform'' class which relies on an
  *       implicit model generated from a input data set (i.e. supervised learning algorithm
  * @see Scala for Machine learning Chapter 2 "Hello World!" Designing a workflow /
  *      Monadic transformation for explicit models
  * @author Patrick Nicolas
  * @author Yuriy Stul.
  */
private[learning] abstract class ETransform[T](val config: T) {

  /**
    * Input type for the data transformation using config
    */
  type U
  /**
    * Output type for the data transformation using config
    */
  type V

  /**
    * Declaration of the  data transformation on an explicit model
    *
    * @return A partial function that implement the data transformation U => Try[V]
    */
  def |> : PartialFunction[U, Try[V]]
}


/**
  * Companion object to the ETransform class that define the zero value
  *
  * @author Patrick Nicolas
  * @since May 06, 2015
  * @version 0.99.1.1
  */
object ETransform {
  /**
    * Definition of the zero value (partial function) for the explicit transformation E
    *
    * @tparam U type of input to the partial function (data transformation)
    * @tparam V type of output from the partial function (data transformation)
    */
  def zero[U, V]: PartialFunction[U, Try[V]] = {
    case _ => null.asInstanceOf[Try[V]]
  }
}


/**
  * Singleton that encapsulates the monadic implementation of the explicit transformation
  */
object ETransformMonad {

  protected def eTransform[T](config: T): ETransform[T] = new ETransform[T](config) {
    override def |> : PartialFunction[U, Try[V]] = {
      case _ => println("error")
        null.asInstanceOf[Try[V]]
    }
  }

  private val eTransformMonad = new _Monad[ETransform] {
    override def unit[T](t: T): ETransform[T] = eTransform(t)

    override def map[T, U](m: ETransform[T])(f: T => U): ETransform[U] = eTransform(f(m.config))

    override def flatMap[T, U](m: ETransform[T])(f: T => ETransform[U]): ETransform[U] = f(m.config)
  }

  /**
    * Implicit class conversion from explicit transformation, ETransform, into its Monad.
    * {{{
    *  Use the monadic template
    *    trait _Monad[M[_]] {
    *      def unit[T](t: T): M[T]
    *      def map[T, U](m: M[T])(f: T => U): M[U]
    *      def flatMap[T, U](m: M[T])(f: T => M[U]): M[U]
    *   }
    * }}}
    *
    * @tparam T type parameter for the explicit data transform
    * @param fct explicit transformation of type '''ETransform''' contained and managed by the monadic wrapper
    * @author Patrick Nicolas
    * @since 0.99
    * @version 0.99.1.1
    * @see Scala for Machine Learning Chapter 2 Hello World!/Designing a workflow /
    *      Monadic data transformation
    */
  implicit class eTransform2Monad[T](fct: ETransform[T]) {

    /**
      * Access the element of type T contained in this instance
      *
      * @return element of type T
      */
    def unit(t: T): ETransform[T] = eTransformMonad.unit(t)

    /**
      * Implementation of the map method
      *
      * @tparam U type of output of morphism
      * @param  f function that converts from type T to type U
      */
    def map[U](f: T => U): ETransform[U] = eTransformMonad.map(fct)(f)

    /**
      * Implementation of flatMap
      *
      * @tparam U type of explicit transformation generated by morphism
      * @param f function that converts from type T to a monadic container of type U
      */
    def flatMap[U](f: T => ETransform[U]): ETransform[U] = eTransformMonad.flatMap(fct)(f)
  }
}