package task1

import scala.util.Random

/* Implement a simple "guess a number" game. A user will be guessing a number generated by computer.
  - The game should be interactive
  - Use console input\output: Console.in.read/readline, print/println
  - Use string interpolations like s"Hello ${subject}"
  - Use Random.between() to create the number to guess
  - You could define any hierarchy of classes\traits in order to express your solution idea
  - The code below is to provide a place to start from. You cold rewrite it from scratch if you like.
  - Be as creative as possible. Use as many Scala features you know as you can.
  - How much you program is mutable\immutable is completely up to you
  - Implement one of the features below or all of them (Optional)
      - a mode when the computer will be guessing your number;
      - a scoring system (like every guess would subtract 10 points from your initial budget);
      - a mode when a human will be playing with computer switching sides until somebody of the two
            will run out of initial budget and be declared a looser.
   */

sealed trait Command

case object QuitRequest extends Command

case object Range extends Command

case class GreaterThan(x: Int) extends Command

case class LessThan(x: Int) extends Command

case class Equals(x: Int) extends Command


class Game(val numberToGuess: Int) {
  def produceResponse(cmd: Command): String = {
    cmd match {
      case Range =>
        val a = 0
        val b = 100
        s"The range is [$a to $b]"
      // case ... Implement more logic here
      case GreaterThan(number) =>
        if (numberToGuess > number) s"Yes, then guessed number is greater then $number."
        else "No"
      case LessThan(number) if numberToGuess < number => s"Yes, then guessed number is less then $number."
      case Equals(number) if numberToGuess == number => s"Hey! You guess the number! It's $number."
      case _ => "No"
    }
  }
}

object GuessNumberGame extends App {

  def parseInput(userInput: String): Option[Command] = {

    def parseNumber(x: String, sign: Char): Int = {
      Integer.valueOf(x.substring(x.lastIndexOf(sign) + 1).trim)
    }

    userInput match {
      case "q!" => Some(QuitRequest)
      case "range" => Some(Range)
      case x if x.startsWith("? >") => Some(GreaterThan(parseNumber(x, '>')))
      case x if x.startsWith("? <") => Some(LessThan(parseNumber(x, '<')))
      case x if x.startsWith("? ==") => Some(Equals(parseNumber(x, '=')))
      case _ => None
    }
  }

  println("Guess a number I've just generated with minimal attempts")
  println(
    """Commands:
      | - range - prints the range in which the number lies
      | - ? > <X> - prints whether the number is greater than <X>
      | - ? < <X> - prints whether the number is less than <X>
      | - ? == <X> - prints whether the number is equal to <X>
      | - q! - quit
      |""".stripMargin)
  val random = Random.between(0, 100);
  println(random)
  val aGame = new Game(random)
  var command: Option[Command] = None
  do {
    print(":>")
    val userInput = Console.in.readLine().trim
    command = parseInput(userInput)
    command.filter(_ != QuitRequest).foreach { cmd =>
      val computerResponse = aGame.produceResponse(cmd)
      println(computerResponse)
    } // feel free to change everything you want
  } while (!command.contains(QuitRequest))
  println("bye!")
}
