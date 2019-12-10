package task5

case class Person(name: String, age: Int, aliases: Seq[String] = List.empty)

object SerializationApp extends App {
   import Serializable.ops.intCanSerialize
   val json = Serializable.ops.serialize(300)
   println(json)

   import Serializable.ops.personCanSerialize
   val p = Person("personName", 30)
   val personJson = Serializable.ops.serialize(p)
   println(personJson) // {"name": "personName", "age": 30}

   val p2 = Person("Alan", 40, List("man", "himan"))
   val p2json = Serializable.ops.serialize(p2)
   println(p2json)

   import Serializable.ops.intArrayCanSerialize
   val intArr = Array(1, 2, 3)
   println(Serializable.ops.serialize(intArr))

   import Serializable.ops.boolListCanSerialize
   val boolArr = List(true, false, false, true)
   println(Serializable.ops.serialize(boolArr))

   import Serializable.ops.strSeqCanSerialize
   val strArr = Seq("one", "two", "three")
   println(Serializable.ops.serialize(strArr))

   import Serializable.ops.personListCanSerialize
   val persons = p :: p2 :: Nil
   println(persons.getClass)
   println(Serializable.ops.serialize(persons))
}
