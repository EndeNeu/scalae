package scalaE

class Polymorphism {

  /*

  Parametric polymorphism is obtained when a function works uniformly on a range of types;
  these types normally exhibit some common structure. Parametric polymorphism is so called
  because the uniformity of type structure is normally achieved by type parameters,
  but uniformity can be achieved in different ways,
  and this more general concept is called universal polymorphism.

  Ad-hoc polymorphism is obtained when a function works, or appears to work,
  on several different types (which may not exhibit a common structure)
  and may behave in unrelated ways for each type.

  Parametric and inclusion polymorphism are classified as the two major subcategories of
  “universal polymorphism,” which is contrasted with "nonuniversal" or ad-hoc polymorphism.

  In terms of implementation, a universally polymorphic function will execute the same code
  for arguments of any admissible type, whereas an ad-hoc polymorphic function may execute
  different code for each type of argument.

   */

  case class Node(elem:String, child:List[Node])

  def findMatch(n: Node, n2: Node): Boolean = {
    if(n.elem == n2.elem) n.child.exists(x => findMatch(x, n2))
    else if(n2.child.nonEmpty) n2.child.exists(x => findMatch(n, x))
    else false
  }

  val n5 = Node("n3", List())
  val n4 = Node("n3", List())
  val n2 = Node("n2", List(n4))
  val n3 = Node("n2", List(n5))
  val n1 = Node("n1", List(n2, n3))
  val n12 = Node("n2", List())
  val n13 = Node("n3", List())
  val n11 = Node("n1", List(n12, n13))




}
