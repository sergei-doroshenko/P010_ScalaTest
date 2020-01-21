package finalTask.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import finalTask.dao.{Teacher, TeacherRepository}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

class TeacherService(teacherRepository: TeacherRepository) {

  def shutDown: Unit = {
    teacherRepository.shutDown
  }
}

object TeacherService {

  sealed trait TeacherCommand

  final case class GetAllTeachers(replyTo: ActorRef[Seq[Teacher]]) extends TeacherCommand

  final case class AddTeacher(teacher: Teacher, replyTo: ActorRef[Teacher]) extends TeacherCommand

  final case class DeleteTeacher(id: Int, replyTo: ActorRef[Response]) extends TeacherCommand

  def apply(teacherRepository: TeacherRepository): Behavior[TeacherCommand] =
    Behaviors.setup { ctx =>
      implicit val ec: ExecutionContextExecutor = ctx.system.executionContext
      Behaviors.receiveMessage {
        case GetAllTeachers(replyTo) =>
          teacherRepository.findAll.onComplete(res =>
            if (res.isSuccess)
              replyTo ! res.get
            else
              replyTo ! Seq.empty
          )
          Behaviors.same
        case AddTeacher(teacher, replyTo) =>
          teacherRepository.insertTeacher(teacher).onComplete {
            case Success(value) => replyTo ! value
            case Failure(exception) => replyTo ! null
          }
          Behaviors.same
        case DeleteTeacher(id, replyTo) =>
          teacherRepository.deleteTeacher(id).onComplete {
            case Success(value) => replyTo ! OK(s"Deleted teacher: ${value}")
            case Failure(e) => replyTo ! KO(s"Falied due to ${e.getMessage}")
          }
          Behaviors.same
      }
    }
}




