package finalTask.dao

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class StudentRepository {

  val db = Database.forConfig("h2mem1")

  def init(): Future[Unit] = db.run(Query.createSchema)

  def findStudentById(id: Int): Future[Option[Student]] =
    db.run(Query.studentById(id).result.headOption)

  def insertStudent(student: Student): Future[Student] =
    db.run(Query.writeStudents += student)

  def deleteStudentById(id: Int): Future[Int] =
    db.run(Query.deleteStudentById(id))

  def shutDown: Unit = db.close()

  object Query {
    val students = TableQuery[Students]

    val createSchema = students.schema.create

    val studentById = students.findBy(_.id)

    // Return the student with it's auto incremented id instead of an insert count
    val writeStudents = students returning students
      .map(_.id) into((student, id) => student.copy(Option.apply(id)))

    def deleteStudentById(id: Int) = students.filter(_.id === id).delete
  }
}

object StudentRepository {
  def apply(): StudentRepository = {
    val repository = new StudentRepository()
    Await.result(repository.init(), Duration.Inf)
    repository
  }
}
