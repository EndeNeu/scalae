package scalaE

class Ordering {


  def check[T](value: T, mini: T, maxi: T)(implicit ord: Ordering[T]): Boolean = {
    import ord.mkOrderingOps
    value >= mini && value <= maxi
  }

}
