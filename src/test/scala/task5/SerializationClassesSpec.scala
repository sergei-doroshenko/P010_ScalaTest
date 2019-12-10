package task5

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}
import task5.serializationPackage.Serializable

class SerializationClassesSpec extends WordSpec with Matchers with TableDrivenPropertyChecks {
  "Serializable" can {
    import task5.serializationPackage.Serializable.ops.personCanSerialize
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
  }
}
