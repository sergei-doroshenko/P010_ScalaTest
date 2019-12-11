package task5

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}
import task5.serializationPackage.Serializable
import task5.serializationPackage.Serializable.ops._

class SerializationClassesSpec extends WordSpec with Matchers with TableDrivenPropertyChecks {
  "Serializable" can {
    "serialize" should {
      "be able to serialize persons" in {
        val cases = Table(
          ("person", "expected"),
          (
            Person("personName", 30), """{"name":"personName","age":30,"aliases":[]}"""
          ),
          (
            Person("Alan", 40, List("developer","programmer")), """{"name":"Alan","age":40,"aliases":["developer","programmer"]}"""
          )
        )

        forEvery(cases) { (person, expected) =>
          Serializable.ops.serialize(person).toString should be(expected)
        }
      }
    }

    "serialize" should {
      "be able to serialize null" in {
        Serializable.ops.serialize(null).toString should be(null)
      }
    }

    "serialize" should {
      "be able to serialize boolean" in {
        Serializable.ops.serialize(true).toString should be("true")
      }
    }

    "serialize" should {
      "be able to serialize numbers" in {
        Serializable.ops.serialize(10).toString should be("10")
      }
    }

    "serialize" should {
      "be able to serialize strings" in {
        Serializable.ops.serialize("hello").toString should be("\"hello\"")
      }
    }

    "serialize" should {
      "be able to serialize arrays" in {
        val cases = Table(
          ("array", "expected"),
          (
            Array(1, 2, 3), "[1,2,3]"
          )
        )

        forEvery(cases) { (person, expected) =>
          Serializable.ops.serialize(person).toString should be(expected)
        }
      }
    }

    "serialize" should {
      "be able to serialize lists" in {
        val p = Person("personName", 30)
        val p2 = Person("Alan", 40, List("man", "himan"))
        val cases = Table(
          ("list", "expected"),
          (
            p :: p2 :: Nil, """[{"name":"personName","age":30,"aliases":[]},{"name":"Alan","age":40,"aliases":["man","himan"]}]"""
          )
        )

        forEvery(cases) { (person, expected) =>
          Serializable.ops.serialize(person).toString should be(expected)
        }
      }
    }
  }
}
