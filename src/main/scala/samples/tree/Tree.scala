package samples.tree

import scala.annotation.tailrec

/**
 * Implement a couple of useful functions for Binary Tree.
 * All functions must be defined via Recursion.
 **/
sealed trait Tree

case object Leaf extends Tree

case class Node(left: Tree, right: Tree, value: Int) extends Tree

object Node {
  // overloads apply for a special common case
  def apply(value: Int): Node = Node(Leaf, Leaf, value)
}

object TreeFuncs {
  /* (Mandatory, Easy) Define walkMap function (without tail call optimization)
      which returns a new tree with the specific function applied to
      every element of the initial tree
   */
  def walkMap(tree: Tree, func: Int => Int): Tree = tree match {
    case Leaf => tree
    case Node(left, right, value) => Node(walkMap(left, func), walkMap(right, func), func.apply(value))
  }

  /* (Mandatory, Normal) Define tail call optimized search function,
    it returns a sub tree having the node where the element was found as a root
    and None in case the tree does not contain the value
   */
  @tailrec
  def search(tree: Tree, target: Int): Option[Tree] = {
    tree match {
      case Leaf => None
      case Node(_, _, value) if target == value => Option.apply(tree)
      case Node(left, _, value) if target < value => search(left, target)
      case Node(_, right, value) if target > value => search(right, target)
    }
  }

  /*
   (Optional, Very Hard) The same as walkMap but with tail call optimization
   */
  def walkMapOptimized(tree: Tree, func: Int => Int): Tree = {
    fromList(toList(tree), func)
  }

  def toList(tree: Tree): List[Int] = {
    @tailrec
    def toList0(inList: List[Tree], outList: List[Int]): List[Int] =
      inList match {
        case Leaf :: Nil => outList
        case Leaf :: tail => toList0(tail, outList) // skip Leaf not add
        case Node(left, right, value) :: tail => toList0(right :: left :: tail, value :: outList)
      }

    toList0(List(tree), List())
  }

  def fromList(list: List[Int], func: Int => Int): Tree = {
    @tailrec
    def fromList0(in: List[Int], a: List[Tree], b: List[Tree]): Tree =
      in match {
        case head :: Nil if (a.size == 1) => a.head
        case head :: Nil if (b.size == 1) => b.head
        case head :: Nil if (a.size == 2) => Node(a(1), a(0), func.apply(head))
        case head :: Nil if (b.size == 2) => Node(b(1), b(0), func.apply(head))
        case head :: tail if (b.size == 2) =>
          fromList0(tail, b, List())
        case head :: next :: tail if (a.size < 2) =>
          fromList0(in.slice(3, in.size), Node(Node(func.apply(head)), Node(func.apply(next)), func.apply(in(2))) :: a, b)
        case head :: tail if (a.size == 2) =>
          fromList0(tail, List(), Node(a(1), a(0), func.apply(head)) :: b)

      }

    fromList0(list, List(), List())
  }

  def size(t: Tree): Int = {
    @tailrec
    def size0(l: List[Tree], acc: Int): Int =
      l match {
        case Nil => acc
        case Leaf :: ls => size0(ls, acc + 1)
        case Node(a, b, _) :: ls => size0(a :: b :: ls, acc + 1)
      }

    size0(List(t), acc = 0)
  }
}

object TreeApp extends App {
  // here goes the test suite :)
  val tree = Node(Node(Node(-1), Node(-3), -2), Node(Node(1), Node(3), 2), 0)
  println(s"Tree: $tree")
  val r1 = TreeFuncs.walkMap(tree, _ + 1) == Node(Node(Node(0), Node(-2), -1), Node(Node(2), Node(4), 3), 1) // should be true
  val r2 = TreeFuncs.search(tree, -2) == Some(Node(Node(-1), Node(-3), -2)) // should be true
  val r3 = TreeFuncs.search(tree, -100) == None // should be true
  println(r1)
  println(r2)
  println(r3)
  var r4 = TreeFuncs.walkMapOptimized(tree, _ + 1) == Node(Node(Node(0), Node(-2), -1), Node(Node(2), Node(4), 3), 1)
  println(r4)
  private val aList: List[Int] = TreeFuncs.toList(tree)
  println(aList)
  val aTree = TreeFuncs.fromList(aList, x => x)
  println(aTree)
  println(aTree == tree)

  val sample =
    Node(
      Node(
        Node(
          Node(2),
          Node(3),
          4
        ),
        Node(
          Node(6),
          Node(8),
          7
        ),
        5
      ),
      Node(
        Node(
          Node(10),
          Node(12),
          11
        ),
        Node(
          Node(13),
          Node(15),
          14
        ),
        9
      ),
      1
    )
  println(TreeFuncs.toList(sample))
  val size = TreeFuncs.size(tree)
  println(s"Tree size: $size")
  println(size == 15)
  val tree3 = TreeFuncs.fromList(List(1, 2, 3, 5, 7, 6, 4, 9, 11, 10, 12, 14, 13, 8, 0), _ + 1)
  println(tree3)
  println(tree3 == sample)
}

