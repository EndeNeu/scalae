name := "scalaE"

version := "1.0"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)



libraryDependencies ++= Seq(
  "com.netflix.rxjava" % "rxjava-scala" % "0.15.0",
  "org.json4s" % "json4s-native_2.10" % "3.2.5",
  "org.scala-lang" % "scala-swing" % "2.10.3",
  "net.databinder.dispatch" % "dispatch-core_2.10" % "0.11.0",
  "org.scala-lang" % "scala-reflect" % "2.10.3",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  "com.squareup.retrofit" % "retrofit" % "1.0.0",
  "org.scala-lang.modules" %% "scala-async" % "0.9.0-M2",
  "org.scalaz" %% "scalaz-core" % "7.2.0",
  "com.chuusai" %% "shapeless" % "2.2.5"
)