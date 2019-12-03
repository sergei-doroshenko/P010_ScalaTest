package task4

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}
import scalaBasic.urlPackage.{Read, Show}
import task4.urlPackage.Credentials.LoginAndPassword
import task4.urlPackage.{Path, Query, Url}


class UrlClassesSpec  extends WordSpec with Matchers with TableDrivenPropertyChecks {

  "Show" can {
    import task4.urlPackage.Url.ops.urlCanShow

    "show" should {
      "be able to display url" in {
        val query = Query(Map("key" -> "val", "a" -> "b"))
        val cases = Table(
          ("url", "expected"),
          (
            Url(Some("http"), Some(LoginAndPassword("user", "pass")), "host.org", Path("test"), Some(query)),
            "http://user:pass@host.org/test?key=val&a=b"
          ),
          (
            Url(Option.empty, Some(LoginAndPassword("user", "pass")), "host.org", Path("test"), Some(query)),
            "user:pass@host.org/test?key=val&a=b"
          ),
          (
            Url(Some("http"), Some(LoginAndPassword("user", "pass")), "host.org", Path("test"), Some(query)),
            "http://user:pass@host.org/test?key=val&a=b"
          ),
          (
            Url(Some("http"), Option.empty, "host.org", Path("test"), Some(query)),
            "http://host.org/test?key=val&a=b"
          ),
        )

        forEvery(cases) { (url, expected) =>
          Show[Url].show(url) should be (expected)
        }
      }
    }
  }

  "Read" can {
    import task4.urlPackage.Url.ops.urlCanRead

    "read" should {
      "be able to read valid url" in {
        val query = Query(Map("a" -> "b", "key" -> "val"))
        val cases = Table(
          ("url", "expected"),
          (
            "http://user:pass@host.org/test?key=val&a=b",
            Url(Some("http"), Some(LoginAndPassword("user", "pass")), "host.org", Path("test"), Some(query)),
          ),
          (
            "http://host.org/test?key=val&a=b",
            Url(Some("http"), Option.empty, "host.org", Path("test"), Some(query)),
          ),
          (
            "user:pass@host.org/test?key=val&a=b",
            Url(Option.empty, Some(LoginAndPassword("user", "pass")), "host.org", Path("test"), Some(query)),
          ),
          (
            "http://user:pass@host.org/test",
            Url(Some("http"), Some(LoginAndPassword("user", "pass")), "host.org", Path("test"), Option.empty),
          ),
        )

        forEvery(cases) { (url, expected) =>
          Read[Url].read(url) should be (Right(expected))
        }
      }

      "be able to read invalid url" in {
        val cases = Table(
          ("host.org"),
          ("user:pass")
        )

        forEvery(cases) { url =>
          Read[Url].read(url) should be(Left(s"""Unable to read a URL from "$url"."""))
        }
      }
    }
  }
}
