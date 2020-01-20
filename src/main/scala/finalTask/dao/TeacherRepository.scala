package finalTask.dao

import scala.concurrent.Future

class TeacherRepository(dbComponent: DBComponent, tables: Tables) {

  import dbComponent.driver.api._
  import tables.teachers

  private val db = dbComponent.db

  def init(): Future[Unit] = db.run(Query.createSchema)

  private object Query {

    val createSchema = teachers.schema.create

    val teacherById = teachers.findBy(_.id)
  }

}

object TeacherRepository {
  def apply(dbComponent: DBComponent, tables: Tables): TeacherRepository = {
    val repository = new TeacherRepository(dbComponent, tables)
    repository.init()
    repository
  }
}
