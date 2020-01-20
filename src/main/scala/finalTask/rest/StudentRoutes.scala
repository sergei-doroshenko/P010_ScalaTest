package finalTask.rest

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import finalTask.dao.Student
import finalTask.service.StudentService

import scala.concurrent.Future
import scala.concurrent.duration._

class StudentRoutes(buildJobRepository: ActorRef[StudentService.Command])(implicit system: ActorSystem[_]) extends JsonSupport {

  import akka.actor.typed.scaladsl.AskPattern._
  implicit val timeout: Timeout = 3.seconds

  lazy val theJobRoutes: Route =
    pathPrefix("students") {
      concat(
        pathEnd {
          concat(
            post {
              entity(as[Student]) { student =>
                val operationPerformed: Future[StudentService.Response] =
                  buildJobRepository.ask(StudentService.AddStudent(student, _))
                onSuccess(operationPerformed) {
                  case StudentService.OK => complete("Student added")
                  case StudentService.KO(reason) => complete(StatusCodes.BadRequest -> reason)
                }
              }
            }
          )
        },
        (get & path(IntNumber)) { id =>
          val maybeStudent: Future[Option[Student]] =
            buildJobRepository.ask(StudentService.GetStudentById(id, _))
          rejectEmptyResponse {
            complete(maybeStudent)
          }
        },
        (delete & path(IntNumber)) { id =>
          val operationPerformed: Future[StudentService.Response] =
            buildJobRepository.ask(StudentService.DeleteStudent(id, _))
          onSuccess(operationPerformed) {
            case StudentService.OK => complete("Student deleted")
            case StudentService.KO(reason) => complete(StatusCodes.BadRequest -> reason)
          }
        }
      )
    }
}
