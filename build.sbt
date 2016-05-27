name := "scalaE"

version := "1.0"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "com.netflix.rxjava" % "rxjava-scala" % "0.15.0",
  //"org.json4s" % "json4s-native_2.10" % "3.2.5",
  //"org.scala-lang" % "scala-swing" % "2.10.3",
  //"net.databinder.dispatch" % "dispatch-core_2.10" % "0.11.0",
  //"org.scala-lang" % "scala-reflect" % "2.10.3",
  //"org.slf4j" % "slf4j-api" % "1.7.5",
  //"org.slf4j" % "slf4j-simple" % "1.7.5",
  //"com.squareup.retrofit" % "retrofit" % "1.0.0",
  "org.scala-lang.modules" % "scala-async_2.11" % "0.9.6-RC2",
  "org.scalaz" %% "scalaz-core" % "7.2.0",
  "com.chuusai" %% "shapeless" % "2.3.0-RC4",
  "org.typelevel" %% "cats" % "0.6.0",
  "com.github.julien-truffaut"  %%  "monocle-core"    % "1.2.1",
  "com.github.julien-truffaut"  %%  "monocle-generic" % "1.2.1",
  "com.github.julien-truffaut"  %%  "monocle-macro"   % "1.2.1",
  "com.github.julien-truffaut"  %%  "monocle-state"   % "1.2.1",
  "com.github.julien-truffaut"  %%  "monocle-refined" % "1.2.1",
  "com.github.julien-truffaut"  %%  "monocle-law"     % "1.2.1" % "test"
)

addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
