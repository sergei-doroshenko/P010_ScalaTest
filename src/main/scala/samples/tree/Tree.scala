package samples.tree

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

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
    @tailrec
    def walkAux(tree: Tree, nodes: List[Tree], func: Int => Int): Tree = {
      println(s"Nodes: $nodes")
      if (nodes.isEmpty) {
        tree
      } else {
        var res = new ArrayBuffer[Tree]
        nodes.map(t => t.asInstanceOf[Node]).foreach(n => {
          func.apply(n.value)
          if (n.left != Leaf) res.addOne(n.left)
          if (n.right != Leaf) res.addOne(n.right)
        })

        walkAux(tree, res.toList, func)
      }
    }

    walkAux(tree, List(tree), func)
  }
}

object TreeApp extends App {
  // here goes the test suite :)
  val tree = Node(Node(Node(-1), Node(-3), -2), Node(Node(1), Node(3), 2), 0)
  println(s"Tree: $tree")
  /*val r1 = TreeFuncs.walkMap(tree, _ + 1) == Node(Node(Node(0), Node(-2), -1), Node(Node(2), Node(4), 3), 1) // should be true
  val r2 = TreeFuncs.search(tree, -2) == Some(Node(Node(-1), Node(-3), -2)) // should be true
  val r3 = TreeFuncs.search(tree, -100) == None // should be true
  println(r1)
  println(r2)
  println(r3)*/
  var r4 = TreeFuncs.walkMapOptimized(tree, _ + 1) == Node(Node(Node(0), Node(-2), -1), Node(Node(2), Node(4), 3), 1)
  println(r4)
}

