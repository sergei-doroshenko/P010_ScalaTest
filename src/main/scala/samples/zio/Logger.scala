package samples.zio

import java.io._

import zio.{DefaultRuntime, Task, ZIO}

class Logger0 {
  /**
   * write a `String` to the `filename`.
   */
  def writeFile(filename: String, s: String): Unit = {
    val file = new File(filename)

    try {
      val fw = new FileWriter(file, true)
      val bw = new BufferedWriter(fw)
      val out = new PrintWriter(bw)
      try {
        out.println(s)
        out.flush()
      } catch {
        case e: IOException => println(e)
        //exception handling left as an exercise for the reader
      }
    }
  }

  def log(filename: String, s: String): Task[Unit] =
    ZIO.effect(writeFile(filename, s))
}

object Logger0 {
  def apply(): Logger0 = {
    new Logger0
  }
}

object LoggerLauncher extends App {
  val runtime = new DefaultRuntime {}
  val logger = new Logger0
  val task = logger.log("test0.txt", "Test004")
  runtime.unsafeRun(task)
}
