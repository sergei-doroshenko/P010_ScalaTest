package lecture5

// Implement a simple library for Json serialization using Type Classes pattern.
// We want be able to serialize any arbitrary object into a json
// string by providing a conversion to Json AST (abstract syntax tree).

case class Person(name: String, age: Int, aliases: Seq[String] = List.empty)

trait Serializable[T] {
  def serialize(obj: T): JsType
}

object Serializable {
  def apply[T](implicit evidence: Serializable[T]): Serializable[T] = evidence

  object ops {
    def serialize[T: Serializable](value: T): JsType = Serializable[T].serialize(value)

    implicit def nullCanSerialize: Serializable[Null] = (_) => JsNull()
    implicit val boolCanSerialize: Serializable[Boolean] = (obj: Boolean) => JsBool(obj)
    implicit val intCanSerialize: Serializable[Int] = (obj: Int) => JsNumber(obj)
    implicit val strCanSerialize: Serializable[String] = (obj: String) => JsString(obj)

    implicit val personCanSerialize: Serializable[Person] = (p: Person) => JsObject(
      "name" -> JsString(p.name),
      "age" -> JsNumber(p.age),
      "aliases" -> JsArray(p.aliases)
    )

    implicit def personListCanSerialize: Serializable[List[Person]] = (s: List[Person]) => JsArray(s)

    implicit def intSeqCanSerialize: Serializable[Seq[Int]] = (s: Seq[Int]) => JsArray(s)
    implicit def intArrayCanSerialize: Serializable[Array[Int]] = (s: Array[Int]) => JsArray(s)

    implicit def strSeqCanSerialize: Serializable[Seq[String]] = (s: Seq[String]) => JsArray(s)

//    implicit def boolListCanSerialize: Serializable[List[Boolean]] = (s: Seq[Boolean]) => JsArray(s)

//    implicit def intArrayCanSerialize: Serializable[Array[Int]] = (s: Array[Int]) => JsArray(s)
  }

}

trait JsType

case class JsObject(fields: Map[String, JsType]) extends JsType
object JsObject {
  def apply(fields: (String, JsType)*): JsObject = JsObject(fields.toMap)
}

case class JsNull() extends JsType

case class JsBool(value: Boolean) extends JsType

case class JsNumber(value: Int) extends JsType

case class JsString(value: String) extends JsType

case class JsArray(value: Seq[JsType]) extends JsType
object JsArray {
  def apply[T](value: Seq[T])(implicit ser: Serializable[T]): JsArray = JsArray(value.map(v => ser.serialize(v)))
  /*def apply(value: Seq[T]): JsArray = value match {
    case b: Seq[Boolean] => JsArray[JsBool](b.map(b => JsBool(b)))
    case i: Seq[Int] => JsArray(i.map(i => JsNumber(i)))
    case s: Seq[String] => JsArray(s.map(e => JsString(e)))
    case _ => JsArray(Seq.empty)
  }*/
}


// For example:
//
// We have class
// case class Person(name: String, age: Int)
//
// For this class we should provide a conversion to Json AST (through implicit scope) like this:
//
// def serialize(p: Person) = JSObject(
// "name" -> JsString(p.name),
// "age" -> JsNumber(p.age)
// )
//
// Then we can serialize person:
//
// val p = Person("personName", 30)
// serialize(p) // {"name": "personName", "age": 30}
