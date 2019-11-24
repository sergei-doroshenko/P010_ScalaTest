import scala.annotation.tailrec

val aList = List(1, 2, 3)
val sameList1 = 1 :: 2 :: 3 :: Nil
sameList1.head
sameList1.tail
val sameList2 = Nil.::(4).::(3).::(2).::(1)
sameList2.head
sameList2.tail

def sum(list: List[Int]): Int = {
  @tailrec
  def sumAux(list: List[Int], acc: Int): Int = {
    list match {
      case Nil => acc
      case head :: tail => {
        sumAux(tail, acc + head)
      }
      //      case list.head :: list.tail => sumAux(list.tail, acc + list.head)
      //      case List(list.head, list.tail) => sumAux(list.tail, acc + list.head)
    }
  }

  sumAux(list, 0)
}

sum(List(1, 2, 3, 4, 5))

val lol = List(List(1,2), List(3,4))
val result = lol.flatten