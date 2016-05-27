package scalaE.scalaze

/*

A monad is a parametric type M[T] with two perations, flatMap(bind) and unit

monad laws:

- associativity: (m flatMap f) flatMap g == m flatMap (x => f(x) flatMap g)
- Left unit: unit(x) flatMap f == f(x)
- Right unit m flatMap unit == m

Try is not a monad because it doesn't respect the left unit law:
Try(expr) flatMap f != f(expr)
because  the left hand side will never raise a non fatal exceptions bt the right hand side could.

 */


trait MonadE[T] {

  def flatMap[S](f: T => MonadE[S]): MonadE[S]

  def unit[S](x: S): MonadE[S]

  // map is defined with combination of flatMap and unit
  def map[S](m: MonadE[T], f: T => S): MonadE[S] = m.flatMap(x => unit(f(x)))

}