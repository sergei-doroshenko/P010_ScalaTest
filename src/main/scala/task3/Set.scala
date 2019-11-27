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
  //              return type
  val singleton2: (Weekday.Value => Set) = {
    // closure - return
    (day: Weekday.Value) => (x: Weekday.Value) => (x == day)
  }
  val fromList: List[Weekday.Value] => Set = _.foldRight(empty) { case (day, set) =>
    if (contains(day, set)) set else insert(day, set)
  }

  def union(left: Set, right: Set): Set = ???

  def difference(left: Set, right: Set): Set = ???

  def intersection(left: Set, right: Set): Set = ???

  def insert(day: Weekday.Value, set: Set): Set = set match {
    case set if Set.contains(day, set) => set
    case _ => x =>
      if (x == day) true
      else set.apply(x)
  }

  /**
   * Full syntax.
   * @param day a Weekday
   * @param set a function (Set, parent function)
   * @return a function that return true for a 'day' and for other days for which 'set' returns true
   */
  def insert1(day: Weekday.Value, set: Set): Set = set match {
    case set if Set.contains(day, set) => set
    case _ => (x: Weekday.Value) => {
      if (x == day) true
      else set.apply(x)
    }
  }

  def remove(day: Weekday.Value, set: Set): Set = set match {
    case set if !Set.contains(day, set) => set
    case _ => x =>
      if (x == day) false
      else set(x)
  }

  def contains(day: Weekday.Value, set: Set): Boolean = set match {
    case Set.empty => false
    case _ => set(day)
  }

  def isEmpty(set: Set): Boolean =
    set match {
      case Set.empty => true
      case _ => false
    }

  def isEqual(left: Set, right: Set): Boolean = ???

  def isSubsetOf(left: Set, right: Set): Boolean = ???

  def isDisjointFrom(left: Set, right: Set): Boolean = ???

}
