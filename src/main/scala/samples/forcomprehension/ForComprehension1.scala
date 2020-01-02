package samples.forcomprehension

import java.time.Instant

sealed trait Functor[F[_]] {
  def map[A, B]: (A => B) => F[A] => F[B]
}

sealed trait Applicative[F[_]] extends Functor[F] {
  def ap[A, B]: F[A => B] => F[A] => F[B]
}

sealed trait Monad[F[_]] extends Applicative[F] {
  def bind[A, B]: (A => F[B]) => F[A] => F[B]
}

case class Pet(name: String, birthday: Option[Instant])
case class User(id: String)

case class Building(number: Int)
case class Street(name: String, buildings: Seq[Building])
case class City(population: Int, streets: Seq[Street])
case class Address(city: City, street: Street, building: Building)

class ForComprehension1 {
  val cities: Seq[City] = ???
  val log = new {
    def info(message: => String): Unit = ???
  }

  val addresses: Seq[Address] = for {
    city        <- cities if city.population > 10000
    street    <- city.streets if street.name.startsWith("F")
    building <- street.buildings if building.number % 2 == 1
    result = Address(city, street, building)
    _ = log.info(s"Address: $result")
  } yield result
}

object Laucher1 extends App {
  val forComprehension = new ForComprehension1
  val addresses = forComprehension.addresses
  println(f"Addresses: ${addresses}")
}

