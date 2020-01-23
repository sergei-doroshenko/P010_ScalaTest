package finalTask.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import finalTask.dao.{Course, Student, Teacher}
import finalTask.service.CourseService.CourseResponse
import finalTask.service.{Failed, Status, Successful}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {

  implicit object StatusFormat extends RootJsonFormat[Status] {
    def write(status: Status): JsValue = status match {
      case Failed     => JsString("Failed")
      case Successful => JsString("Successful")
    }

    def read(json: JsValue): Status = json match {
      case JsString("Failed")     => Failed
      case JsString("Successful") => Successful
      case _                      => throw DeserializationException("Status unexpected")
    }
  }

  import DefaultJsonProtocol._
  implicit val studentFormat = jsonFormat2(Student)
  implicit val teacherFormat = jsonFormat2(Teacher)
  implicit val courseFormat = jsonFormat3(Course)
  implicit val courseResponseFormat = jsonFormat3(CourseResponse)
}
