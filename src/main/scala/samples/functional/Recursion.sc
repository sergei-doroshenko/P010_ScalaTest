// found n-th fibonacci number
def fibRec(n: Int): Int = n match {
  case 0 => 0
  case 1 => 1
  case _ => fibRec(n - 1) + fibRec(n - 2)
}

fibRec(10)
(1 to 7).toList.map(fibRec)

def factorial(n: Long): Long =
  if (n == 0) 1 else
    n * factorial(n - 1)

factorial(5)
(1L to 7L).toList.map(factorial)