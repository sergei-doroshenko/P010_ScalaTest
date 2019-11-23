/*val summing: (Int, Int) => Int =
  new Function2[Int, Int, Int] {
    override def apply(a: Int, b: Int): Int = a + b
  }*/

// assign function to a value
val summing: (Int, Int) => Int =
  (a: Int, b: Int) => a + b

List(1, 2, 3, 4, 5)
  .map(x => x + 1)
  .map(_ + 1)
  .reduce(summing)

List(1, 2, 3, 4, 5).reduce(_ + _)

//type FUN1 = Function1[Int, Int]
type FUN1 = (Int) => Int
val funSum: (Int => Int, Int => Int) => Int => Int =
  (a: FUN1, b: FUN1) => a.andThen(b)

val funList = List[Int => Int] (_ + 2, _ * 3, -_)
// apply all functions in the list one by one
// 2 + 2 = 4 * 3 = 12 apply `-` = -12
funList.reduce(funSum)(2)
// 2 -> -2 * 3 = -6 + 2 = -4
funList.reduce(_.compose(_))(2)
