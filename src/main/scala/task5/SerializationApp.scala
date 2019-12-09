package task5

case class Person(name: String, age: Int)

object SerializationApp extends App {
   import Serializable.ops.intCanSerialize
   val json = Serializable.ops.serialize(300)
   println(json)

   import Serializable.ops.personCanSerialize
   val p = Person("personName", 30)
   val personJson = Serializable.ops.serialize(p)
   println(personJson) // {"name": "personName", "age": 30}
}
