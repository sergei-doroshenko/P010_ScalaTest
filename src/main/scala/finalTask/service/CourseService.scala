package finalTask.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import finalTask.dao.{Course, CourseRepository, Teacher}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

class CourseService(courseRepository: CourseRepository) {
  def shutDown: Unit = {
    courseRepository.shutDown
  }
}

object CourseService {

  sealed trait CourseCommand

  final case class GetAllCourses(replyTo: ActorRef[Seq[CourseResponse]]) extends CourseCommand

  final case class AddCourse(course: Course, replyTo: ActorRef[Course]) extends CourseCommand

  final case class CourseResponse(id: Int, name: String, teacherName: String)

  def apply(courseRepository: CourseRepository): Behavior[CourseCommand] =
    Behaviors.setup { ctx =>
      implicit val ec: ExecutionContextExecutor = ctx.system.executionContext
      Behaviors.receiveMessage {
        case GetAllCourses(replyTo) =>
          courseRepository.findAllCoursesWithTeachers.onComplete {
            case Success(value) => replyTo ! value.map(v => CourseResponse(v._1.id.get, v._1.name, v._2.name)).toList
            case Failure(e) => replyTo ! Seq.empty
          }
          Behaviors.same
        case AddCourse(course, replyTo) =>
          courseRepository.insertCourse(course).onComplete {
            case Success(course) => replyTo ! course
            case Failure(e) => replyTo ! null
          }
          Behaviors.same
      }
    }
}
