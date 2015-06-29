name := "sims"

version := "1.1"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2",
  "com.typesafe.akka" %% "akka-actor" % "2.3.11"
)
