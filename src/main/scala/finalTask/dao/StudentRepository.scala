package finalTask.dao

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class StudentRepository(dbComponent: DBComponent, tables: Tables) {

  import dbComponent.driver.api._
  import tables.students

  private val db = dbComponent.db

  def init(): Future[Unit] = db.run(Query.createSchema)

  def findAll():Future[Seq[Student]] = db.run(Query.all)

  def findStudentById(id: Int): Future[Option[Student]] =
    db.run(Query.studentById(id).result.headOption)

  def findStudentByName(name: String): Future[Option[Student]] =
    db.run(Query.studentByName(name).result.headOption)

  def insertStudent(student: Student): Future[Student] =
    db.run(Query.writeStudents += student)

  def deleteStudentById(id: Int): Future[Int] =
    db.run(Query.deleteStudentById(id))

  def shutDown: Unit = db.close()

  private object Query {

    val createSchema = students.schema.create

    val all = students.result

    val studentById = students.findBy(_.id)

    def studentByName(name: String) = students.filter(_.name === name)

    // Return the student with it's auto incremented id instead of an insert count
    val writeStudents = students returning students
      .map(_.id) into ((student, id) => student.copy(Option.apply(id)))

    def deleteStudentById(id: Int) = students.filter(_.id === id).delete
  }

}

object StudentRepository {
  def apply(dbComponent: DBComponent, tables: Tables): StudentRepository = {
    val repository = new StudentRepository(dbComponent, tables)
    Await.result(repository.init(), Duration.Inf)
    repository
  }
}
