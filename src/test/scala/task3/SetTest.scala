package task3

import org.scalatest.{FunSuite, Matchers}

class SetTest extends FunSuite with Matchers {

  // Empty test cases - Returns true if the set contains no elements
  test("sets with no elements are empty") {
    val set = Set.empty
    Set.isEmpty(set) should be(true)
  }

  test("sets with elements are not empty") {
    val set = Set.singleton(Weekday.Monday)
    Set.isEmpty(set) should be(false)
  }

  // Contains test cases - Sets can report if they contain an element
  test("nothing is contained in an empty set") {
    val set = Set.empty
    Set.contains(Weekday.Monday, set) should be(false)
  }

  test("when the element is in the set") {
    val set = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    Set.contains(Weekday.Monday, set) should be(true)
  }

  test("when the element is not in the set") {
    val set = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    Set.contains(Weekday.Thursday, set) should be(false)
  }

  // Subset test cases - A set is a subset if all of its elements are contained in the other set
  test("empty set is a subset of another empty set") {
    val left = Set.empty
    val right = Set.empty
    Set.isSubsetOf(left, right) should be(true)
  }

  test("empty set is a subset of non-empty set") {
    val left = Set.empty
    val right = Set.singleton(Weekday.Monday)
    Set.isSubsetOf(left, right) should be(true)
  }

  test("non-empty set is not a subset of empty set") {
    val left = Set.singleton(Weekday.Monday)
    val right = Set.empty
    Set.isSubsetOf(left, right) should be(false)
  }

