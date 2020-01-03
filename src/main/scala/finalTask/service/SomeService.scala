package finalTask.service

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.util.Timeout
import finalTask.dao.{Student, StudentsDbio}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class SomeService(studentsDbio: StudentsDbio) {

  implicit val system: ActorSystem[Root.SystemMessage] = ActorSystem(Root(), "root")
  implicit val timeout: Timeout = 3.seconds

  def findStudentById(id: Int): Option[Student] = {
    import ServiceQuery._
    val studentQueryFuture: Future[ActorRef[Query]] = system ? Root.FindStudent // ask
    val serviceQueryActor = Await.result(studentQueryFuture, 1.second)

    val eventualStudent: Future[Student] = serviceQueryActor ? (FindById("", id, _)) // ask
    val student: Student = Await.result(eventualStudent, 3.seconds)

    system.log.info(s"Found student: $student")
    Option.apply(student)
  }

  def saveStudent(student: Student): Future[Student] =
    studentsDbio.insertStudent(student)

  def shutDown: Unit = {
    studentsDbio.shutDown
    system.terminate()
  }

  object Root {
    sealed trait SystemMessage
    case class Stop(reason: String) extends SystemMessage
    case class FindStudent(replyTo: ActorRef[ActorRef[ServiceQuery.Query]]) extends SystemMessage

    def apply(): Behavior[SystemMessage] = Behaviors.setup { ctx =>
      val serviceQuery: ActorRef[ServiceQuery.Query] = ctx.spawn(ServiceQuery(), "serviceQuery")

      Behaviors.receiveMessage {
        case Stop(reason) =>
          ctx.log.info(s"Stopping with reason: $reason")
          Behaviors.stopped
        case FindStudent(replyTo) =>
          replyTo ! serviceQuery // here we send actor ref to ServiceQuery actor
          Behaviors.same
      }
    }
  }

  object ServiceQuery {
    sealed trait Query
    case class FindById(tkey: String, studentId: Int, replyTo: ActorRef[Student]) extends Query

    import scala.concurrent.ExecutionContext.Implicits.global
    def apply(): Behavior[Query] = Behaviors.setup( ctx => Behaviors.receiveMessage {
      case FindById(_, id, replyTo) =>
        val future = studentsDbio.findStudentById(id)
        future.onComplete{
          case Success(x) => x.map(st => replyTo ! st)
          case Failure(e) => e.printStackTrace
        }
        Behaviors.same
      case _ => Behaviors.ignore
    })
  }

}

object SomeService {
  def apply(studentsDbio: StudentsDbio): SomeService = {
    new SomeService(studentsDbio)
  }
}


