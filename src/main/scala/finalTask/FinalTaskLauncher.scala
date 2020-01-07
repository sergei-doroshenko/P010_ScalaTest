package finalTask

import scala.concurrent.{Await, Promise}
import finalTask.dao.{Student, StudentRepository}
import finalTask.service.StudentService

import scala.concurrent.duration.Duration

object FinalTaskLauncher extends App {
  val studentRepository = StudentRepository()
  val studentService = StudentService(studentRepository)

  /*try {
    val s0 = Await.result(studentService.saveStudent(Student(Option.empty, "Karl")), Duration.Inf)
    println(s0)
    val s1 = Await.result(studentService.saveStudent(Student(Option.empty, "Eva")), Duration.Inf)
    println(s1)
    val s2 = studentService.findStudentById(1).get
    println(s2)
  } finally studentService.shutDown*/
}
