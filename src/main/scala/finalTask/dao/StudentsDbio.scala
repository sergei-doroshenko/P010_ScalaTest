package finalTask.dao

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class StudentsDbio {

  val db = Database.forConfig("h2mem1")

  def init(): Future[Unit] = db.run(Query.createSchema)

  def findStudentById(id: Int): Future[Option[Student]] =
    db.run(Query.studentById(id).result.headOption)

  def insertStudent(student: Student): Future[Student] =
    db.run(Query.writeStudents += student)

  def shutDown: Unit = db.close()

  object Query {
    val students = TableQuery[Students]

    val createSchema = students.schema.create

    val studentById = students.findBy(_.id)

    // Return the student with it's auto incremented id instead of an insert count
    val writeStudents = students returning students
      .map(_.id) into((student, id) => student.copy(Option.apply(id)))
  }
}

object StudentsDbio {
  def apply(): StudentsDbio = {
    val dbio = new StudentsDbio()
    Await.result(dbio.init(), Duration.Inf)
    dbio
  }
}
