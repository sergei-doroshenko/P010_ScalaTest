package task4

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}
import scalaBasic.urlPackage.{Read, Show}
import task4.urlPackage.Url
import task4.urlPackage.Url.ops.{Query, WithAuthUrl, WithProtocolUrl, WithoutAuthUrl, WithoutProtocolUrl}

class UrlClassesSpec  extends WordSpec with Matchers with TableDrivenPropertyChecks {

  "Show" can {
    import task4.urlPackage.Url.ops.urlCanShow

    "show" should {
      "be able to display int" in {
        val query = Query().add("key", "val").add("a", "b")
        val cases = Table(
          ("url", "expected"),
          (
            WithProtocolUrl("http", "user", "pass", "host.org", "/test", query),
            "http://user:pass@host.org/test?key=val&a=b"
          ),
          (
            WithoutProtocolUrl("user", "pass", "host.org", "/test", query),
            "user:pass@host.org/test?key=val&a=b"
          ),
          (
            WithAuthUrl("http", "user", "pass", "host.org", "/test", query),
            "http://user:pass@host.org/test?key=val&a=b"
          ),
          (
            WithoutAuthUrl("http", "host.org", "/test", query),
            "http://host.org/test?key=val&a=b"
          ),
        )

        forEvery(cases) { (url, expected) =>
          Show[Url].show(url) should be (expected)
        }
      }
    }
  }
}
