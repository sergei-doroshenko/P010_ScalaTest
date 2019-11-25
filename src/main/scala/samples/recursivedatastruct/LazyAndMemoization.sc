val fibStream: LazyList[Int] = {
  def fibTail: LazyList[Int] = (fibStream zip fibStream.tail).map(n => n._1 + n._2)

  0 #:: 1 #:: fibTail
}

fibStream.slice(0, 10).mkString(",")

val facStream: LazyList[Long] =
  1L #:: 1L #:: facStream.zipWithIndex.tail.map {
    case (fn, n) => fn * (n + 1)
  }


facStream.slice(0, 10).mkString(",")