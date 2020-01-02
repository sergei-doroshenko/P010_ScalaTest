package samples.forcomprehension

import java.time.Instant

trait M[A] {

  def get(): A

  /* applies a transformation of the monad "content" mantaining the
  * monad "external shape"
  * i.e. a List remains a List and an Option remains an Option
  * but the inner type changes
  */
  def map[B](f: A => B): M[B]

  /* applies a transformation of the monad "content" by composing
   * this monad with an operation resulting in another monad instance
   * of the same type
   */
  def flatMap[B](f: A => M[B]): M[B]
}

class Container[A](a: A) extends M[A] {
  override def get(): A = a
  override def map[B](f: A => B): M[B] = Container(f.apply(a))
  override def flatMap[B](f: A => M[B]): M[B] = f.apply(a)
}

object Container {
  def apply[A](a: A): Container[A] = new Container(a)
}

case class Pet0(name: String, birthday: Container[Instant])

case class User0(id: String)

class ForComprehension0 {

  /*implicit val listMonad = new Monad[List[A]] {
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
    def pure[A](a: A): List[A] = List(a)
  }
*/
  def findUser(id: String):        M[User0] = Container(User0("Sergei"))

  def findBestFriend(user: User0):  M[User0] = Container(User0("Andrew"))

  def findFavoritePet(user: User0): M[Pet0] = Container(Pet0("Lapa", Container(Instant.now())))

  def whenIsBirthdayOfTheFavouritePetOfTheBestFriendOfUser0(id: String): M[Instant] = {
    findUser(id)
      .flatMap(findBestFriend)
      .flatMap(findFavoritePet)
      .flatMap(_.birthday)
  }

  def whenIsBirthdayOfTheFavouritePetOfTheBestFriendOfUser(id: String): M[Instant] = {

    for {
      user     <- findUser(id)
      friend   <- findBestFriend(user)
      pet      <- findFavoritePet(friend)
      birthday <- pet.birthday
    } yield birthday

  }
}

object Laucher0 extends App {
  val forComprehension = new ForComprehension
  val birthday = forComprehension.whenIsBirthdayOfTheFavouritePetOfTheBestFriendOfUser("sldfj-xxxx")
  println(f"Birthday: ${birthday}")
}

