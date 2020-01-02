package samples.forcomprehension

import java.time.Instant

case class Pet(name: String, birthday: Option[Instant])

case class User(id: String)

class ForComprehension {
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

