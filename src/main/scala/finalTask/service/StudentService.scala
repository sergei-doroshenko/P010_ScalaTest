package finalTask.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import finalTask.dao.{Student, StudentRepository}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class StudentService(studentRepository: StudentRepository) {

  def shutDown: Unit = {
    studentRepository.shutDown
  }
}

object StudentService {

  // Trait and its implementations representing all possible messages that can be sent to this Behavior
  sealed trait StudentCommand

  final case class AddStudent(student: Student, replyTo: ActorRef[Response]) extends StudentCommand

  final case class GetAll(replyTo: ActorRef[Seq[StudentResponse]]) extends StudentCommand

  final case class GetStudentById(id: Int, replyTo: ActorRef[Option[Student]]) extends StudentCommand

  final case class DeleteStudent(id: Int, replyTo: ActorRef[Response]) extends StudentCommand

  final case class AddCourse(studentId: Int, courseId: Int, replyTo: ActorRef[Response]) extends StudentCommand

  final case class CourseEvaluation(courseId: Int, feedback: String)

  final case class EvaluateCourse(studentId: Int, courseEvaluation: CourseEvaluation, replyTo: ActorRef[Response]) extends StudentCommand

  final case class StudentResponse(id: Int, name: String, courses: List[StudentCoursesOverview])

  final case class StudentCoursesOverview(name: String, teacher: String, feedback: Option[String])

  def apply(studentRepository: StudentRepository): Behavior[StudentCommand] =
    Behaviors.setup { ctx =>
      implicit val ec: ExecutionContextExecutor = ctx.system.executionContext

      def studentExist(student: Student): Future[Boolean] = {
        student match {
          case Student(None, name) => studentRepository.findStudentByName(name).map(optStudent => optStudent.isDefined)
          case Student(id, _) => studentRepository.findStudentById(id.get).map(optStudent => optStudent.isDefined)
          case _ => Future(false)
        }
      }

      Behaviors.receiveMessage {
        case AddStudent(student, replyTo) =>
          studentExist(student).onComplete {
            case Success(r) =>
              if (r) {
                replyTo ! KO("Student already exists")
              } else {
                studentRepository.insertStudent(student)
                replyTo ! OK("Success")
              }
            case Failure(e) => replyTo ! KO(e.getMessage)
          }
          Behaviors.same
        case GetAll(replyTo) =>
          studentRepository.findAll().onComplete {
            case Success(value) => {
              println(value)
              replyTo ! value
                .groupBy(record => (record._1, record._2))
                .map(tuple => StudentResponse(
                  tuple._1._1,
                  tuple._1._2,
                  tuple._2
                    .filter(e => e._3.isDefined && e._4.isDefined)
                    .map(f => StudentCoursesOverview(f._3.get, f._4.get, f._5.flatten))
                    .toList
                ))
                .toSeq
            }
            case Failure(exception) => replyTo ! Seq.empty
          }
          Behaviors.same
        case GetStudentById(id, replyTo) =>
          studentRepository.findStudentById(id).onComplete {
            case Success(value) => replyTo ! value
            case Failure(e) => replyTo ! Option.empty
          }
          Behaviors.same
        case DeleteStudent(id, replyTo) =>
          studentRepository.deleteStudentById(id).onComplete {
            case Success(value) => replyTo ! OK(s"Deleted: ${value}")
            case Failure(e) => replyTo ! KO(e.getMessage)
          }
          Behaviors.same
        case AddCourse(studentId, courseId, replyTo) =>
          studentRepository.addCourse(studentId, courseId).onComplete {
            case Success(id) =>
              replyTo ! OK(s"Student: ${studentId} successfully subscribed to course: ${courseId}, subscription: ${id}")
            case Failure(e) =>
              replyTo ! KO(s"Can't subscribe student: ${studentId} to course: ${courseId} due to ${e.getMessage}")
          }
          Behaviors.same
        case EvaluateCourse(studentId, e, replyTo) =>
          studentRepository.evaluateCourse(studentId, e.courseId, e.feedback).onComplete {
            case Success(id) => replyTo ! OK(s"Feedback added ${id}")
            case Failure(exception) => replyTo ! KO(s"Failed to evaluate course due to: ${exception.getMessage}")
          }
          Behaviors.same
      }

    }
}


