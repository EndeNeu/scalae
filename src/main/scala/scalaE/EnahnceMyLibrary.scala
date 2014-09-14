package scalaE

class EnahnceMyLibrary {


  class EnhancedList[T](self: List[T]) {
    def ?:(t: T) =
      t match {
        case null => self
        case _ => t :: self
      }
  }

  implicit def enhanceList[T](self: List[T]) = new EnhancedList(self)

  List(1, 2, 3).?:(1)

}


case class someClass(f1: Int, f2: String, f3: Long)
