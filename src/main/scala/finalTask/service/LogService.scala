package finalTask.service

import java.io.{BufferedWriter, File, FileWriter, IOException, PrintWriter}

import zio.{DefaultRuntime, Runtime, ZIO}

class LogService(val filename: String, val runtime: Runtime[_]) {

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
        //TODO: exception handling
      }
    }
  }

  def info(msg: String): Unit = {
    val task = ZIO.effect(writeToFile(s"INFO: $msg"))
    runtime.unsafeRun(task)
  }
}

object LogService {
  def apply(): LogService = new LogService("app.log", new DefaultRuntime {})
  def apply(filename: String, runtime: Runtime[_]): LogService = new LogService(filename, runtime)
}