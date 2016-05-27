package scalaE.definitions

object MethodsAndFunction {


  // method
  def add1(n: Int): Int = n + 1

  // methods are not values, this doesn't compile:
  // val f = add1

  // functions are:
  // The effect of _ is to perform the equivalent of the following:
  // we construct a Function1 instance that delegates to our method.
  val f = add1 _

  // Note also the type of add1, which doesn't look normal; you can't declare a variable of type (n: Int)Int.
  // Methods are not values.

  //In contexts where the compiler expects a function type,
  // the desired expansion is inferred and the underscore is not needed:
  List(1, 2, 3).map(add1 _)
  List(1, 2, 3).map(add1)

  // val z = add1 doeasn't compile
  val z: Int => Int = add1 // does compile, the compiler automatically expands it to a function, like in the map

}