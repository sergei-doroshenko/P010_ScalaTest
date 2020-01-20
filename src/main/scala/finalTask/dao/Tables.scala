package finalTask.dao

import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

case class Student(id: Option[Int] = None, name: String)

class Students(tag: Tag) extends Table[Student](tag, "students") {

  // This is the primary key column:
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("name")

  // Every table needs a * projection with the same type as the table's type parameter
  override def * : ProvenShape[Student] = (id.?, name) <> (Student.tupled, Student.unapply)
}

case class Teacher(id: Option[Int] = None, name: String)

class Teachers(tag: Tag) extends Table[Teacher](tag, "teachers") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("name")
  override def * : ProvenShape[Teacher] = (id.?, name) <> (Teacher.tupled, Teacher.unapply)
}

case class Course(id: Option[Int] = None, name: String)

class Courses(tag: Tag) extends Table[Course](tag, "courses") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("name")
  override def * : ProvenShape[Course] = (id.?, name) <> (Course.tupled, Course.unapply)
}

