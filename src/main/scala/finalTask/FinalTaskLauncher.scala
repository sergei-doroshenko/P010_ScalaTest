package finalTask

import scala.concurrent.{Await, Promise}
import finalTask.dao.{Student, StudentsDbio}
import finalTask.service.SomeService

import scala.concurrent.duration.Duration

object FinalTaskLauncher extends App {
  val studentsDbio = StudentsDbio()
  val someService = SomeService(studentsDbio)

  try {
    val s0 = Await.result(someService.saveStudent(Student(Option.empty, "Karl")), Duration.Inf)
    println(s0)
    val s1 = Await.result(someService.saveStudent(Student(Option.empty, "Eva")), Duration.Inf)
    println(s1)
    val s2 = someService.findStudentById(1).get
    println(s2)
  } finally someService.shutDown


}
