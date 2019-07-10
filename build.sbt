name := "scala-analyzer"
version := "0.1.3"

scalaVersion := "2.12.8"

val circeVersion = "0.10.0"

libraryDependencies += "org.scalameta" %% "scalameta" % "4.1.9"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic"
).map(_ % circeVersion)
libraryDependencies += "org.typelevel" %% "cats-effect" % "1.3.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

scalacOptions += "-Ypartial-unification"