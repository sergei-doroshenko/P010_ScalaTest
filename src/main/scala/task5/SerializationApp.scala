package task5

case class Person(name: String, age: Int)

object SerializationApp extends App {
   import Serializable.ops.intCanSerialize
   Serializable.ops.serialize(300)

   import Serializable.ops.personCanSerialize
   val p = Person("personName", 30)
   Serializable.ops.serialize(p) // {"name": "personName", "age": 30}
}