  test("set is a subset of set with exact same elements") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val right = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    Set.isSubsetOf(left, right) should be(true)
  }

  test("set is a subset of larger set with same elements") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val right = Set.fromList(List(Weekday.Thursday, Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    Set.isSubsetOf(left, right) should be(true)
  }

  test("set is not a subset of set that does not contain its elements") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val right = Set.fromList(List(Weekday.Thursday, Weekday.Monday, Weekday.Wednesday))
    Set.isSubsetOf(left, right) should be(false)
  }

  // Disjoint test cases - Sets are disjoint if they share no elements
  test("the empty set is disjoint with itself") {
    val left = Set.empty
    val right = Set.empty
    Set.isDisjointFrom(left, right) should be(true)
  }

  test("empty set is disjoint with non-empty set") {
    val left = Set.empty
    val right = Set.singleton(Weekday.Monday)
    Set.isDisjointFrom(left, right) should be(true)
  }

  test("non-empty set is disjoint with empty set") {
    val left = Set.singleton(Weekday.Monday)
    val right = Set.empty
    Set.isDisjointFrom(left, right) should be(true)
  }

  test("sets are not disjoint if they share an element") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday))
    val right = Set.fromList(List(Weekday.Tuesday, Weekday.Wednesday))
    Set.isDisjointFrom(left, right) should be(false)
  }

  test("sets are disjoint if they share no elements") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday))
    val right = Set.fromList(List(Weekday.Wednesday, Weekday.Thursday))
    Set.isDisjointFrom(left, right) should be(true)
  }

  // Equal test cases - Sets with the same elements are equal
  test("empty sets are equal") {
    val left = Set.empty
    val right = Set.empty
    Set.isEqual(left, right) should be(true)
  }

  test("empty set is not equal to non-empty set") {
    val left = Set.empty
    val right = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    Set.isEqual(left, right) should be(false)
  }

  test("non-empty set is not equal to empty set") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val right = Set.empty
    Set.isEqual(left, right) should be(false)
  }

  test("sets with the same elements are equal") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday))
    val right = Set.fromList(List(Weekday.Tuesday, Weekday.Monday))
    Set.isEqual(left, right) should be(true)
  }

  test("sets with different elements are not equal") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val right = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Thursday))
    Set.isEqual(left, right) should be(false)
  }

  test("set is not equal to larger set with same elements") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val right = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday, Weekday.Thursday))
    Set.isEqual(left, right) should be(false)
  }

  // Add test cases - Unique elements can be added to a set
  test("add to empty set") {
    val set = Set.empty
    val expected = Set.singleton(Weekday.Wednesday)
    Set.isEqual(Set.insert(Weekday.Wednesday, set), expected) should be(true)
  }

  test("add to non-empty set") {
    val set = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Thursday))
    val expected = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday, Weekday.Thursday))
    Set.isEqual(Set.insert(Weekday.Wednesday, set), expected) should be(true)
  }

  test("adding an existing element does not change the set") {
    val set = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val expected = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    Set.isEqual(Set.insert(Weekday.Wednesday, set), expected) should be(true)
  }

  // Remove test cases - Elements can be removed from a set
  test("remove from empty set") {
    val set = Set.singleton(Weekday.Wednesday)
    val expected = Set.empty
    Set.isEqual(Set.remove(Weekday.Wednesday, set), expected) should be(true)
  }

  test("remove from non-empty set") {
    val set = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday, Weekday.Thursday))
    val expected = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Thursday))
    Set.isEqual(Set.remove(Weekday.Wednesday, set), expected) should be(true)
  }

  test("removing a non-existing element does not change the set") {
    val set = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val expected = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    Set.isEqual(Set.insert(Weekday.Wednesday, set), expected) should be(true)
  }

  // Intersection test cases - Intersection returns a set of all shared elements
  test("intersection of two empty sets is an empty set") {
    val left = Set.empty
    val right = Set.empty
    val expected = Set.empty
    Set.isEqual(Set.intersection(left, right), expected) should be(true)
  }

  test("intersection of an empty set and non-empty set is an empty set") {
    val left = Set.empty
    val right = Set.fromList(List(Weekday.Wednesday, Weekday.Tuesday, Weekday.Friday))
    val expected = Set.empty
    Set.isEqual(Set.intersection(left, right), expected) should be(true)
  }

  test("intersection of a non-empty set and an empty set is an empty set") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday, Weekday.Thursday))
    val right = Set.empty
    val expected = Set.empty
    Set.isEqual(Set.intersection(left, right), expected) should be(true)
  }

  test("intersection of two sets with no shared elements is an empty set") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday))
    val right = Set.fromList(List(Weekday.Thursday, Weekday.Friday, Weekday.Saturday))
    val expected = Set.empty
    Set.isEqual(Set.intersection(left, right), expected) should be(true)
  }

  test(
    "intersection of two sets with shared elements is a set of the shared elements") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday, Weekday.Thursday))
    val right = Set.fromList(List(Weekday.Wednesday, Weekday.Tuesday, Weekday.Friday))
    val expected = Set.fromList(List(Weekday.Tuesday, Weekday.Wednesday))
    Set.isEqual(Set.intersection(left, right), expected) should be(true)
  }

  // Difference test cases - Difference (or Complement) of a set is a set of all elements that are only in the first set
  test("difference of two empty sets is an empty set") {
    val left = Set.empty
    val right = Set.empty
    val expected = Set.empty
    Set.isEqual(Set.difference(left, right), expected) should be(true)
  }

  test("difference of empty set and non-empty set is an empty set") {
    val left = Set.empty
    val right = Set.fromList(List(Weekday.Wednesday, Weekday.Tuesday, Weekday.Friday))
    val expected = Set.empty
    Set.isEqual(Set.difference(left, right), expected) should be(true)
  }

  test("difference of a non-empty set and an empty set is the non-empty set") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday, Weekday.Thursday))
    val right = Set.empty
    val expected = Set.fromList(List(Weekday.Monday, Weekday.Tuesday, Weekday.Wednesday, Weekday.Thursday))
    Set.isEqual(Set.difference(left, right), expected) should be(true)
  }

  test(
    "difference of two non-empty sets is a set of elements that are only in the first set") {
    val left = Set.fromList(List(Weekday.Wednesday, Weekday.Tuesday, Weekday.Monday))
    val right = Set.fromList(List(Weekday.Tuesday, Weekday.Thursday))
    val expected = Set.fromList(List(Weekday.Monday, Weekday.Wednesday))
    Set.isEqual(Set.difference(left, right), expected) should be(true)
  }

  // Union test cases - Union returns a set of all elements in either set
  test("union of empty sets is an empty set") {
    val left = Set.empty
    val right = Set.empty
    val expected = Set.empty
    Set.isEqual(Set.union(left, right), expected) should be(true)
  }

  test("union of an empty set and non-empty set is the non-empty set") {
    val left = Set.empty
    val right = Set.singleton(Weekday.Tuesday)
    val expected = Set.singleton(Weekday.Tuesday)
    Set.isEqual(Set.union(left, right), expected) should be(true)
  }

  test("union of a non-empty set and empty set is the non-empty set") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Wednesday))
    val right = Set.empty
    val expected = Set.fromList(List(Weekday.Monday, Weekday.Wednesday))
    Set.isEqual(Set.union(left, right), expected) should be(true)
  }

  test("union of non-empty sets contains all unique elements") {
    val left = Set.fromList(List(Weekday.Monday, Weekday.Wednesday))
    val right = Set.fromList(List(Weekday.Tuesday, Weekday.Wednesday))
    val expected = Set.fromList(List(Weekday.Wednesday, Weekday.Tuesday, Weekday.Monday))
    Set.isEqual(Set.union(left, right), expected) should be(true)
  }
}
