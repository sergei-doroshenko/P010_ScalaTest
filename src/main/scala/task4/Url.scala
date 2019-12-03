package task4.urlPackage

import scalaBasic.urlPackage.{Read, Show}
import task4.urlPackage.Credentials.{LoginAndPassword, LoginOnly}

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
case class Url(   protocol   : Option[String]
                , credentials: Option[Credentials]
                , host       : String
                , path       : Path
                , query      : Option[Query]
              )

sealed trait Credentials

object Credentials {
  case class LoginOnly(username: String) extends Credentials
  case class LoginAndPassword(username: String, password: String) extends Credentials
}

case class Path(segments: Seq[String]) {
  override def toString: String = segments.mkString("/", "/", "")
}
object Path {
//  def apply(segments: Seq[String]): Path = Path(segments)
  def apply(path: String): Path = Path(path.split("/"))
}

case class Query(parameters: Map[String, String] = Map.empty) {
  override def toString: String = parameters.map { case (k, v) => k + "=" + v }.mkString("?", "&", "")
}

object Query {
//  def apply(parameters: Map[String, String] = Map.empty): Query = new Query(parameters)
  def apply(query: String): Query = {
    val parameters = query.split("&").map(t => t.split("=")).map(p => (p(0), p(1))).toMap
    Query(parameters)
  }
}

object Url {
  /*def apply(protocol: String, credentials: Credentials, host: String, path: Path, query: Query): Url = Url(Some(protocol), Some(credentials), host, path, Some(query))
  def apply(credentials: Credentials, host: String, path: Path, query: Query): Url = Url(Option.empty, Some(credentials), host, path, Some(query))
  def apply(host: String, path: Path, query: Query): Url = Url(Option.empty, Option.empty, host, path, Some(query))*/

  object ops {

    // implement here all the required type classes
    // To understand the material it would be enough to implement Show
    // Read implementation is a task with asterisk

    implicit val urlCanShow: Show[Url] = {
      case Url(Some(protocol), Some(LoginAndPassword(username, password)), host, path, Some(query)) => s"${protocol}://${username}:${password}@${host}${path}${query}"
      case Url(Some(protocol), Some(LoginOnly(username)), host, path, Some(query)) => s"${protocol}://${username}@${host}${path}${query}"
      case Url(Some(protocol), None, host, path, Some(query)) => s"${protocol}://${host}${path}${query}"
      case Url(None, Some(LoginAndPassword(username, password)), host, path, Some(query)) => s"${username}:${password}@${host}${path}${query}"
      case Url(None, None, host, path, Some(query)) => s"${host}${path}${query}"
      case _ => "Unsupported type"
    }

    implicit val urlCanRead: Read[Url] = url => {
      val UrlPattern = """(?:(\w++)://)?(?:([-_A-Za-z0-9]++)(?::([-_A-Za-z0-9]++))?@)?(?:([-._A-Za-z0-9]++(?::[0-9]++)?)(?:/)?)(?:([-/._A-Za-z0-9]++)[?]?)?(?:([-=&.%_A-Za-z0-9]++))?""".r
      url match {
        case UrlPattern(protocol, null, null, host, path, query) => Right(Url(Some(protocol), Option.empty, host, Path(path), Some(Query(query))))
        case UrlPattern(null, username, password, host, path, query) => Right(Url(Option.empty, Some(LoginAndPassword(username, password)), host, Path(path), Some(Query(query))))
        case UrlPattern(protocol, username, password, host, path, null) => Right(Url(Some(protocol), Some(LoginAndPassword(username, password)), host, Path(path), Option.empty))
        case UrlPattern(protocol, username, password, host, path, query) => Right(Url(Some(protocol), Some(LoginAndPassword(username, password)), host, Path(path), Some(Query(query))))
        case invalid => Left(s"""Unable to read a URL from "$invalid".""")
      }
    }

    def parse(url:String): Unit = {
      val UrlPattern = """(?:(\w++)://)?(?:([-_A-Za-z0-9]++)(?::([-_A-Za-z0-9]++))?@)?(?:([-._A-Za-z0-9]++(?::[0-9]++)?)(?:/)?)(?:([-/._A-Za-z0-9]++)[?]?)?(?:([-=&.%_A-Za-z0-9]++))?""".r
      url match {
        case UrlPattern(protocol, null, null, host, path, null) => println(s"2$protocol-$host-$path")
        case UrlPattern(protocol, null, null, host, path, query) => println(s"1$protocol-$host-$path-$query")
        case UrlPattern(null, username, password, host, path, query) => println(s"3$username-$password-$host-$path-$query")
        case UrlPattern(protocol, username, password, host, path, null) => println(s"4$protocol-$username-$password-$host-$path")
        case UrlPattern(protocol, username, password, host, path, query) => println(s"5$protocol-$username-$password-$host-$path-$query")
        case invalid => println(s"""Unable to read a URL from "$invalid".""")
      }
    }
  }

}

object UrlApp extends App {
  println("Start...")
  import task4.urlPackage.Url.ops.urlCanShow

  val query = Query(Map("a" -> "b", "key" -> "val"))
  val url = Url(Some("http"), Some(LoginAndPassword("username", "pass")), "host.org", Path("path"), Some(query))
  println(Show[Url].show(url))

  import task4.urlPackage.Url.ops.parse
  parse("http://username:pass@host.org/path?a=b&key=val")
  parse("username:pass@host.org/path?a=b&key=val")
  parse("http://username:pass@host.org/path")
  parse("http://host.org/path?a=b&key=val")
  parse("http://host.org/path")

//  println(Query("key=val&a=b"))
}
