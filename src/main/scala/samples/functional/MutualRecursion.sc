import scala.util.control.TailCalls.{done, tailcall}
import scala.util.control.TailCalls.TailRec

object evenOdd {
  def even(i: Int): Boolean = i match {
    case 0 => true
    case _ => odd(i - 1)
  }

  def odd(i: Int): Boolean = i match {
    case 0 => false
    case _ => even(i - 1)
  }
}

List(1, 2).map(evenOdd.even)
List(1, 2).map(evenOdd.odd)

object evenOddTR {
  def even(i: Int): TailRec[Boolean] = i match {
    case 0 => done(true)
    case _ => tailcall(odd(i - 1))
  }

  def odd(i: Int): TailRec[Boolean] = i match {
    case 0 => done(false)
    case _ => tailcall(even(i - 1))
  }
}

List(1, 2).map(evenOddTR.even).map(_.result)
List(1, 2).map(evenOddTR.odd).map(_.result)