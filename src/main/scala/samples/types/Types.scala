package samples.types

object Types extends App {

  def getValue(value: IntsAndDoubles): Unit = {
    value match {
      case Ints(value) => println(s"I've got an int: ${value}")
      case Doubles(value) => println(s"I've got a double: ${value}")
      case _ => println("Error")
    }
  }

  getValue(Ints(100))
  getValue(Doubles(345.333))

  type IntOrNothing = Option[Int]
  type IntOrString = Either[String, Int]

}

/**
 * Algebraic sum of two sets: integers and doubles.
 */
sealed trait IntsAndDoubles

case class Ints(value: Int) extends IntsAndDoubles

case class Doubles(value: Double) extends IntsAndDoubles
