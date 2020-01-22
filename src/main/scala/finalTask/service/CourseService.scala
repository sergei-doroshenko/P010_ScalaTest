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

  final case class GetAllCourses(replyTo: ActorRef[Seq[(Course, Teacher)]]) extends CourseCommand

  final case class AddCourse(course: Course, replyTo: ActorRef[Course]) extends CourseCommand

  def apply(courseRepository: CourseRepository): Behavior[CourseCommand] =
    Behaviors.setup { ctx =>
      implicit val ec: ExecutionContextExecutor = ctx.system.executionContext
      Behaviors.receiveMessage {
        case GetAllCourses(replyTo) =>
          courseRepository.findAllCoursesWithTeachers.onComplete {
            case Success(value) => replyTo ! value
            case Failure(e) => replyTo ! Seq.empty
          }
          Behaviors.same
        case AddCourse(course, replyTo) =>
          courseRepository.insertCourse(course).onComplete {
            case Success(value) => replyTo ! value
            case Failure(e) => replyTo ! null
          }
          Behaviors.same
      }
    }
}
