package task4

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}
import scalaBasic.urlPackage.{Read, Show}

class TypeClassesSpec  extends WordSpec with Matchers with TableDrivenPropertyChecks {

  "Show" can {
    import scalaBasic.urlPackage.Show.ops.intCanShow

    "show" should {
      "be able to display int" in {
        val cases = Table(
          ("number", "expected"),
          (42, "42"),
          (-42, "-42")
        )

        forEvery(cases) { (number, expected) =>
          Show[Int].show(number) should be (expected)
        }
      }
    }
  }

  "Read" can {
    import scalaBasic.urlPackage.Read.ops.intCanRead

    "read" should {
      "be able to read valid int" in {
        val cases = Table(
          ("number", "expected"),
          ("42", 42),
          ("+42", 42),
          ("-42", -42)
        )

        forEvery(cases) { (number, expected) =>
          Read[Int].read(number) should be (Right(expected))
        }
      }
      "be able to read invalid int" in {
        val cases = Table(
          ("number"),
          ("apple"),
          ("+4+2"),
          ("-4-2")
        )

        forEvery(cases) { number =>
          Read[Int].read(number) should be(Left(s"""Unable to read an Int from "$number"."""))
        }
      }
    }
  }
}
