package finalTask.dao

import slick.lifted

import scala.concurrent.Future

class TeacherRepository(dbComponent: DBComponent) {

  import dbComponent.driver.api._

  private val db = dbComponent.db

  def init(): Future[Unit] = db.run(Query.createSchema)

  private object Query {
    val teachers = lifted.TableQuery[Teachers]

    val createSchema = teachers.schema.create

    val teacherById = teachers.findBy(_.id)
  }

}

object TeacherRepository {
  def apply(dbComponent: DBComponent): TeacherRepository = {
    val repository = new TeacherRepository(dbComponent)
    repository.init()
    repository
  }
}
