package finalTask.dao

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class CourseRepository(val dbComponent: DBComponent, val tables: Tables) {

  import dbComponent.driver.api._
  import tables.{courses, teachers}

  private val db = dbComponent.db

  def init(): Future[Unit] = db.run(Query.createSchema)
  def shutDown: Unit = db.close()

  def insertCourse(course: Course): Future[Course] = db.run(Query.writeCourse += course)

  def findCourseWithTeacher(id: Int) = db.run(Query.courseWithTeacher(id).result)

  def findAllCoursesWithTeachers = db.run(Query.coursesWithTeachers.result)

  private object Query {

    val createSchema = courses.schema.create

    val courseById = courses.findBy(_.id)

    val writeCourse = courses returning courses
      .map(_.id) into ((course, id) => course.copy(Option.apply(id)))

    def courseWithTeacher(id: Int) = for {
      (course, teacher) <- courses join teachers on (_.teacherId === _.id) if course.id === id
    } yield (course, teacher)

    val coursesWithTeachers = for {
      (course, teacher) <- courses join teachers on (_.teacherId === _.id)
    } yield (course, teacher)
  }

}

object CourseRepository {
  def apply(dbComponent: DBComponent, tables: Tables): CourseRepository = {
    val repository = new CourseRepository(dbComponent, tables)
    Await.result(repository.init(), Duration.Inf)
    repository
  }
}

