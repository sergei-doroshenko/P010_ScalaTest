package finalTask.dao

import scala.concurrent.Future

class TeacherRepository(val dbComponent: DBComponent, val tables: Tables) {

  import dbComponent.driver.api._
  import tables.teachers

  private val db = dbComponent.db

  def init(): Future[Unit] = db.run(Query.createSchema)

  def shutDown: Unit = db.close()

  def findAll = db.run(Query.all)

  def insertTeacher(teacher: Teacher): Future[Teacher] = db.run(Query.writeTeacher += teacher)

  def deleteTeacher(id: Int) = db.run(Query.deleteTeacherById(id))

  private object Query {

    val all = teachers.result

    val createSchema = teachers.schema.create

    val teacherById = teachers.findBy(_.id)

    val writeTeacher = teachers returning teachers
      .map(_.id) into ((teacher, id) => teacher.copy(Option.apply(id)))

    def deleteTeacherById(id: Int) = teachers.filter(_.id === id).delete
  }

}

object TeacherRepository {
  def apply(dbComponent: DBComponent, tables: Tables): TeacherRepository = {
    val repository = new TeacherRepository(dbComponent, tables)
    repository.init()
    repository
  }
}
