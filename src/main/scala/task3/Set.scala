package task3

/*
 * Functional Set
 * Implement operations for Set that is defined as a function. You may not use other collections to accumulate state.
 * State is accumulated only by closures.
 *
 * The only things you need to change are those `???` symbols (implementation), the other code remains unchanged.
 *
 * Please, use tests to check you implementation (scalaBasic/lecture3/SetTest.scala).
 */
object Set {
  type Set = Weekday.Value => Boolean

  val empty: Set = _ => false

  val singleton: Weekday.Value => Set = day => x => x == day

  val fromList: List[Weekday.Value] => Set = _.foldRight(empty) { case (day, set) =>
    if (contains(day, set)) set else insert(day, set)
  }

  def union(left: Set, right: Set): Set = ???

  def difference(left: Set, right: Set): Set = ???

  def intersection(left: Set, right: Set): Set = ???

  def insert(day: Weekday.Value, set: Set): Set = set match {
    case set if Set.contains(day, set) => set
    case _ =>  Set.singleton(day).andThen(set)
  }

  def remove(day: Weekday.Value, set: Set): Set = ???

  def contains(day: Weekday.Value, set: Set): Boolean = ???

  def isEmpty(set: Set): Boolean =
    set match {
      case Set.empty => true
      case _ => false
    }

  def isEqual(left: Set, right: Set): Boolean = ???

  def isSubsetOf(left: Set, right: Set): Boolean = ???

  def isDisjointFrom(left: Set, right: Set): Boolean = ???
}
