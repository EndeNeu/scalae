package scalaE.definitions

object ReferencialTransparency {


  val x = "E"
  val y = "E"

  // referencial transparency x == y => val x = "E"; val y = x

}

/*
Operational equivalence: given two definitions x and y, x and y are operational
                         equivalent if there's no test that can distinguish between them
 */
