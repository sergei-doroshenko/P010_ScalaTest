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

  // Definition of the a build job and its possible status values
  sealed trait Status

  object Successful extends Status

  object Failed extends Status

  // Trait defining successful and failure responses
  sealed trait Response

  case object OK extends Response

  final case class KO(reason: String) extends Response

  // Trait and its implementations representing all possible messages that can be sent to this Behavior
  sealed trait Command

  final case class AddStudent(student: Student, replyTo: ActorRef[Response]) extends Command

  final case class GetStudentById(id: Int, replyTo: ActorRef[Option[Student]]) extends Command

  final case class DeleteStudent(id: Int, replyTo: ActorRef[Response]) extends Command

  def apply(studentRepository: StudentRepository): Behavior[Command] =
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
                replyTo ! OK
              }
            case Failure(e) => replyTo ! KO(e.getMessage)
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
            case Success(value) => replyTo ! OK
            case Failure(e) => replyTo ! KO(e.getMessage)
          }
          Behaviors.same
      }

    }
}


