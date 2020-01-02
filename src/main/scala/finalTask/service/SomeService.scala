package finalTask.service

import finalTask.dao.{Student, StudentsDbio}
import scala.concurrent.Future

class SomeService(studentsDbio: StudentsDbio) {

  def findStudentById(id: Int): Future[Option[Student]] =
    studentsDbio.findStudentById(id)

  def saveStudent(student: Student): Future[Student] =
    studentsDbio.insertStudent(student)

  def shutDown: Unit = studentsDbio.shutDown
}

object SomeService {
  def apply(studentsDbio: StudentsDbio): SomeService = {
    new SomeService(studentsDbio)
  }
}
