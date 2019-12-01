package scalaBasic

package object urlPackage {
  trait Read[T] {
    /**
     * @param string Source from where we going to read a value.
     * @return Either a Right(T) or Left(String) with message about why it failed.
     */
    def read(string: String): Either[String, T]
  }

  trait Show[T] {
    /**
     * @param value T value to represent as a String.
     * @return
     */
    def show(value: T): String
  }
  
  object Read {
    def apply[T](implicit evidence: Read[T]): Read[T] = evidence

    object ops {
      def read[T: Read](value: String): Either[String, T] = Read[T].read(value)

      implicit class ReadOps[T: Read](value: String) {
        def read: Either[String, T] = Read[T].read(value)
      }

      // Example for reading integers
      implicit val intCanRead: Read[Int] = {
        case d if ((d.startsWith("+") || d.startsWith("-")) && d.tail.forall(_.isDigit)) || d.forall(_.isDigit) =>
          Right(d.toInt)
        case invalid => Left(s"""Unable to read an Int from "$invalid".""")
      }
    }
  }

  object Show {
    def apply[T](implicit evidence: Show[T]): Show[T] = evidence

    object ops {
      def show[T: Show](value: T): String = Show[T].show(value)

      implicit class ShowOps[T: Show](value: T) {
        def show: String = Show[T].show(value)
      }

      // Example for displaying integers
      implicit val intCanShow: Show[Int] = _.toString
    }
  }
}
