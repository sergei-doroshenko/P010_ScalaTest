package finalTask

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import akka.http.scaladsl.unmarshalling._
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.common.JsonEntityStreamingSupport


import scala.concurrent.Future
import scala.util.{Failure, Success}


object ServerTest {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
      uri = "http://localhost:8080/teachers",
      method = HttpMethods.POST,
      entity = HttpEntity(ContentTypes.`application/json`, """{"name":"Mr Fergusson"}""")
    ))

    responseFuture
      .onComplete {
        case Success(res) => println(Unmarshal(res.entity).to[String])
        case Failure(_) => sys.error("something wrong")
      }
  }
}
