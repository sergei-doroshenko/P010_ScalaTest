package lecture5

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}
import lecture5.Serializable.ops._

class SerializationClassesSpec extends WordSpec with Matchers with TableDrivenPropertyChecks {
  "Serializable" can {
    "serialize" should {
      "be able to serialize persons" in {
        val cases = Table(
          ("person", "expected"),
          (
            Person("personName", 30),
            JsObject("name" -> JsString("personName"), "age" -> JsNumber(30), "aliases" -> JsArray(List()))
          ),
          (
            Person("Alan", 40, List("developer","programmer")),
            JsObject("name" -> JsString("Alan"), "age" -> JsNumber(40), "aliases" -> JsArray(List(JsString("developer"), JsString("programmer"))))
          )
        )

        forEvery(cases) { (person, expected) =>
          Serializable.ops.serialize(person) should be(expected)
        }
      }
    }

    "serialize" should {
      "be able to serialize null" in {
        Serializable.ops.serialize(null) should be(JsNull())
      }
    }

    "serialize" should {
      "be able to serialize boolean" in {
        Serializable.ops.serialize(true) should be(JsBool(true))
      }
    }

    "serialize" should {
      "be able to serialize numbers" in {
        Serializable.ops.serialize(10) should be(JsNumber(10))
      }
    }

    "serialize" should {
      "be able to serialize strings" in {
        Serializable.ops.serialize("hello") should be(JsString("hello"))
      }
    }

    "serialize" should {
      "be able to serialize arrays" in {
        val cases = Table(
          ("array", "expected"),
          (
            Array(1, 2, 3), JsArray(Seq(JsNumber(1), JsNumber(2), JsNumber(3)))
          )
        )

        forEvery(cases) { (person, expected) =>
          Serializable.ops.serialize(person) should be(expected)
        }
      }
    }

    "serialize" should {
      "be able to serialize lists" in {
        val p1 = Person("personName", 30)
        val p2 = Person("Alan", 40, List("man", "himan"))
        val cases = Table(
          ("list", "expected"),
          (
            p1 :: p2 :: Nil,
            JsArray(List(
              JsObject(Map("name" -> JsString("personName"), "age" -> JsNumber(30), "aliases" -> JsArray(List()))),
              JsObject(Map("name" -> JsString("Alan"), "age" -> JsNumber(40), "aliases" -> JsArray(List(JsString("man"), JsString("himan")))))
            ))
          )
        )

        forEvery(cases) { (list, expected) =>
          Serializable.ops.serialize(list) should be(expected)
        }
      }
    }

    "serialize" should {
      "be able to serialize maps" in {
        val p1 = Person("personName", 30)
        val p2 = Person("Alan", 40, List("man", "himan"))
        val cases = Table(
          ("map", "expected"),
          (
            Map("personA" -> p1, "personB" -> p2),
            JsObject(
              "personA" -> JsObject(Map("name" -> JsString("personName"), "age" -> JsNumber(30), "aliases" -> JsArray(List()))),
              "personB" -> JsObject(Map("name" -> JsString("Alan"), "age" -> JsNumber(40), "aliases" -> JsArray(List(JsString("man"), JsString("himan")))))
            )
          )
        )

        forEvery(cases) { (map, expected) =>
          Serializable.ops.serialize(map) should be(expected)
        }
      }
    }
  }
}
