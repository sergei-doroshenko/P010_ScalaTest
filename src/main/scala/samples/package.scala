package object samples {
  import scala.util.matching.{Regex => Re}
  object TopRegexp {
    lazy val userName: Re = "^[a-z0-9_-]{3,15}$".r
    lazy val password: Re = """((?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})""".r
    lazy val hexColor: Re = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$".r
  }
}
