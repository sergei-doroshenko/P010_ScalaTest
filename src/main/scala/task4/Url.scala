package task4.urlPackage

import scalaBasic.urlPackage.{Read, Show}
import task4.urlPackage.Url.ops.{HttpUrl, Query}

/*
 * Task is to get practice in ADT and type classes.
 * - Refactor Url in a way it would be possible to express distinct urls:
 *   - with protocol
 *   - without protocol
 *   - without authentication
 *   - with authentication segment:
 *     - only username
 *     - username and password
 *     - it should be impossible to express url with password only
 *   - it would be better to make Query a wrapper over a Map[String, String] rather than just a String
 *   - no nulls should be needed to define all these states
 * - Implement Show type class for your Url
 * - [*] Implement Read type class for you Url
 */


// Make Url type more strict, rewrite it as Algebraic Data Type
//case class Url(protocol: String, username: String, password: String, host: String, path: String, query: String)
sealed trait Url {
  def protocol: String

  def username: String

  def password: String

  def host: String

  def path: String

  def query: Query
}

object Url {

  object ops {

    // implement here all the required type classes
    // To understand the material it would be enough to implement Show
    // Read implementation is a task with asterisk
    case class HttpUrl(username: String, password: String, host: String, path: String, query: Query) extends Url {
      override def protocol: String = "http"
    }

    case class Query() {
      var map:Map[String, String] = Map()

      def add(key: String, value: String): Query = {
        map += (key -> value)
        this
      }

      override def toString: String = map.map{case (k, v) => k + "=" + v}.mkString("?", "&", "")
    }

    implicit val urlCanShow: Show[Url] = url =>
      s"${url.protocol}://${url.username}:${url.password}@${url.host}${url.path}${url.query}"

    implicit val urlCanRead: Read[Url] = {
      case url => Right(HttpUrl("username", "pass", "host.org", "/path", Query()))
      case invalid => Left(s"""Unable to read a URL from "$invalid".""")
    }
  }

}

object UrlApp extends App {
  import task4.urlPackage.Url.ops.urlCanShow
  val query = Query().add("a", "b").add("key", "val")
  val http = HttpUrl("username", "pass", "host.org", "/path", query)
  println(Show[Url].show(http))
}
