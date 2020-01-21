package finalTask.rest

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import finalTask.dao.Teacher
import finalTask.service.{KO, OK, Response, TeacherService}
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.concurrent.duration._

class TeacherRoutes(teacherServiceActor: ActorRef[TeacherService.TeacherCommand])(implicit system: ActorSystem[_]) extends JsonSupport {

  import akka.actor.typed.scaladsl.AskPattern._

  implicit val timeout: Timeout = 3.seconds

  lazy val theTeacherRoutes: Route =
    pathPrefix("teachers") {
      concat(
        pathEnd {
          post {
            entity(as[Teacher]) { teacher =>
              val createdTeacher: Future[Teacher] = teacherServiceActor.ask(TeacherService.AddTeacher(teacher, _))
              rejectEmptyResponse {
                complete(createdTeacher)
              }
            }
          }
        },
        get {
          val teacherList: Future[Seq[Teacher]] = teacherServiceActor.ask(TeacherService.GetAllTeachers)
          rejectEmptyResponse {
            complete(teacherList)
          }
        },
        (delete & path(IntNumber)) { id =>
          val operationPerformed: Future[Response] = teacherServiceActor.ask(TeacherService.DeleteTeacher(id, _))
          onSuccess(operationPerformed) {
            case OK(msg) => complete(s"Teacher deleted: ${msg}")
            case KO(reason) => complete(StatusCodes.BadRequest -> reason)
          }
        }
      )
    }
}
