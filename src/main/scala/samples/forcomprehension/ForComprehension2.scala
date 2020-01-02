package samples.forcomprehension

import java.time.Instant

sealed trait Monad[A] {
  def flatMap[B](f: A => Monad[B]): Monad[B]
  def pure[A](a: A): Monad[A]
}



case class Pet(name: String, birthday: Option[Instant])

case class User(id: String)

class ForComprehension {

  /*implicit val listMonad = new Monad[List[A]] {
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
    def pure[A](a: A): List[A] = List(a)
  }
*/
  def findUser(id: String):        Option[User] = Option.apply(User("Sergei"))

  def findBestFriend(user: User):  Option[User] = Option.apply(User("Andrew"))

  def findFavoritePet(user: User): Option[Pet] = Option.apply(Pet("Lapa", Option.apply(Instant.now())))

  def whenIsBirthdayOfTheFavouritePetOfTheBestFriendOfUser0(id: String): Option[Instant] = {
    findUser(id)
      .flatMap(findBestFriend)
      .flatMap(findFavoritePet)
      .flatMap(_.birthday)
  }

  def whenIsBirthdayOfTheFavouritePetOfTheBestFriendOfUser(id: String): Option[Instant] = {

    for {
      user     <- findUser(id)
      friend   <- findBestFriend(user)
      pet      <- findFavoritePet(friend)
      birthday <- pet.birthday
    } yield birthday

  }
}

object Laucher extends App {
  val forComprehension = new ForComprehension
  val birthday = forComprehension.whenIsBirthdayOfTheFavouritePetOfTheBestFriendOfUser("sldfj-xxxx")
  println(f"Birthday: ${birthday}")
}

