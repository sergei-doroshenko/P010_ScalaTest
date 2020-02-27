package finalTask.rest

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import finalTask.dao.Student
import finalTask.service.StudentService.{CourseEvaluation, StudentCoursesOverview}
import finalTask.service.{KO, LogService, OK, Response, StudentService}
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.concurrent.duration._

class StudentRoutes(studentService: ActorRef[StudentService.StudentCommand], log: LogService)(implicit system: ActorSystem[_]) extends JsonSupport {

  import akka.actor.typed.scaladsl.AskPattern._

  implicit val timeout: Timeout = 3.seconds

  val addStudentRoute = pathEnd {
    concat(
      post {
        entity(as[Student]) { student =>
          val operationPerformed: Future[Response] =
            studentService.ask(StudentService.AddStudent(student, _))
          onSuccess(operationPerformed) {
            case OK(msg) =>
              val message = s"Student added: ${msg}"
              log.info(message)
              complete(message)
//            case KO(reason) => complete(StatusCodes.BadRequest -> reason)
            case KO(reason) => throw new RuntimeException(reason)
          }
        }
      }
    )
  }

  val getAllStudentsRoute = get {
    val all = studentService.ask(StudentService.GetAll)
    rejectEmptyResponse {
      complete(all)
    }
  }

  val getOneStudentRoute = (get & path(IntNumber)) { id =>
    val maybeStudent: Future[Option[Student]] =
      studentService.ask(StudentService.GetStudentById(id, _))
    rejectEmptyResponse {
      complete(maybeStudent)
    }
  }

  val deleteStudentRoute = (delete & path(IntNumber)) { id =>
    val operationPerformed: Future[Response] =
      studentService.ask(StudentService.DeleteStudent(id, _))
    onSuccess(operationPerformed) {
      case OK(msg) => complete(s"Student deleted: ${msg}")
      case KO(reason) => complete(StatusCodes.BadRequest -> reason)
    }
  }

  lazy val theStudentRoutes: Route =
    pathPrefix("students") {
      concat(
        addStudentRoute,
        getAllStudentsRoute,
        getOneStudentRoute,
        deleteStudentRoute,
        (post & path(IntNumber / "courses")) { studentId =>
          entity(as[Map[String, Int]]) { param =>
            val response: Future[Response] = studentService.ask(StudentService.AddCourse(studentId, param("courseId"), _))
            onSuccess(response) {
              case OK(msg) => complete(msg)
              case KO(reason) => complete(StatusCodes.BadRequest -> reason)
            }
          }
        },
        (put & path(IntNumber / "courses")) { studentId =>
          entity(as[CourseEvaluation]) { param =>
            val response: Future[Response] = studentService.ask(StudentService.EvaluateCourse(studentId, param, _))
            onSuccess(response) {
              case OK(msg) => complete(msg)
              case KO(reason) => complete(StatusCodes.BadRequest -> reason)
            }
          }
        }
      )
    }
}
