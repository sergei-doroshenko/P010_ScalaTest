def bubbleSort(list: List[Int]): List[Int] = {
  def sortAux(inList: List[Int], outList: List[Int], sorted: Boolean): List[Int] =
    inList match {
      case Nil => if (sorted) outList else
        sortAux(outList.reverse, Nil, sorted = true)
      case head :: Nil => sortAux(Nil, head :: outList, sorted)
      case head :: next :: tail if (head <= next) => sortAux(next :: tail, head :: outList, sorted)
      case head :: next :: tail if (head > next) => sortAux(head :: tail, next :: outList, sorted = false)
    }

  sortAux(list, outList = Nil, sorted = true).reverse
}

bubbleSort(List(5, 0, 7, -2)).mkString(",")