package samples

/**
 * TopRegexp defined in package object.
 */
object Imports extends App {
  println(TopRegexp.hexColor.regex)

  import TopRegexp._

  println(userName.regex)
}
