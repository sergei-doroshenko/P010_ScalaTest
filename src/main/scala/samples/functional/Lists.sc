val aList = List(1, 2, 3)
val sameList1 = 1 :: 2 :: 3 :: Nil
sameList1.head
sameList1.tail
val sameList2 = Nil.::(4).::(3).::(2).::(1)
sameList2.head
sameList2.tail

def sum(list: List[Int]): Int = {
  def sumAux(list: List[Int], acc: Int): Int = {
    list match {
      case Nil => acc
      case head :: tail => {
        println("H:"+head)
        println("T:"+tail)
        return sumAux(tail, acc + head)
      }
      //      case list.head :: list.tail => sumAux(list.tail, acc + list.head)
      //      case List(list.head, list.tail) => sumAux(list.tail, acc + list.head)
    }
  }

  sumAux(list, 0)
}

sum(List(1, 2, 3, 4, 5))