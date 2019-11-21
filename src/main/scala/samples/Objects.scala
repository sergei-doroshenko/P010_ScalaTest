package samples

object MyCustomObject {
  private val name = "My hidden name"
  def printName(): Unit = {
    println(name)
  }
}

object Objects extends App {
  MyCustomObject.printName()
}
