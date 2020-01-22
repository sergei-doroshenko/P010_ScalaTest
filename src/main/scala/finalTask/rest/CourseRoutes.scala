package finalTask.rest

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import finalTask.dao.{Course, Teacher}
import finalTask.service.CourseService
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.concurrent.duration._

class CourseRoutes(courseServiceActor: ActorRef[CourseService.CourseCommand])(implicit system: ActorSystem[_]) extends JsonSupport {

  import akka.actor.typed.scaladsl.AskPattern._

  implicit val timeout: Timeout = 3.seconds

  lazy val theCourseRoutes: Route =
    pathPrefix("courses") {
      concat(
        pathEnd {
          post {
            entity(as[Course]) { course =>
              val createdCourse: Future[Course] = courseServiceActor.ask(CourseService.AddCourse(course, _))
              rejectEmptyResponse {
                complete(createdCourse)
              }
            }
          }
        },
        get {
          val courseList: Future[Seq[(Course, Teacher)]] = courseServiceActor.ask(CourseService.GetAllCourses)
          rejectEmptyResponse {
            complete(courseList)
          }
        }
      )
    }
}
