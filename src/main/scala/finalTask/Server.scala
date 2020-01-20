package finalTask

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.{ActorSystem, Behavior, PostStop}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import finalTask.dao.{H2DBComponent, StudentRepository}
import finalTask.rest.StudentRoutes
import finalTask.service.StudentService

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

/**
 * $ curl -d {\"name\":\"Adam\"} -H "Content-Type: application/json" -X POST localhost:8080/students
 * Student added
 *
 * $ curl localhost:8080/students/1
 * {"id":1,"name":"Adam"}
 *
 * $ curl -X DELETE localhost:8080/students/1
 * Student deleted
 *
 * $ curl localhost:8080/students/1
 * The requested resource could not be found.
 */
object Server {

  sealed trait Message

  private final case class StartFailed(cause: Throwable) extends Message

  private final case class Started(binding: ServerBinding) extends Message

  case object Stop extends Message

  def apply(host: String, port: Int): Behavior[Message] = Behaviors.setup { ctx =>

    implicit val system = ctx.system
    implicit val untypedSystem: akka.actor.ActorSystem = ctx.system.toClassic
    implicit val materializer: ActorMaterializer = ActorMaterializer()(ctx.system.toClassic)
    implicit val ec: ExecutionContextExecutor = ctx.system.executionContext

    val dbComponent = H2DBComponent()
    val studentRepository = StudentRepository(dbComponent)
    val buildStudentService = ctx.spawn(StudentService(studentRepository), "StudentService")
    val routes = new StudentRoutes(buildStudentService)

    val serverBinding: Future[Http.ServerBinding] =
      Http.apply().bindAndHandle(routes.theJobRoutes, host, port)
    ctx.pipeToSelf(serverBinding) {
      case Success(binding) => Started(binding)
      case Failure(ex) => StartFailed(ex)
    }

    def running(binding: ServerBinding): Behavior[Message] =
      Behaviors.receiveMessagePartial[Message] {
        case Stop =>
          ctx.log.info(
            "Stopping server http://{}:{}/",
            binding.localAddress.getHostString,
            binding.localAddress.getPort)
          Behaviors.stopped
      }.receiveSignal {
        case (_, PostStop) =>
          binding.unbind()
          Behaviors.same
      }

    def starting(wasStopped: Boolean): Behaviors.Receive[Message] =
      Behaviors.receiveMessage[Message] {
        case StartFailed(cause) =>
          throw new RuntimeException("Server failed to start", cause)
        case Started(binding) =>
          ctx.log.info(
            "Server online at http://{}:{}/",
            binding.localAddress.getHostString,
            binding.localAddress.getPort)
          if (wasStopped) ctx.self ! Stop
          running(binding)
        case Stop =>
          // we got a stop message but haven't completed starting yet,
          // we cannot stop until starting has completed
          starting(wasStopped = true)
      }

    starting(wasStopped = false)
  }


  def main(args: Array[String]) {
    val system: ActorSystem[Server.Message] =
      ActorSystem(Server("localhost", 8080), "BuildJobsServer")
  }
}
