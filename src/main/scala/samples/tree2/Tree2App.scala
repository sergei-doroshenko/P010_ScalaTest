package samples.tree2

import scala.annotation.tailrec

sealed trait Tree2[+A]

case class Leaf2[A](value: A) extends Tree2[A]

case class Branch[A](left: Tree2[A], right: Tree2[A]) extends Tree2[A]

private object Tree2 {

  // @tailrec /* This is not tail-recursive!!... */
  def size_bad(t: Tree2[Int]): Int =
    t match {
      case Leaf2(a) => 1
      case Branch(a, b) => size_bad(a) + size_bad(b) + 1
    }

  def size(t: Tree2[Int]): Int = {
    @tailrec
    def inner_size(l: List[Tree2[Int]], acc: Int): Int =
      l match {
        case Nil => acc
        case Leaf2(v) :: ls => inner_size(ls, acc + 1)
        case Branch(a, b) :: ls => inner_size(a :: b :: ls, acc + 1)
      }

    inner_size(List(t), 0)
  }
}

object Tree2App extends App {
  val example =
    Branch(
      Branch(
        Branch(
          Leaf2(1),
          Leaf2(3)),
        Branch(
          Branch(
            Leaf2(4),
            Leaf2(5)),
          Leaf2(7))),
      Branch(
        Branch(
          Branch(
            Leaf2(9),
            Branch(
              Leaf2(10),
              Leaf2(3))),
          Leaf2(0)),
        Leaf2(2)))

  println("size example")
  println(example)
  println("sum (baad):" + Tree2.size_bad(example))
  println("sum (good):" + Tree2.size(example))
}
