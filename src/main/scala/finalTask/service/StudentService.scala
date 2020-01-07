package finalTask.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import finalTask.dao.{Student, StudentRepository}
import samples.httptypedactors.JobRepository.KO

import scala.concurrent.ExecutionContextExecutor
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
      Behaviors.receiveMessage {
        case AddStudent(student, replyTo) if studentRepository.findStudentById(student.id.get) =>
          replyTo ! KO("Student already exists")
          Behaviors.same
        case AddStudent(student, replyTo) =>
          studentRepository.insertStudent(student)
          replyTo ! OK
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


