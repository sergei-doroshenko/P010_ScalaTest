package finalTask.dao

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class CourseRepository(val dbComponent: DBComponent, val tables: Tables) {

  import dbComponent.driver.api._
  import tables.courses

  private val db = dbComponent.db

  def init(): Future[Unit] = db.run(Query.createSchema)
  def shutDown: Unit = db.close()

  private object Query {

    val createSchema = courses.schema.create

    val teacherById = courses.findBy(_.id)
  }

}

object CourseRepository {
  def apply(dbComponent: DBComponent, tables: Tables): CourseRepository = {
    val repository = new CourseRepository(dbComponent, tables)
    Await.result(repository.init(), Duration.Inf)
    repository
  }
}

