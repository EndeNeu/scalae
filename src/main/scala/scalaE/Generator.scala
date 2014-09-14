package scalaE

trait Generator[+T] {
  self =>

  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S]{
    def generate: S = f(self.generate)
    // if it was this.generate we would have recursion because this refers to Generator[S]
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    def generate: S = f(self.generate).generate
  }

}


object generators{

  val integers = new Generator[Int] {
    val rand = new java.util.Random()
    def generate = rand.nextInt()
  }


  val booleanGenerator = new Generator[Boolean] {
    override def generate: Boolean = integers.generate > 0
  }

}