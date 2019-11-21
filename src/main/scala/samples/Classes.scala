package samples

object Classes extends App {
  val instance = new MyCustomClass(1, "test01")
  println(instance.id)
  val instance2 = MyCustomClass.instance(2, "test2")
  println(s"id: ${instance2.id}, name: ${instance2.name}")

  val m1 = new MyMutable(2)
  println(m1.x)
  m1.x = 1030
  println(m1.x)

  val im1 = new MyImmutable(100)
  println(im1.value)
}

class MyCustomClass(_id: Int, _name: String) {
  val id = _id
  val name = _name
}
object MyCustomClass {
  def instance(id: Int, name: String) : MyCustomClass = new MyCustomClass(id, name)
}

class MyMutable(var x: Int)

class MyImmutable(val value: Int)
