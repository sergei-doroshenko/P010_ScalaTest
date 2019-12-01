package task4.urlPackage

import scalaBasic.urlPackage.{Read, Show}
import task4.urlPackage.Url.ops.{Query, WithoutProtocolUrl}

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
sealed trait Url

object Url {

  object ops {

    // implement here all the required type classes
    // To understand the material it would be enough to implement Show
    // Read implementation is a task with asterisk
    case class WithProtocolUrl(protocol: String, username: String, password: String, host: String, path: String, query: Query) extends Url

    case class WithoutProtocolUrl(username: String, password: String, host: String, path: String, query: Query) extends Url

    case class WithAuthUrl(protocol: String, username: String, password: String, host: String, path: String, query: Query) extends Url

    case class WithoutAuthUrl(protocol: String, host: String, path: String, query: Query) extends Url

    case class WithQueryUrl(protocol: String, username: String, password: String, host: String, path: String, query: Query) extends Url

    case class WithoutQueryUrl(protocol: String, username: String, password: String, host: String, path: String) extends Url

    case class Query(query: String = "") {
      var map: Map[String, String] = Map()

      def add(key: String, value: String): Query = {
        map += (key -> value)
        this
      }

      override def toString: String = map.map { case (k, v) => k + "=" + v }.mkString("?", "&", "")
    }

    implicit val urlCanShow: Show[Url] = {
      case WithProtocolUrl(protocol, username, password, host, path, query) => s"${protocol}://${username}:${password}@${host}${path}${query}"
      case WithoutProtocolUrl(username, password, host, path, query) => s"${username}:${password}@${host}${path}${query}"
      case WithAuthUrl(protocol, username, password, host, path, query) => s"${protocol}://${username}:${password}@${host}${path}${query}"
      case WithoutAuthUrl(protocol, host, path, query) => s"${protocol}://${host}${path}${query}"
      case WithQueryUrl(protocol, username, password, host, path, query) => s"${protocol}://${username}:${password}@${host}${path}${query}"
      case WithoutQueryUrl(protocol, username, password, host, path) => s"${protocol}://${username}:${password}@${host}${path}"
      case _ => "Unsupported type"
    }

    implicit val urlCanRead: Read[Url] = url => {
      val regex =
        """
          |(http)://
          |(user)\:(pass)@
          |(host\.org)
          |(\/test)
          |(\?[A-Za-z0-9=]*)
          |""".r
      url match {
        case regex(protocol, username, password, host, path, query) => Right(WithProtocolUrl(protocol, username, password, host, path, Query(query)))
        case invalid => Left(s"""Unable to read a URL from "$invalid".""")
      }

    }
  }

}

object UrlApp extends App {

  import task4.urlPackage.Url.ops.urlCanShow

  val query = Query().add("a", "b").add("key", "val")
  val url = WithoutProtocolUrl("username", "pass", "host.org", "/path", query)
  println(Show[Url].show(url))

  val pattern = "(?:https?://)?(?:www\\.)?(\\w*)?:(\\w*@)?([A-Za-z0-9._%+-]+)/?.*".r
  for (patternMatch <- pattern.findAllMatchIn("username:pass@host.org/path?a=b&key=val"))
    println(s"${patternMatch.group(0)}, ${patternMatch.group(1)}, ${patternMatch.group(2)}" +
      s", ${patternMatch.group(3)}, ${patternMatch.group(4)}")
}
