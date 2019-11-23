import scala.annotation.tailrec

def fibTailRec(n: Int): Int = {
  @tailrec
  def fibAux(i: Int, fib: Int, fibNext: Int): Int = {
    if (i == n) fib else
      fibAux(i + 1, fibNext, fib + fibNext)
  }

  fibAux(i = 0, fib = 0, fibNext = 1)
}

(1 to 7).toList.map(fibTailRec)

def factTailRec(n: Long): Long = {
  @tailrec
  def factAux(acc: Long, n: Long): Long =
    if (n == 0) acc else
      factAux(n * acc, n - 1)

  factAux(1, n)
}

(1L to 7L).toList.map(factTailRec)

@tailrec
def factorial(n: Int, accum: BigInt = 1): BigInt = {
  if (n == 0) {
    accum
  } else {
    factorial(n - 1, n * accum)
  }
}
factorial(10)