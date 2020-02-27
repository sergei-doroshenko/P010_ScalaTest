package finalTask.service

import java.io.{BufferedWriter, File, FileWriter, IOException, PrintWriter}

import zio.{DefaultRuntime, ZIO}

class LogService(val filename: String) {

  def writeToFile(s: String): Unit = {
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

  def info(msg: String): Unit = {
    val runtime = new DefaultRuntime {}
    val task = ZIO.effect(writeToFile(msg))
    runtime.unsafeRun(task)
  }
}

object LogService {
  def apply(filename: String): LogService = new LogService(filename)
}
