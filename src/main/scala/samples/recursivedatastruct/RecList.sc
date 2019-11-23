sealed trait RecList

case object RecNil extends RecList

case class Cons(head: Int, tail: RecList) extends RecList

def map(list: RecList)(func: Int => Int): RecList = {
  list match {
    case RecNil => list
    case Cons(head, tail) => Cons(func(head), map(tail)(func))
  }
}

val list = Cons(1, Cons(2, Cons(3, RecNil)))
map(list)(_ + 1)
map(list)(x => x * 10)