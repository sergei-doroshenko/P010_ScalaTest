package finalTask.dao

import slick.lifted.ProvenShape

case class Student(id: Option[Int] = None, name: String)

case class Teacher(id: Option[Int] = None, name: String)

case class Course(id: Option[Int] = None, name: String, teacherId: Int)

class Tables(val dbComponent: DBComponent) {

  import dbComponent.driver.api._

  class Students(tag: Tag) extends Table[Student](tag, "students") {

    // This is the primary key column:
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] = column[String]("name")

    // Every table needs a * projection with the same type as the table's type parameter
    override def * : ProvenShape[Student] = (id.?, name) <> (Student.tupled, Student.unapply)
  }

  val students = TableQuery[Students]

  class Teachers(tag: Tag) extends Table[Teacher](tag, "teachers") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] = column[String]("name")

    override def * : ProvenShape[Teacher] = (id.?, name) <> (Teacher.tupled, Teacher.unapply)
  }

  val teachers = TableQuery[Teachers]

  class Courses(tag: Tag) extends Table[Course](tag, "courses") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] = column[String]("name")

    def teacherId = column[Int]("teacher_id")

    override def * : ProvenShape[Course] = (id.?, name, teacherId) <> (Course.tupled, Course.unapply)

    def teacher = foreignKey("teachers_fk", teacherId, teachers)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  }

  val courses = TableQuery[Courses]

  class StudentCourses(tag: Tag) extends Table[(Int, Int, Option[String])](tag, "student_courses") {
    def studentId = column[Int]("student_id")
    def courseId = column[Int]("course_id")
    def feedback = column[Option[String]]("feedback")
    override def * = (studentId, courseId, feedback)
    def pk = primaryKey("pk_student_courses", (studentId, courseId))
  }

  val studentCourses = TableQuery[StudentCourses]
}



